package com.tuvarna.phd.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.tuvarna.phd.dto.CandidateApplyDTO;
import com.tuvarna.phd.dto.CurriculumCreateDTO;
import com.tuvarna.phd.entity.Candidate;
import com.tuvarna.phd.entity.Faculty;
import com.tuvarna.phd.mapper.CandidateMapper;
import com.tuvarna.phd.mapper.CurriculumMapper;
import com.tuvarna.phd.mapper.SubjectMapper;
import com.tuvarna.phd.model.DatabaseModel;
import com.tuvarna.phd.repository.CandidateRepository;
import com.tuvarna.phd.repository.CandidateStatusRepository;
import com.tuvarna.phd.repository.CurriculumRepository;
import com.tuvarna.phd.repository.FacultyRepository;
import com.tuvarna.phd.repository.ModeRepository;
import com.tuvarna.phd.repository.PhdRepository;
import com.tuvarna.phd.repository.SubjectRepository;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import java.util.Arrays;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class TestCandidateService {
  @Inject CandidateService candidateService;
  @Inject IPBlockService ipBlockService;

  @InjectMock PhdRepository pRepository;
  @InjectMock CurriculumRepository curriculumRepository;
  @InjectMock FacultyRepository facultyRepository;
  @InjectMock ModeRepository modeRepository;
  @InjectMock CandidateRepository candidateRepository;
  @InjectMock CandidateStatusRepository candidateStatusRepository;
  @InjectMock SubjectRepository subjectRepository;

  @InjectMock CandidateMapper candidateMapper;
  @InjectMock CurriculumMapper curriculumMapper;
  @InjectMock SubjectMapper subjectMapper;

  @InjectMock DatabaseModel databaseModel;

  @Test
  public void testApply() {
    CandidateApplyDTO candidateApplyDTO =
        new CandidateApplyDTO(
            "John",
            "john.doe@mail.com",
            "1111111",
            "Bulgaria",
            "Varna",
            "Primorski, 9002 Varna, Bulgaria",
            "biography113.jpg",
            2025L,
            9000L,
            "faculty1",
            "enrolled",
            new CurriculumCreateDTO(
                "Curriculum123",
                "regular",
                "faculty1",
                Arrays.asList("discipline1", "discipline2")));

    Candidate candidate = new Candidate();

    when(this.candidateMapper.toEntity(candidateApplyDTO)).thenReturn(candidate);
    when(this.facultyRepository.getByName("faculty1")).thenReturn(new Faculty(1L, "faculty"));

    doNothing()
        .when(this.candidateService)
        .checkIfCandidateEmailIsPresent(candidateApplyDTO.getEmail());

    when(databaseModel.selectIfExists(anyString(), any())).thenReturn(false);
    when(ipBlockService.isClientIPBlocked()).thenReturn(false);

    when(this.candidateStatusRepository.getByStatus(candidate.getStatus().getStatus()))
        .thenReturn(candidate.getStatus());
    when(this.candidateRepository.getByEmail("john.doe@mail.com")).thenReturn(candidate);

    candidateService.apply(candidateApplyDTO);
    verify(this.candidateRepository).save(candidate);
  }

  // @Test
  // public void testLogin() {
  //   this.candidateService.login(phdDTO);
  //
  //   doNothing().when(this.candidateService).login(phdDTO);
  //   verify(this.candidateService).login(phdDTO);
  // }
}
