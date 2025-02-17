package com.tuvarna.phd.model;

import com.tuvarna.phd.entity.UserEntity;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.Tuple;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class DatabaseModel {
  @Inject PgPool client;

  public void execute(String statement, Tuple prepQueries) {
    this.client.preparedQuery(statement).execute(prepQueries).await().indefinitely();
  }

  public Boolean selectIfExists(String statement, Tuple prepQueries) {
    return this.client
        .preparedQuery(statement)
        .execute(prepQueries)
        .onItem()
        .transform(rowSet -> rowSet.iterator().next().getBoolean(0))
        .await()
        .indefinitely();
  }

  public String selectString(String statement, Tuple prepQueries, String rowString) {
    return this.client
        .preparedQuery(statement)
        .execute(prepQueries)
        .onItem()
        .transform(rowSet -> rowSet.iterator().next().getString(rowString))
        .await()
        .indefinitely();
  }

  public List<String> selectMapString(String statement, Tuple prepQueries, String rowString) {
    return this.client
        .preparedQuery(statement)
        .execute(prepQueries)
        .map(
            rowSet -> {
              List<String> rowsString = new ArrayList<String>();
              rowSet.forEach(row -> rowsString.add(row.getString(rowString)));
              return rowsString;
            })
        .await()
        .indefinitely();
  }

  public <T extends UserEntity<T>> List<T> selectMapEntity(
      String statement, Tuple prepQueries, UserEntity<T> userEntity) {

    return this.client
        .preparedQuery(statement)
        .execute(prepQueries)
        .map(
            rowSet -> {
              List<T> rows = new ArrayList<T>();
              rowSet.forEach(row -> rows.add(userEntity.toEntity(row)));

              return rows;
            })
        .await()
        .indefinitely();
  }
}
