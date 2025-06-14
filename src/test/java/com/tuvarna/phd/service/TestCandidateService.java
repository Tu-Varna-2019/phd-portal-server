package com.tuvarna.phd.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import com.tuvarna.phd.dto.CandidateApplyDTO;
import com.tuvarna.phd.dto.CurriculumCreateDTO;
import com.tuvarna.phd.dto.CurriculumDTO;
import com.tuvarna.phd.entity.Candidate;
import com.tuvarna.phd.entity.CandidateStatus;
import com.tuvarna.phd.entity.Curriculum;
import com.tuvarna.phd.entity.Faculty;
import com.tuvarna.phd.entity.Mode;
import com.tuvarna.phd.entity.Subject;
import com.tuvarna.phd.mapper.CandidateMapper;
import com.tuvarna.phd.mapper.CurriculumMapper;
import com.tuvarna.phd.repository.CandidateRepository;
import com.tuvarna.phd.repository.CandidateStatusRepository;
import com.tuvarna.phd.repository.CurriculumRepository;
import com.tuvarna.phd.repository.FacultyRepository;
import com.tuvarna.phd.repository.ModeRepository;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import java.util.Arrays;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

@QuarkusTest
public class TestCandidateService {
  @InjectMock IPBlockService ipBlockService;

  @InjectMock FacultyRepository facultyRepository;
  @Inject CandidateRepository candidateRepository;
  @Inject CurriculumRepository curriculumRepository;
  @Inject ModeRepository modeRepository;
  @Inject CandidateStatusRepository candidateStatusRepository;

  @InjectMock CandidateMapper candidateMapper;
  @InjectMock CurriculumMapper curriculumMapper;

  @BeforeAll
  public static void setup() {
    CandidateStatusRepository candidateStatusRepository2 =
        Mockito.mock(CandidateStatusRepository.class);
    CandidateRepository candidateRepository2 = Mockito.mock(CandidateRepository.class);
    ModeRepository modeRepository2 = Mockito.mock(ModeRepository.class);
    CurriculumRepository curriculumRepository2 = Mockito.mock(CurriculumRepository.class);

    Mockito.when(candidateStatusRepository2.getByStatus("enrolled"))
        .thenReturn(new CandidateStatus(null, "enrolled"));
    Mockito.when(curriculumRepository2.getByName("curriculum1"))
        .thenReturn(new Curriculum("curriculum1"));
    Mockito.when(candidateRepository2.getByEmail("john.doe@mail.com"))
        .thenReturn(new Candidate("john.doe@mail.com"));
    Mockito.when(modeRepository2.getByMode("regular")).thenReturn(new Mode("regular"));

    QuarkusMock.installMockForType(candidateStatusRepository2, CandidateStatusRepository.class);
    QuarkusMock.installMockForType(curriculumRepository2, CurriculumRepository.class);
    QuarkusMock.installMockForType(candidateRepository2, CandidateRepository.class);
    QuarkusMock.installMockForType(modeRepository2, ModeRepository.class);
  }

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
    CandidateStatus candidateStatus = new CandidateStatus(1L, "enrolled");
    candidate.setStatus(candidateStatus);

    when(this.candidateMapper.toEntity(candidateApplyDTO)).thenReturn(candidate);
    when(this.facultyRepository.getByName("faculty1")).thenReturn(new Faculty(1L, "faculty"));

    when(ipBlockService.isClientIPBlocked()).thenReturn(false);

    when(this.candidateStatusRepository.getByStatus("enrolled")).thenReturn(candidateStatus);
    when(this.candidateRepository.getByEmail("john.doe@mail.com")).thenReturn(candidate);

    assertEquals("enrolled", candidate.getStatus().getStatus());
  }

  @Test
  public void testCreateCurriculum() {
    CurriculumDTO curriculumDTO =
        new CurriculumDTO(
            "curriculum1",
            "regular",
            "2025",
            "faculty1",
            Arrays.asList("discipline1", "discipline2"));

    Curriculum curriculum =
        new Curriculum(
            "curriculum1",
            new Mode("enrolled"),
            Set.of(new Subject("subject1"), new Subject("subject2")));

    when(this.curriculumMapper.toEntity(curriculumDTO)).thenReturn(curriculum);

    curriculum.setMode(this.modeRepository.getByMode(curriculumDTO.getMode()));
    curriculum.setFaculty(this.facultyRepository.getByName(curriculumDTO.getFaculty()));

    this.curriculumRepository.save(curriculum);
  }
}
