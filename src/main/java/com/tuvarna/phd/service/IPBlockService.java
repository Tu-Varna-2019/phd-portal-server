package com.tuvarna.phd.service;

public sealed interface IPBlockService permits IPBlockServiceImpl {
  boolean isClientIPBlocked();
}
