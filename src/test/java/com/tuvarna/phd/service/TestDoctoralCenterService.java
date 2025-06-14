package com.tuvarna.phd.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.tuvarna.phd.entity.Commission;
import com.tuvarna.phd.entity.Grade;
import com.tuvarna.phd.entity.Subject;
import com.tuvarna.phd.entity.Unauthorized;
import com.tuvarna.phd.mapper.CurriculumMapper;
import com.tuvarna.phd.model.MailModel;
import com.tuvarna.phd.model.MailModel.TEMPLATES;
import com.tuvarna.phd.repository.CommissionRepository;
import com.tuvarna.phd.repository.FacultyRepository;
import com.tuvarna.phd.repository.GradeRepository;
import com.tuvarna.phd.repository.UnauthorizedRepository;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import java.io.IOException;
import java.sql.Date;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

@QuarkusTest
public class TestDoctoralCenterService {
  @InjectMock IPBlockService ipBlockService;

  @InjectMock FacultyRepository facultyRepository;
  @Inject UnauthorizedRepository unauthorizedRepository;
  @Inject GradeRepository gradeRepository;
  @Inject CommissionRepository commissionRepository;

  @InjectMock CurriculumMapper curriculumMapper;
  @InjectMock MailModel mailModel;

  @BeforeAll
  public static void setup() {
    UnauthorizedRepository unauthorizedRepository = Mockito.mock(UnauthorizedRepository.class);
    GradeRepository gradeRepository = Mockito.mock(GradeRepository.class);
    CommissionRepository commissionRepository = Mockito.mock(CommissionRepository.class);

    Mockito.when(unauthorizedRepository.getByOid("0000")).thenReturn(new Unauthorized("0000"));
    Mockito.when(gradeRepository.getById(1L)).thenReturn(new Grade(1L));
    Mockito.when(commissionRepository.getById(1L)).thenReturn(new Commission(1L, "commission1"));

    QuarkusMock.installMockForType(unauthorizedRepository, UnauthorizedRepository.class);
    QuarkusMock.installMockForType(gradeRepository, GradeRepository.class);
    QuarkusMock.installMockForType(commissionRepository, CommissionRepository.class);
  }

  @Test
  public void testGetUnauthorizedUsers() {
    List<Unauthorized> unauthorized = List.of(new Unauthorized("0000"));

    when(this.unauthorizedRepository.getByOid("0000")).thenReturn(unauthorized.get(0));

    assertEquals("0000", unauthorized.get(0).getOid());
  }

  @Test
  public void testSetCommissionOnGrade() throws IOException {
    Long id = 1L;
    String name = "commission1";

    Commission commission = new Commission(1L, name);
    Grade grade =
        new Grade(
            id, 6.00, new Date(System.currentTimeMillis()), "report1", new Subject("subject1"));
    grade.setCommission(commission);

    when(this.gradeRepository.getById(1L)).thenReturn(grade);
    when(this.commissionRepository.getByName(name)).thenReturn(commission);

    assertEquals(1L, grade.getId());
    assertEquals("commission1", commission.getName());
    assertEquals(commission, grade.getCommission());

    doNothing()
        .when(this.mailModel)
        .send(
            "Вие сте добавен в изпит",
            TEMPLATES.COMMITTEE_ADDED_TO_EXAM,
            "john.doe@mail.com",
            Map.of("$EVAL_DATE", grade.getEvalDate().toString()));
  }
}
