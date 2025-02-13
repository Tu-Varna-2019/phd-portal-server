package com.tuvarna.phd.service;

import com.tuvarna.phd.dto.UnauthorizedUsersDTO;
import com.tuvarna.phd.entity.Committee;
import com.tuvarna.phd.entity.DoctoralCenter;
import com.tuvarna.phd.entity.Phd;
import com.tuvarna.phd.entity.UnauthorizedUsers;
import com.tuvarna.phd.entity.UserEntity;
import com.tuvarna.phd.exception.UserException;
import com.tuvarna.phd.mapper.UnauthorizedUsersMapper;
import com.tuvarna.phd.repository.UnauthorizedUsersRepository;
import com.tuvarna.phd.repository.UserRepositoryStrategy;
import io.smallrye.mutiny.tuples.Tuple2;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.Tuple;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

@ApplicationScoped
public final class AuthServiceImpl implements AuthService {

  private final UnauthorizedUsersMapper mapper;
  private final PgPool pgClient;

  @ConfigProperty(name = "user.groups")
  List<String> groups;

  @Inject private Logger LOG = Logger.getLogger(AuthServiceImpl.class);

  public AuthServiceImpl(PgPool pgClient, UnauthorizedUsersMapper mapper) {
    this.pgClient = pgClient;
    this.mapper = mapper;
  }

  @Override
  public String getGroupByOid(String oid) {
    String statement;

    for (String group : this.groups) {
      statement = ("SELECT EXISTS (SELECT 1 FROM " + group + " WHERE oid = $1)");

      Boolean isUserFound =
          this.pgClient
              .preparedQuery(statement)
              .execute(Tuple.of(oid))
              .onItem()
              .transform(rowSet -> rowSet.iterator().next().getBoolean(0))
              .await()
              .indefinitely();

      if (isUserFound) {
        return group;
      }
    }

    return "";
  }

  @Override
  public void addToUnauthorized(UnauthorizedUsersDTO userDTO, String group) {
    UserRepositoryStrategy<UnauthorizedUsers> unauthorizedRepository =
        new UnauthorizedUsersRepository();

    if (unauthorizedRepository.getByOid(userDTO.getOid()) == null) {
      LOG.info(
          "User: "
              + userDTO.getEmail()
              + " is not present in any of the tables. Adding him now...");

      UnauthorizedUsers users = this.mapper.toEntity(userDTO);
      unauthorizedRepository.save(users);
    } else {
      LOG.info(
          "User: " + userDTO.getEmail() + " is already present in the table. No need to add him");

      throw new UserException(
          "User: "
              + userDTO.getOid()
              + " is not present in neither phd, committee or doctoral center tables!",
          401);
    }
  }

  @Override
  @Transactional(dontRollbackOn = UserException.class)
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
    String statement = "SELECT * FROM " + group + " WHERE oid = $1";
    UserEntity<?> userEntity = this.getEntityByGroup(group);

    UserEntity<?> result =
        this.pgClient
            .preparedQuery(statement.toString())
            .execute(Tuple.of(oid))
            .onItem()
            .transform(rowSet -> userEntity.toEntity(rowSet.iterator().next()))
            .await()
            .indefinitely();

    return result;
  }

  private UserEntity<?> getEntityByGroup(String group) {
    return switch (group) {
      case "phd" -> {
        yield new Phd();
      }
      case "committee" -> {
        yield new Committee();
      }
      case "doctoralCenter" -> {
        yield new DoctoralCenter();
      }
      case "unauthorizedUsers" -> {
        yield new UnauthorizedUsers();
      }
      default -> {
        throw new UserException("Error. strategy group doesn't exist: " + group);
      }
    };
  }
}
