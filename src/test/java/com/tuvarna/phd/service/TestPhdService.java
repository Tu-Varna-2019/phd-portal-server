package com.tuvarna.phd.service.impl;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.tuvarna.phd.entity.Phd;
import com.tuvarna.phd.entity.StatusPhd;
import com.tuvarna.phd.mapper.PhdMapper;
import com.tuvarna.phd.repository.PhdRepository;
import com.tuvarna.phd.repository.StatusPhdRepository;
import com.tuvarna.phd.service.PhdService;
import com.tuvarna.phd.service.dto.PhdDTO;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class TestPhdService {
  @InjectMock PhdService phdService;
  @InjectMock PhdRepository pRepository;
  @InjectMock StatusPhdRepository sPhdRepository;
  @Inject public PhdMapper pMapper;
  @InjectMock public PhdMapper pMapperMock;

  static PhdDTO phdDTO;

  @BeforeAll
  static void init() {
    PhdDTO phdDTO = new PhdDTO("1111111", "John", "Johnatan", "Doe", "john.doe@mail.com");
  }

  @Test
  public void testRepo() {
    String defaultStatus = "enrolled";

    when(this.pMapperMock.toEntity(phdDTO)).thenReturn(new Phd());
    Phd phd = this.pMapper.toEntity(phdDTO);

    phd.setStatus(this.sPhdRepository.getByStatus(defaultStatus));
    when(this.sPhdRepository.getByStatus(defaultStatus)).thenReturn(new StatusPhd());

    this.pRepository.getByOid("1111111");
    when(this.pRepository.getByOid("1111111")).thenReturn(phd);

    this.pRepository.save(phd);
    doNothing().when(this.pRepository).save(phd);

    verify(this.pMapperMock).toEntity(phdDTO);
    verify(this.sPhdRepository).getByStatus(defaultStatus);
    verify(this.pRepository).getByOid("1111111");
    verify(this.pRepository).save(phd);
  }

  @Test
  public void testLogin() {
    this.phdService.login(phdDTO);

    doNothing().when(this.phdService).login(phdDTO);
    verify(this.phdService).login(phdDTO);
  }
}
