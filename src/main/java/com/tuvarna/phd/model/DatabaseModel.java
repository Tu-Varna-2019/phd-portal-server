package com.tuvarna.phd.model;

import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.Tuple;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class DatabaseModel {
  @Inject PgPool client;

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

  public void execute(String statement, Tuple prepQueries) {
    this.client.preparedQuery(statement).execute(prepQueries).await().indefinitely();
  }
}
