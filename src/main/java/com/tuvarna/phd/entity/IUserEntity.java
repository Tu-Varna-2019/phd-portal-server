package com.tuvarna.phd.entity;

import io.vertx.mutiny.sqlclient.Row;

public sealed interface IUserEntity<T extends IUserEntity<T>>
    permits Phd, Committee, DoctoralCenter, UnauthorizedUsers, Candidate, Supervisor {

  T toEntity(Row row);
}
