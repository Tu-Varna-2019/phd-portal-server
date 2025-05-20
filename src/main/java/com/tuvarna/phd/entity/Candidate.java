package com.tuvarna.phd.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.security.jpa.Password;
import io.vertx.core.json.JsonObject;
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

  @Transient
  @JsonProperty("facultyname")
  private String facultyName;

  @Transient
  @JsonProperty("curriculumname")
  private String curriculumName;

  @Transient
  @JsonProperty("statusname")
  private String statusName;

  @Column(nullable = false, unique = false)
  private String email;

  @Column(nullable = false, unique = false)
  private String country;

  @Column(nullable = false, unique = false)
  private String city;

  @Column(nullable = false, unique = false)
  private String address;

  @Column(name = "post_code", nullable = false, unique = false)
  @JsonProperty("post_code")
  private String postCode;

  @Column(nullable = false, unique = false)
  private String biography;

  @Transient private String biographyBlob;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "status", nullable = false)
  private CandidateStatus status;

  @Column(name = "exam_step", nullable = false, unique = false, length = 1)
  @JsonProperty("exam_step")
  private Integer examStep;

  @Column(name = "year_accepted", nullable = true, unique = false)
  @JsonProperty("yearaccepted")
  private Long yearAccepted;

  @Password
  @Column(nullable = false, unique = true, length = 10)
  // TODO: Encrypt this pls
  // ЕГН
  private String pin;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "curriculum", nullable = true)
  private Curriculum curriculum;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "faculty", nullable = false)
  private Faculty faculty;

  public Candidate setFaculty(Faculty faculty) {
    this.faculty = faculty;
    return this;
  }

  public Candidate setStatus(CandidateStatus status) {
    this.status = status;
    return this;
  }

  @Override
  public Candidate toEntity(Row row) {
    JsonObject jsonObject = row.toJson();

    return jsonObject.mapTo(Candidate.class);
  }
}
