package com.tuvarna.phd.service;

import com.tuvarna.phd.dto.CommissionDTO;
import com.tuvarna.phd.dto.CommitteeDTO;
import com.tuvarna.phd.dto.CurriculumDTO;
import com.tuvarna.phd.dto.GradeDTO;
import com.tuvarna.phd.dto.SubjectDTO;
import com.tuvarna.phd.dto.UserDTO;
import com.tuvarna.phd.entity.Curriculum;
import com.tuvarna.phd.entity.Faculty;
import com.tuvarna.phd.entity.Subject;
import com.tuvarna.phd.mapper.CurriculumMapper;
import com.tuvarna.phd.mapper.PhdMapper;
import com.tuvarna.phd.mapper.SubjectMapper;
import com.tuvarna.phd.model.DatabaseModel;
import com.tuvarna.phd.repository.CurriculumRepository;
import com.tuvarna.phd.repository.FacultyRepository;
import com.tuvarna.phd.repository.GradeRepository;
import com.tuvarna.phd.repository.PhdRepository;
import com.tuvarna.phd.repository.PhdStatusRepository;
import com.tuvarna.phd.utils.GradeUtils;
import com.tuvarna.phd.utils.GradeUtils.EVAL_USER_TYPE;
import io.vertx.mutiny.sqlclient.Tuple;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.jboss.logging.Logger;

@ApplicationScoped
public final class PhdServiceImpl implements PhdService {

  @Inject PhdRepository pRepository;
  @Inject PhdStatusRepository sPhdRepository;
  @Inject CurriculumRepository curriculumRepository;
  @Inject FacultyRepository facultyRepository;
  @Inject GradeRepository gradeRepository;

  @Inject GradeUtils gradeUtils;

  @Inject PhdMapper pMapper;
  @Inject SubjectMapper subjectMapper;
  @Inject CurriculumMapper curriculumMapper;

  @Inject DatabaseModel databaseModel;

  @Inject private Logger LOG = Logger.getLogger(PhdServiceImpl.class);

  @Override
  // @CacheResult(cacheName = "curriculum-cache")
  public List<CurriculumDTO> getCurriculums() {
    LOG.info("Received a service request to retrieve all curriculums");
    List<Curriculum> curriculums = this.curriculumRepository.getAll();
    curriculums.stream().filter((curriculum) -> curriculum.getIsPublic().equals(true));

    List<CurriculumDTO> curriculumDTOs = new ArrayList<>();

    curriculums.forEach(
        (curriculum) -> curriculumDTOs.add(this.curriculumMapper.toDto(curriculum)));

    LOG.info("All curriculums retrieved: " + curriculums.toString());
    return curriculumDTOs;
  }

  @Override
  public List<SubjectDTO> getSubjectsByCurriculum(String curriculumName) {
    List<SubjectDTO> subjects = new ArrayList<>();

    LOG.info("Received a service request to retrieve all subjects by curriculum");
    this.curriculumRepository
        .getByName(curriculumName)
        .getSubjects()
        .forEach(
            (subject) -> {
              subjects.add(new SubjectDTO(subject.getName()));
            });

    LOG.info("All subjects retrieved!");
    return subjects;
  }

  @Override
  public List<SubjectDTO> getSubjectsByFaculty(String faculty) {
    /** Filter subjects by faculty name, that's retrieved from the teacher */
    List<SubjectDTO> subjectsDtos = new ArrayList<>();

    LOG.info("Received a service request to retrieve all subjects by faculty");

    List<Subject> subjects =
        this.databaseModel.getListEntity(
            "SELECT s.name FROM subject s JOIN"
                + " committee c ON(s.teacher=c.id) JOIN faculty f ON(c.faculty=f.id) WHERE"
                + " f.name = $1",
            Optional.of(Tuple.of(faculty)),
            new Subject());

    subjects.forEach((subject) -> subjectsDtos.add(this.subjectMapper.toDto(subject)));

    LOG.info("All subjects retrieved!");
    return subjectsDtos;
  }

  @Override
  // @CacheResult(cacheName = "faculty-cache")
  public List<Faculty> getFaculties() {
    LOG.info("Received a service request to retrieve all faculties");
    List<Faculty> faculties = this.facultyRepository.listAll();

    LOG.info("Faculties retrieved");
    return faculties;
  }

  @Override
  // @CacheResult(cacheName = "phd-exams-cache")
  public List<GradeDTO> getGrades(String oid) {
    LOG.info("Service received to retrieve all grades");

    Boolean isPhdEvaluated =
        this.databaseModel.getBoolean(
            "SELECT EXISTS (SELECT 1 FROM phd_grades WHERE phd_id = $1)", Tuple.of(oid));

    if (!isPhdEvaluated) {
      LOG.info("Didn't find any phd evals. Moving on...");
      return null;
    }

    List<GradeDTO> gradeDTOs = new ArrayList<>();
    this.gradeRepository
        .getAll()
        .forEach(
            grade -> {
              UserDTO userDTO =
                  this.gradeUtils.queryEvaluatedUsers(grade.getId(), EVAL_USER_TYPE.phd);

              // NOTE: Get exams for the signed in phd
              if (userDTO.getOid().equals(oid)) {
                List<CommitteeDTO> committeeDTOs = new ArrayList<>();
                grade
                    .getCommission()
                    .getMembers()
                    .forEach(
                        committee -> {
                          Double committeeGrade = null;
                          try {
                            committeeGrade =
                                this.databaseModel.getDouble(
                                    "SELECT grade FROM committee_grade WHERE"
                                        + " committee_id = $1 AND grade_id = $2",
                                    Tuple.of(committee.getId(), grade.getId()),
                                    "grade");
                          } catch (NoSuchElementException exception) {
                            LOG.warn(
                                "Committee: "
                                    + committee.getName()
                                    + " hasn't evaluated grade with id: "
                                    + grade.getId()
                                    + " yet!");
                          }

                          committeeDTOs.add(
                              new CommitteeDTO(
                                  committee.getOid(),
                                  committee.getName(),
                                  committee.getEmail(),
                                  committee.getPicture(),
                                  committeeGrade,
                                  committee.getFaculty().getName(),
                                  committee.getRole().getRole()));
                        });

                GradeDTO gradeDTO =
                    new GradeDTO(
                        grade.getId(),
                        grade.getGrade(),
                        grade.getEvalDate(),
                        new CommissionDTO(grade.getCommission().getName(), committeeDTOs),
                        grade.getReport(),
                        grade.getAttachments(),
                        userDTO,
                        grade.getSubject().getName());

                // NOTE: Cannot use gradeMapper because we need to get the commitete grade from the
                // committee_grade table
                // this.gradeMapper.toDto(grade, userDTO)
                gradeDTOs.add(gradeDTO);
              }
            });

    return gradeDTOs;
  }
}
