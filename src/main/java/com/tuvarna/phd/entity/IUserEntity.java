package com.tuvarna.phd.entity;

public sealed interface IUserEntity<T extends IUserEntity<T>> extends IEntity<T>
    permits Phd, Committee, DoctoralCenter, Unauthorized, Candidate, Supervisor {}
