package com.tuvarna.phd.service;

import com.tuvarna.phd.dto.UnauthorizedUsersDTO;
import com.tuvarna.phd.entity.Committee;
import com.tuvarna.phd.entity.DoctoralCenter;
import com.tuvarna.phd.entity.Phd;
import com.tuvarna.phd.entity.UnauthorizedUsers;
import com.tuvarna.phd.entity.UserEntity;
import com.tuvarna.phd.exception.HttpException;
import com.tuvarna.phd.mapper.UnauthorizedUsersMapper;
import com.tuvarna.phd.model.DatabaseModel;
import com.tuvarna.phd.model.S3Model;
import com.tuvarna.phd.repository.CommitteeRepository;
import com.tuvarna.phd.repository.DoctoralCenterRepository;
import com.tuvarna.phd.repository.PhdRepository;
import com.tuvarna.phd.repository.UnauthorizedUsersRepository;
import io.smallrye.mutiny.tuples.Tuple2;
import io.vertx.mutiny.sqlclient.Tuple;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

@ApplicationScoped
public final class AuthServiceImpl implements AuthService {

  @Inject UnauthorizedUsersMapper mapper;

  @Inject DatabaseModel databaseModel;

  @Inject S3Model s3Model;

  @Inject DoctoralCenterRepository doctoralCenterRepository;

  @Inject CommitteeRepository committeeRepository;

  @Inject PhdRepository phdRepository;

  @Inject UnauthorizedUsersRepository unauthorizedUsersRepository;

  @ConfigProperty(name = "user.groups")
  List<String> groups;

  @Inject private Logger LOG = Logger.getLogger(AuthServiceImpl.class);

  @Override
  public String getGroupByOid(String oid) {
    String statement;

    for (String group : this.groups) {
      statement = ("SELECT EXISTS (SELECT 1 FROM " + group + " WHERE oid = $1)");
      Boolean isUserFound = this.databaseModel.selectIfExists(statement, Tuple.of(oid));

      if (isUserFound) {
        return group;
      }
    }

    return "";
  }

  @Override
  public void addToUnauthorized(UnauthorizedUsersDTO userDTO, String group) {
    if (this.unauthorizedUsersRepository.getByOid(userDTO.getOid()) == null) {
      LOG.info(
          "User: "
              + userDTO.getEmail()
              + " is not present in any of the tables. Adding him now...");

      UnauthorizedUsers users = this.mapper.toEntity(userDTO);
      this.unauthorizedUsersRepository.save(users);
    } else {
      LOG.info(
          "User: " + userDTO.getEmail() + " is already present in the table. No need to add him");
    }
    throw new HttpException(
        "User: "
            + userDTO.getOid()
            + " is not present in neither phd, committee or doctoral center tables!",
        401);
  }

  @Override
  @Transactional(dontRollbackOn = HttpException.class)
  public Tuple2<UserEntity<?>, String> login(UnauthorizedUsersDTO userDTO) {
    String group;
    LOG.info("Service received a request to login as a user: " + userDTO);

    if (!(group = this.getGroupByOid(userDTO.getOid())).isEmpty()) {
      return Tuple2.of(this.getUser(userDTO.getOid(), group), group);
    } else {
      this.addToUnauthorized(userDTO, group);
    }
    return null;
  }

  @Override
  public UserEntity<?> getUser(String oid, String group) {
    if (group.equals(this.groups.get(0))) {
      Phd phd = this.phdRepository.getByOid(oid);
      phd.setPictureBlob(this.setPictureBlobBase64(oid, phd.getPicture()));
      return phd;
    } else if (group.equals(this.groups.get(1))) {
      Committee committee = this.committeeRepository.getByOid(oid);
      committee.setPictureBlob(this.setPictureBlobBase64(oid, committee.getPicture()));
      return committee;
    } else if (group.equals(this.groups.get(2))) {
      DoctoralCenter doctoralCenter = this.doctoralCenterRepository.getByOid(oid);
      doctoralCenter.setPictureBlob(this.setPictureBlobBase64(oid, doctoralCenter.getPicture()));
      return doctoralCenter;
    }
    throw new HttpException("Error: Cannot getUser because of non existing group!");
  }

  private String setPictureBlobBase64(String oid, String picture) {
    if (picture.isEmpty()) return "";

    return this.s3Model.getDataUrlPicture(oid, picture);
  }
}
