package com.tuvarna.phd.service;

import com.tuvarna.phd.entity.UnauthorizedUsers;
import com.tuvarna.phd.entity.UserEntity;
import com.tuvarna.phd.exception.UserException;
import com.tuvarna.phd.mapper.UnauthorizedUsersMapper;
import com.tuvarna.phd.repository.UnauthorizedUsersRepository;
import com.tuvarna.phd.repository.UserRepositoryStrategy;
import com.tuvarna.phd.service.dto.UnauthorizedUsersDTO;
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

  private final UserRepositoryStrategy userRepositoryStrategy;
  private final UnauthorizedUsersRepository usersRepository;
  private final UnauthorizedUsersMapper mapper;
  private final PgPool pgClient;

  @ConfigProperty(name = "user.groups")
  List<String> groups;

  @Inject private Logger LOG = Logger.getLogger(AuthServiceImpl.class);

  public AuthServiceImpl(
      PgPool pgClient,
      UserRepositoryStrategy userRepositoryStrategy,
      UnauthorizedUsersRepository usersRepository,
      UnauthorizedUsersMapper mapper) {

    this.userRepositoryStrategy = userRepositoryStrategy;
    this.pgClient = pgClient;
    this.usersRepository = usersRepository;
    this.mapper = mapper;
  }

  @Override
  public String getGroupByOid(String oid) {
    StringBuffer statement = new StringBuffer();

    for (String group : groups) {
      statement.append("SELECT COUNT(1) FROM " + group + " WHERE oid = $1");

      Boolean isUserFound =
          this.pgClient
              .preparedQuery(statement.toString())
              .execute(Tuple.of(oid))
              .onItem()
              .transform(rowSet -> rowSet.iterator().hasNext())
              .await()
              .indefinitely();

      if (isUserFound) {
        return group;
      }
    }

    return "";
  }

  @Override
  public void addToUnauthorized(UnauthorizedUsersDTO userDTO) {
    if (this.userRepositoryStrategy.getByOid(userDTO.getOid()) == null) {
      LOG.info(
          "User: "
              + userDTO.getEmail()
              + " is not present in any of the tables. Adding him now...");

      UnauthorizedUsers users = this.mapper.toEntity(userDTO);
      this.usersRepository.save(users);
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
  public Tuple2<UserEntity, String> login(UnauthorizedUsersDTO userDTO) {
    String group;
    LOG.info("Service received a request to login as a user: " + userDTO);

    if (!(group = this.getGroupByOid(userDTO.getOid())).isEmpty()) {
      return Tuple2.of(this.authenticate(userDTO), group);
    } else {
      this.addToUnauthorized(userDTO);
    }
    return null;
  }

  @Override
  public UserEntity authenticate(UnauthorizedUsersDTO uDto) {

    return null;
  }
}
