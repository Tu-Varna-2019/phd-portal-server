package com.tuvarna.phd.service;

import com.tuvarna.phd.entity.Committee;

public sealed interface CommitteeService permits CommitteeServiceImpl {

  // void login(Committee committee);

  void delete(Committee committee);
}
