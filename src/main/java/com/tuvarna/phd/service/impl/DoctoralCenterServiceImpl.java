package com.tuvarna.phd.service.impl;

import com.tuvarna.phd.entity.Committee;
import com.tuvarna.phd.entity.DoctoralCenter;
import com.tuvarna.phd.entity.DoctoralCenterRole;
import com.tuvarna.phd.entity.Phd;
import com.tuvarna.phd.entity.StatusPhd;
import com.tuvarna.phd.entity.UnauthorizedUsers;
import com.tuvarna.phd.exception.DoctoralCenterException;
import com.tuvarna.phd.repository.CommitteeRepository;
import com.tuvarna.phd.repository.DoctoralCenterRepository;
import com.tuvarna.phd.repository.DoctoralCenterRoleRepository;
import com.tuvarna.phd.repository.PhdRepository;
import com.tuvarna.phd.repository.StatusPhdRepository;
import com.tuvarna.phd.repository.UnauthorizedUsersRepository;
import com.tuvarna.phd.service.DoctoralCenterService;
import com.tuvarna.phd.service.dto.PhdDTO;
import com.tuvarna.phd.service.dto.UnauthorizedUsersDTO;
import io.quarkus.mailer.Mail;
import io.quarkus.mailer.reactive.ReactiveMailer;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

@ApplicationScoped
public class DoctoralCenterServiceImpl implements DoctoralCenterService {
  private final DoctoralCenterRepository doctoralCenterRepository;
  private final DoctoralCenterRoleRepository doctoralCenterRoleRepository;
  private final PhdRepository phdRepository;
  private final CommitteeRepository committeeRepository;
  private final StatusPhdRepository sPhdRepository;
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
      StatusPhdRepository sPhdRepository,
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
  public void updatePhdStatus(PhdDTO pDto, String status) {
    LOG.info("Service received a request to update status for  phd user: " + pDto);

    Phd phd = this.phdRepository.getByEmail(pDto.getEmail());
    StatusPhd statusPhd = this.sPhdRepository.getByStatus(status);

    LOG.info("Updating phd status to: " + status);
    phd.setStatus(statusPhd);
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
  public void setUnauthorizedUserRole(UnauthorizedUsersDTO usersDTO, String role) {
    LOG.info(
        "Service received a request to set role for unauthorized user: " + usersDTO.getEmail());

    UnauthorizedUsers users = this.uRepository.getByOid(usersDTO.getOid());
    this.createUserByRole(usersDTO.getOid(), usersDTO.getName(), usersDTO.getEmail(), role);
    LOG.info(
        "User created for a role: "
            + role
            + " ! Now deleting him from unauthorized users table...");
    this.uRepository.delete(users);
    LOG.info("User deleted from that table!");
  }

  private void createUserByRole(String oid, String name, String email, String role) {
    switch (role) {
      case "committee":
        Committee committee = new Committee(oid, name, email);
        this.committeeRepository.save(committee);
        break;
      case "expert", "manager":
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
