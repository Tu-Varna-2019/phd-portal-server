package com.tuvarna.phd.model;

import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.Tuple;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

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
}
