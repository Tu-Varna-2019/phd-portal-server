package com.tuvarna.phd.service.impl;

import com.tuvarna.phd.entity.UnauthorizedUsers;
import com.tuvarna.phd.exception.CommitteeException;
import com.tuvarna.phd.exception.DoctoralCenterException;
import com.tuvarna.phd.exception.PhdException;
import com.tuvarna.phd.mapper.UnauthorizedUsersMapper;
import com.tuvarna.phd.repository.CommitteeRepository;
import com.tuvarna.phd.repository.DoctoralCenterRepository;
import com.tuvarna.phd.repository.PhdRepository;
import com.tuvarna.phd.repository.UnauthorizedUsersRepository;
import com.tuvarna.phd.service.AuthService;
import com.tuvarna.phd.service.dto.UserDTO;
import io.smallrye.mutiny.tuples.Tuple2;
import io.vertx.ext.web.handler.HttpException;
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
  @Transactional
  public Tuple2<Object, String> login(UserDTO userDTO) {
    LOG.info("Service received a request to login as a user: " + userDTO);

    return Tuple2.of(this.verifyUserInDB(userDTO), this.role);
  }

  private Object verifyUserInDB(UserDTO userDTO) {
    String oid = userDTO.getOid();

    // NOTE: Check if user is in phd -> commitee -> doctor center tables sequentially
    Object obj =
        Optional.ofNullable(isUserInPHDTable(oid))
            .orElse(
                Optional.ofNullable(isUserInCommitteeTable(oid))
                    .orElse(Optional.ofNullable(isUserInDoctoralCenterTable(oid))));

    // NOTE: if user is actually found, then return it
    if (obj != null) return obj;

    // NOTE: Else, add it to the unauthorized users repo if it's not already in it
    if (this.usersRepository.getByOid(oid) == null) {
      UnauthorizedUsers users = this.mapper.toEntity(userDTO);
      this.usersRepository.save(users);
    }

    throw new HttpException(
        400,
        "User: "
            + userDTO.getOid()
            + " is not present in neither phd, committee or doctoral center tables!");
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
      LOG.error(
          "User not found in any of the tables! Adding him to unauthorized users table now...");
      return null;
    }
  }
}
