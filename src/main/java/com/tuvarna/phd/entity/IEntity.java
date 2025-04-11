package com.tuvarna.phd.entity;

import io.vertx.mutiny.sqlclient.Row;

public interface IEntity<T extends IEntity<T>> {
  T toEntity(Row row);
}
