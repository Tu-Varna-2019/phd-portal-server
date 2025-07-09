package com.tuvarna.phd.utils;

import com.tuvarna.phd.entity.Grade;
import com.tuvarna.phd.entity.Mode;
import com.tuvarna.phd.entity.Phd;
import com.tuvarna.phd.entity.Report;
import com.tuvarna.phd.entity.Subject;
import com.tuvarna.phd.model.DatabaseModel;
import com.tuvarna.phd.repository.GradeRepository;
import com.tuvarna.phd.repository.PhdRepository;
import com.tuvarna.phd.repository.ReportRepository;
import com.tuvarna.phd.repository.SubjectRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import org.jboss.logging.Logger;

@ApplicationScoped
public class PhdUtils {
  @Inject ReportRepository reportRepository;
  @Inject PhdRepository phdRepository;
  @Inject GradeRepository gradeRepository;
  @Inject SubjectRepository subjectRepository;

  @Inject DatabaseModel databaseModel;
  @Inject private Logger LOG = Logger.getLogger(GradeUtils.class);

  public void generateReport(Phd phd) {
    Integer MONTHLY_REPORT_DELAY = 3;

    Set<Report> reports = new HashSet<>();
    Date currentDate = new Date();
    currentDate.setMonth(currentDate.getMonth() + Report.TIME_MONTH_DELAY_CANDIDATE_APPROVAL);
    phd.setEnrollDate(new java.sql.Date(currentDate.getTime()));

    for (Integer year = 0; year < phd.getCurriculum().getMode().getYearPeriod(); year++) {
      LOG.info("Now generating the report for year: " + year);
      for (Integer month = 0; month < 9; month += 3) {
        // TODO: Verify how to generate the order number
        // Currently it's unknown to me
        Date monthDate = new Date();
        monthDate.setTime(currentDate.getTime());
        monthDate.setMonth(month + MONTHLY_REPORT_DELAY);
        monthDate.setYear(currentDate.getYear() + year);

        Report report =
            new Report(
                "Индивидуален тримесечен учебен план за подготовка за докоторант",
                Mode.modeBGtoEN.get(phd.getCurriculum().getMode().getMode()),
                monthDate,
                month + 1);
        this.reportRepository.save(report);
        reports.add(report);
        LOG.info("Now generating the report for month: " + month);
      }

      Date yearDate = new Date();
      yearDate.setTime(currentDate.getTime());
      yearDate.setMonth(11);
      yearDate.setYear(currentDate.getYear() + year);

      Report report =
          new Report(
              "Индивидуален годишен учебен план за подготовка за докоторант",
              Mode.modeBGtoEN.get(phd.getCurriculum().getMode().getMode()),
              yearDate,
              4);
      this.reportRepository.save(report);
      reports.add(report);
    }

    phd.setReports(reports);
    LOG.info("Phd report created successfully!");
  }

  public void generateExams(Phd phd) {
    Integer END_OF_SEMESTER_EXAM_MONTH_DELAY = 3;

    Set<Grade> grades = new HashSet<>();
    Set<Subject> subjects = phd.getCurriculum().getSubjects();
    Integer highestCourse = Collections.max(subjects).getCourse();

    Date currentDate = new Date();
    currentDate.setMonth(currentDate.getMonth());

    for (Integer course = 0; course < highestCourse; course++) {
      final int finalCourse = course;
      LOG.info("Now generating exams for course: " + course);

      Set<Subject> subjectsPerCourse =
          subjects.stream()
              .filter(subject -> subject.getCourse() == finalCourse)
              .collect(Collectors.toSet());

      for (Integer semester = 0; semester < 2; semester++) {
        final int finalSemester = semester;
        Set<Subject> subjectsPerSemester =
            subjectsPerCourse.stream()
                .filter(subject -> subject.getSemester() == finalSemester)
                .collect(Collectors.toSet());

        for (Subject subject : subjectsPerSemester) {
          Date monthDate = new Date();
          monthDate.setTime(currentDate.getTime());
          monthDate.setMonth(END_OF_SEMESTER_EXAM_MONTH_DELAY);
          monthDate.setYear(currentDate.getYear() + course);

          Grade grade =
              new Grade(
                  "Изпит по: " + subject.getName(),
                  new java.sql.Date(monthDate.getTime()),
                  this.subjectRepository.getById(subject.getId()));

          this.gradeRepository.save(grade);
          grades.add(grade);
          LOG.info("Now generating the exam for semester: " + semester);
        }
      }
    }

    phd.setGrades(grades);
    LOG.info("Phd grade created successfully!");
  }
}
