package com.tuvarna.phd.service;

import com.tuvarna.phd.entity.Committee;
import com.tuvarna.phd.entity.DoctoralCenter;
import com.tuvarna.phd.entity.IUserEntity;
import com.tuvarna.phd.entity.Phd;
import com.tuvarna.phd.entity.Unauthorized;
import com.tuvarna.phd.exception.HttpException;
import com.tuvarna.phd.mapper.UnauthorizedMapper;
import com.tuvarna.phd.model.DatabaseModel;
import com.tuvarna.phd.model.S3Model;
import com.tuvarna.phd.repository.CommitteeRepository;
import com.tuvarna.phd.repository.DoctoralCenterRepository;
import com.tuvarna.phd.repository.PhdRepository;
import com.tuvarna.phd.repository.UnauthorizedRepository;
import io.quarkus.security.UnauthorizedException;
import io.smallrye.mutiny.tuples.Tuple2;
import io.vertx.mutiny.sqlclient.Tuple;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.sql.Timestamp;
import java.util.List;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

@ApplicationScoped
public final class AuthServiceImpl implements AuthService {

  @Inject UnauthorizedMapper mapper;

  @Inject DatabaseModel databaseModel;

  @Inject S3Model s3Model;

  @Inject DoctoralCenterRepository doctoralCenterRepository;

  @Inject CommitteeRepository committeeRepository;

  @Inject PhdRepository phdRepository;

  @Inject UnauthorizedRepository unauthorizedUsersRepository;

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
  public void addToUnauthorizedTable(String oid, String name, String email, String group) {
    LOG.info("User: " + email + " is not present in any of the tables. Adding him now...");

    Unauthorized users =
        new Unauthorized(oid, name, email, new Timestamp(System.currentTimeMillis()));
    this.unauthorizedUsersRepository.save(users);

    throw new UnauthorizedException(
        "User: " + oid + " is not present in neither phd, committee or doctoral center tables!");
  }

  @Override
  @Transactional(dontRollbackOn = UnauthorizedException.class)
  public Tuple2<IUserEntity<?>, String> login(String oid, String name, String email) {
    String group;
    LOG.info("Service received a request to login as a user: " + email);

    if (!(group = this.getGroupByOid(oid)).isEmpty()) {
      return Tuple2.of(this.getAuthenticatedUser(oid, group), group);
    } else {
      this.addToUnauthorizedTable(oid, name, email, group);
    }
    return null;
  }

  @Override
  public IUserEntity<?> getAuthenticatedUser(String oid, String group) {
    switch (group) {
      case "phd" -> {
        Phd phd = this.phdRepository.getByOid(oid);
        phd.setPictureBlob(this.s3Model.getDataUrlPicture(oid, phd.getPicture()));
        return phd;
      }
      case "committee" -> {
        Committee committee = this.committeeRepository.getByOid(oid);
        committee.setPictureBlob(this.s3Model.getDataUrlPicture(oid, committee.getPicture()));
        return committee;
      }
      case "doctoralCenter" -> {
        DoctoralCenter doctoralCenter = this.doctoralCenterRepository.getByOid(oid);
        doctoralCenter.setPictureBlob(
            this.s3Model.getDataUrlPicture(oid, doctoralCenter.getPicture()));
        return doctoralCenter;
      }
      default -> {
        throw new HttpException("Error: Cannot getUser because of non existing group!");
      }
    }
  }
}
