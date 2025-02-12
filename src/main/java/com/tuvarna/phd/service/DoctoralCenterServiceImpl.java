package com.tuvarna.phd.service;

import com.tuvarna.phd.entity.Committee;
import com.tuvarna.phd.entity.DoctoralCenter;
import com.tuvarna.phd.entity.DoctoralCenterRole;
import com.tuvarna.phd.entity.Phd;
import com.tuvarna.phd.entity.PhdStatus;
import com.tuvarna.phd.entity.UnauthorizedUsers;
import com.tuvarna.phd.exception.DoctoralCenterException;
import com.tuvarna.phd.exception.UserException;
import com.tuvarna.phd.repository.CommitteeRepository;
import com.tuvarna.phd.repository.DoctoralCenterRepository;
import com.tuvarna.phd.repository.DoctoralCenterRoleRepository;
import com.tuvarna.phd.repository.PhdRepository;
import com.tuvarna.phd.repository.PhdStatusRepository;
import com.tuvarna.phd.repository.UnauthorizedUsersRepository;
import com.tuvarna.phd.service.dto.CandidateDTO;
import com.tuvarna.phd.service.dto.RoleDTO;
import com.tuvarna.phd.service.dto.UnauthorizedUsersDTO;
import com.tuvarna.phd.service.dto.UserDTO;
import io.quarkus.mailer.Mail;
import io.quarkus.mailer.reactive.ReactiveMailer;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

@ApplicationScoped
public final class DoctoralCenterServiceImpl implements DoctoralCenterService {
  private final DoctoralCenterRepository doctoralCenterRepository;
  private final DoctoralCenterRoleRepository doctoralCenterRoleRepository;
  private final PhdRepository phdRepository;
  private final CommitteeRepository committeeRepository;
  private final PhdStatusRepository sPhdRepository;
  private final UnauthorizedUsersRepository uRepository;

  @Inject private Logger LOG = Logger.getLogger(DoctoralCenterServiceImpl.class);
  @Inject private ReactiveMailer mailer;

  @ConfigProperty(name = "client.base-url")
  private String clientBaseURL;

  @Inject
  public DoctoralCenterServiceImpl(
      DoctoralCenterRepository doctoralCenterRepository,
      PhdRepository phdRepository,
      DoctoralCenterRoleRepository doctoralCenterRoleRepository,
      PhdStatusRepository sPhdRepository,
      CommitteeRepository committeeRepository,
      UnauthorizedUsersRepository uRepository) {
    this.doctoralCenterRepository = doctoralCenterRepository;
    this.phdRepository = phdRepository;
    this.doctoralCenterRoleRepository = doctoralCenterRoleRepository;
    this.sPhdRepository = sPhdRepository;
    this.uRepository = uRepository;
    this.committeeRepository = committeeRepository;
  }

  @Override
  @Transactional
  public void updateCandidateStatus(CandidateDTO candidateDTO, String oid) {
    LOG.info(
        "Service received a request to update status for candidate: " + candidateDTO.toString());

    Phd phd = this.phdRepository.getByEmail(candidateDTO.getEmail());
    PhdStatus statusPhd = this.sPhdRepository.getByStatus(candidateDTO.getStatus());

    LOG.info("Updating candidate status to: " + candidateDTO.getStatus());
    phd.setStatus(statusPhd);
  }

  @Override
  @Transactional
  public void deleteAuthorizedUser(String oid, RoleDTO role) {
    switch (role.getRole()) {
      case "phd" -> this.phdRepository.deleteByOid(oid);
      case "committee" -> this.committeeRepository.deleteByOid(oid);
      case "doctoralCenter" -> this.doctoralCenterRepository.deleteByOid(oid);

      default -> throw new UserException("Role is incorrect!", 400);
    }
  }

  @Override
  public Uni<Void> sendEmail(String email) {
    String body =
        String.format(
            """
            <html>
            <body>
                <h3> Добре дошли в докторската платформа за Технически университет Варна!</h3>
                <p>Вие сте одобрен във вашата кандидатура</p>
                <p>Създаден ви е профил в Azure Active Directory и в докторантската платформа</p>
                <p><strong>Линк:</strong> %s </p>
                <p>С най-добри пожелания,</p>
                <p> Екипът на Технически университет Варна</p>
            </body>
            </html>
            """,
            this.clientBaseURL);

    return this.mailer.send(
        Mail.withHtml(email, "Добре дошли в Технически университет Варна!", body));
  }

  @Override
  @Transactional
  public List<UnauthorizedUsers> getUnauthorizedUsers() {
    LOG.info("Service received to retrieve all unauthorized users");
    List<UnauthorizedUsers> unauthorizedUsers = this.uRepository.getAll();

    return unauthorizedUsers;
  }

  @Override
  @Transactional
  public List<UserDTO> getAuthorizedUsers() {
    LOG.info("Service received to retrieve all unauthorized users");
    List<UserDTO> authenticatedUsers = new ArrayList<>();
    List<Phd> phds = this.phdRepository.getAll();
    List<Committee> committees = this.committeeRepository.getAll();
    List<DoctoralCenter> doctoralCenters = this.doctoralCenterRepository.getAll();

    for (Phd phd : phds) {
      UserDTO user =
          new UserDTO(phd.getId(), phd.getOid(), phd.getFullName(), phd.getEmail(), "phd");
      authenticatedUsers.add(user);
    }

    for (Committee commitee : committees) {
      UserDTO user =
          new UserDTO(
              commitee.getId(),
              commitee.getOid(),
              commitee.getName(),
              commitee.getEmail(),
              "committee");
      authenticatedUsers.add(user);
    }

    for (DoctoralCenter dCenter : doctoralCenters) {
      UserDTO user =
          new UserDTO(
              dCenter.getId(),
              dCenter.getOid(),
              dCenter.getName(),
              dCenter.getEmail(),
              dCenter.getRole().getRole());
      authenticatedUsers.add(user);
    }

    return authenticatedUsers;
  }

  @Override
  @Transactional
  public void setUnauthorizedUserRole(List<UnauthorizedUsersDTO> usersDTO, String role) {
    LOG.info(
        "Service received a request to set a role: "
            + role
            + "for unauthorized user: "
            + usersDTO.toString());

    for (UnauthorizedUsersDTO userDTO : usersDTO) {
      UnauthorizedUsers user = this.uRepository.getByOid(userDTO.getOid());
      this.createUserByRole(userDTO.getOid(), userDTO.getName(), userDTO.getEmail(), role);

      LOG.info(
          "User created for a role: "
              + role
              + " ! Now deleting him from unauthorized users table...");
      this.uRepository.delete(user);

      LOG.info("User" + user.getEmail() + "deleted from that table!");
    }
  }

  private void createUserByRole(String oid, String name, String email, String role) {
    switch (role) {
      // TODO: Not sure if the admin should add users as committees
      // case "committee":
      //   Committee committee = new Committee(oid, name, email);
      //   this.committeeRepository.save(committee);
      //   break;
      case "expert", "manager", "admin":
        DoctoralCenter dCenter = new DoctoralCenter(oid, name, email);
        DoctoralCenterRole doctoralCenterRole = this.doctoralCenterRoleRepository.getByRole(role);
        dCenter.setRole(doctoralCenterRole);

        this.doctoralCenterRepository.save(dCenter);
        break;
      default:
        throw new DoctoralCenterException("Role: " + role + " doesn't exist!");
    }
  }
}
