package com.tuvarna.phd.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.security.jpa.Password;
import io.vertx.mutiny.sqlclient.Row;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "candidate")
public non-sealed class Candidate extends PanacheEntityBase implements IUserEntity<Candidate> {

  @Id
  @SequenceGenerator(
      name = "candidateSequence",
      sequenceName = "candidate_id_seq",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "candidateSequence")
  private Long id;

  @Column(nullable = false, unique = false)
  private String name;

  @Column(nullable = false, unique = false)
  private String email;

  @Column(nullable = false, unique = false)
  private String country;

  @Column(nullable = false, unique = false)
  private String city;

  @Column(nullable = false, unique = false)
  private String address;

  @Column(nullable = false, unique = false)
  private String biography;

  @Transient private String biographyBlob;

  @Column(nullable = false, unique = false)
  private String status = "waiting";

  @Password
  @Column(nullable = false, unique = true, length = 10)
  // TODO: Encrypt this pls
  // ЕГН
  private String pin;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "curriculum", nullable = true)
  private Curriculum curriculum;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "faculty", nullable = true)
  private Faculty faculty;

  public Candidate(String name, String email, String biography, String status) {
    this.name = name;
    this.email = email;
    this.biography = biography;
    this.status = status;
  }

  @Override
  public Candidate toEntity(Row row) {
    return new Candidate(
        row.getString("name"),
        row.getString("email"),
        row.getString("biography"),
        row.getString("status"));
  }
}
