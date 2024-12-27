package com.tuvarna.phd.service.impl;

import com.tuvarna.phd.entity.UnauthorizedUsers;
import com.tuvarna.phd.exception.CommitteeException;
import com.tuvarna.phd.exception.DoctoralCenterException;
import com.tuvarna.phd.exception.PhdException;
import com.tuvarna.phd.exception.UserException;
import com.tuvarna.phd.mapper.UnauthorizedUsersMapper;
import com.tuvarna.phd.repository.CommitteeRepository;
import com.tuvarna.phd.repository.DoctoralCenterRepository;
import com.tuvarna.phd.repository.PhdRepository;
import com.tuvarna.phd.repository.UnauthorizedUsersRepository;
import com.tuvarna.phd.service.AuthService;
import com.tuvarna.phd.service.dto.UnauthorizedUsersDTO;
import io.smallrye.mutiny.tuples.Tuple2;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.Optional;
import org.jboss.logging.Logger;

@ApplicationScoped
public class AuthServiceImpl implements AuthService {

  private final PhdRepository pRepository;
  private final DoctoralCenterRepository doctoralCenterRepository;
  private final CommitteeRepository committeeRepository;
  private final UnauthorizedUsersRepository usersRepository;
  private final UnauthorizedUsersMapper mapper;

  private String role;

  @Inject private Logger LOG = Logger.getLogger(AuthServiceImpl.class);

  public AuthServiceImpl(
      PhdRepository pRepository,
      DoctoralCenterRepository doctoralCenterRepository,
      CommitteeRepository committeeRepository,
      UnauthorizedUsersRepository usersRepository,
      UnauthorizedUsersMapper mapper) {

    this.pRepository = pRepository;
    this.doctoralCenterRepository = doctoralCenterRepository;
    this.committeeRepository = committeeRepository;
    this.usersRepository = usersRepository;
    this.mapper = mapper;
  }

  @Override
  @Transactional(dontRollbackOn = UserException.class)
  public Tuple2<Object, String> login(UnauthorizedUsersDTO userDTO) {
    LOG.info("Service received a request to login as a user: " + userDTO);

    return Tuple2.of(this.verifyUserInDB(userDTO), this.role);
  }

  private Object verifyUserInDB(UnauthorizedUsersDTO userDTO) {
    String oid = userDTO.getOid();

    // NOTE: Check if user is in phd -> commitee -> doctor center tables sequentially
    Object obj =
        Optional.ofNullable(isUserInPHDTable(oid))
            .or(() -> Optional.ofNullable(isUserInCommitteeTable(oid)))
            .or(() -> Optional.ofNullable(isUserInDoctoralCenterTable(oid)))
            .orElse(null);

    // NOTE: if user is actually found, then return it
    if (obj != null) return obj;

    // NOTE: Else, add it to the unauthorized users repo if it's not already in it
    if (this.usersRepository.getByOid(oid) == null) {
      LOG.info("User: " + userDTO.getEmail() + " is not present in the table. Adding him now...");
      // LOG.info("Timestamp: "+userDTO.g)
      UnauthorizedUsers users = this.mapper.toEntity(userDTO);
      this.usersRepository.save(users);
    } else
      LOG.info(
          "User: " + userDTO.getEmail() + " is already present in the table. No need to add him");

    throw new UserException(
        "User: "
            + userDTO.getOid()
            + " is not present in neither phd, committee or doctoral center tables!",
        401);
  }

  private Object isUserInPHDTable(String oid) {
    LOG.info("Checking if user: " + oid + " is in phd table...");
    try {
      this.role = "phd";
      return this.pRepository.getByOid(oid);
    } catch (PhdException exPhd) {
      LOG.warn("User is not in phd table. Now checking if he's present in committee table... ");
      return null;
    }
  }

  private Object isUserInCommitteeTable(String oid) {
    try {
      this.role = "committee";
      return this.committeeRepository.getByOid(oid);
    } catch (CommitteeException exComm) {
      LOG.warn(
          "User is not in committee table. Now checking if he's present in doctoral center"
              + " table... ");
      return null;
    }
  }

  private Object isUserInDoctoralCenterTable(String oid) {
    try {
      this.role = "doctoralCenter";
      return this.doctoralCenterRepository.getByOid(oid);
    } catch (DoctoralCenterException exDoc) {
      LOG.warn("User not found in doctoral center table!");
      return null;
    }
  }
}
