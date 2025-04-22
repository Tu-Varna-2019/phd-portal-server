package com.tuvarna.phd.model;

import com.tuvarna.phd.entity.IEntity;
import com.tuvarna.phd.exception.HttpException;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.Tuple;
import io.vertx.pgclient.PgException;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionException;
import org.jboss.logging.Logger;

@Singleton
public class DatabaseModel {
  @Inject PgPool client;
  @Inject private Logger LOG = Logger.getLogger(DatabaseModel.class);

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

  public List<String> selectMapString(String statement, String rowString) {
    return this.client
        .preparedQuery(statement)
        .execute()
        .map(
            rowSet -> {
              List<String> rowsString = new ArrayList<String>();
              rowSet.forEach(row -> rowsString.add(row.getString(rowString)));
              return rowsString;
            })
        .await()
        .indefinitely();
  }

  public <T extends IEntity<T>> List<T> selectMapEntity(
      String statement, Optional<Tuple> prepQueries, IEntity<T> entity) {
    try {
      return this.client
          .preparedQuery(statement)
          .execute(prepQueries.orElse(Tuple.tuple()))
          .map(
              rowSet -> {
                List<T> rows = new ArrayList<T>();
                rowSet.forEach(row -> rows.add(entity.toEntity(row)));

                return rows;
              })
          .await()
          .indefinitely();

    } catch (CompletionException exception) {
      LOG.error("Error when executing this SQL statement: " + statement);
      throw new HttpException("Error in retrieving candidates. Please try again next time!", 500);
    } catch (PgException exception) {
      LOG.error(
          "Column doesn't exist for statement: "
              + statement
              + " exception: "
              + exception.getMessage());
      throw new HttpException("Requested columns doesn't exist in candidate!", 400);
    }
  }

  public <T extends IEntity<T>> List<T> selectMapEntity(String statement, IEntity<T> entity) {
    try {
      return this.client
          .preparedQuery(statement)
          .execute()
          .map(
              rowSet -> {
                List<T> rows = new ArrayList<T>();
                rowSet.forEach(row -> rows.add(entity.toEntity(row)));

                return rows;
              })
          .await()
          .indefinitely();

    } catch (CompletionException exception) {
      LOG.error("Error when executing this SQL statement: " + statement);
      throw new HttpException("Error in retrieving candidates. Please try again next time!", 500);
    } catch (PgException exception) {
      LOG.error(
          "Column doesn't exist for statement: "
              + statement
              + " exception: "
              + exception.getMessage());
      throw new HttpException("Requested columns doesn't exist in candidate!", 400);
    }
  }
}
