package com.tuvarna.phd.service;

import java.util.List;

public sealed interface PhdService permits PhdServiceImpl {
  List<String> getCurriculum();

  void deleteCurriculum();
}
