package com.tuvarna.phd.entity;

public sealed interface UserEntity permits Phd, Committee, DoctoralCenter, UnauthorizedUsers {}
