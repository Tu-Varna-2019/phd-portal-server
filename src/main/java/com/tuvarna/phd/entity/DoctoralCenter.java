package com.tuvarna.phd.entity;

import com.tuvarna.phd.utils.Generator;
import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.security.jpa.Password;
import io.quarkus.security.jpa.Username;
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
@Table(name = "doctoralCenter")
public class DoctoralCenter extends PanacheEntityBase {
  // Решат дали докторанта е одобрен или ne -
  //  Справка от всички докторанти за техните изпити и оценки, от които са получили
  //  Експерта назначава коя комисия/преподавател на кой изпит

  @Id
  @SequenceGenerator(
      name = "doctoralCenterSequence",
      sequenceName = "doctoralCenter_id_seq",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "doctoralCenterSequence")
  private Long id;

  @Username
  @Column(nullable = false, unique = false)
  private String name;

  @Column(nullable = false, unique = false)
  private String email;

  @Column(nullable = false, unique = false)
  @Password
  private String password;

  ////////////////////////////// ADMIN ONLY///////////////////////////

  // NOTE: only admin
  // expert doctor center and manager are going to be added to AAD

  @Column(nullable = false, unique = false)
  private boolean isPasswordChangeRequired;

  @Transient private String unhashedPassword;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "DoctoralCenterRole", nullable = false)
  private DoctoralCenterRole role;

  public void hashPassword() {
    unhashedPassword = this.password;
    this.password = BcryptUtil.bcryptHash(this.password);
  }

  public void generatePassword(Integer length) {
    this.password = Generator.generateRandomString(length);
  }

  public boolean doesPasswordMatch(String plainPassword) {

    return BcryptUtil.matches(plainPassword, this.password);
  }
}
