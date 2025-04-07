package com.tuvarna.phd.service;

import com.tuvarna.phd.dto.CandidateDTO;
import com.tuvarna.phd.dto.CurriculumDTO;
import com.tuvarna.phd.dto.SubjectDTO;
import com.tuvarna.phd.entity.Candidate;
import com.tuvarna.phd.entity.Curriculum;
import com.tuvarna.phd.entity.Faculty;
import com.tuvarna.phd.entity.Mode;
import com.tuvarna.phd.entity.Subject;
import com.tuvarna.phd.exception.HttpException;
import com.tuvarna.phd.mapper.CandidateMapper;
import com.tuvarna.phd.mapper.CurriculumMapper;
import com.tuvarna.phd.mapper.SubjectMapper;
import com.tuvarna.phd.model.DatabaseModel;
import com.tuvarna.phd.repository.CandidateRepository;
import com.tuvarna.phd.repository.CandidateStatusRepository;
import com.tuvarna.phd.repository.CurriculumRepository;
import com.tuvarna.phd.repository.FacultyRepository;
import com.tuvarna.phd.repository.ModeRepository;
import com.tuvarna.phd.repository.SubjectRepository;
import io.quarkus.cache.CacheInvalidate;
import io.quarkus.cache.CacheResult;
import io.vertx.mutiny.sqlclient.Tuple;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.jboss.logging.Logger;

@ApplicationScoped
public final class CandidateServiceImpl implements CandidateService {
  @Inject CandidateRepository candidateRepository;
  @Inject CurriculumRepository curriculumRepository;
  @Inject FacultyRepository facultyRepository;
  @Inject ModeRepository modeRepository;
  @Inject CandidateStatusRepository candidateStatusRepository;
  @Inject SubjectRepository subjectRepository;

  @Inject DatabaseModel databaseModel;
  @Inject IPBlockService ipBlockService;

  @Inject CandidateMapper candidateMapper;
  @Inject CurriculumMapper curriculumMapper;
  @Inject SubjectMapper subjectMapper;

  @Inject private Logger LOG = Logger.getLogger(CandidateServiceImpl.class);

  @Override
  public void apply(CandidateDTO candidateDTO) {
    // TODO: create global notification to inform all doc center manager/expert about the new
    // candidate!
    LOG.info("Recevived a service request to register a new candidate: " + candidateDTO.toString());

    if (this.candidateRepository.getByEmail(candidateDTO.getEmail()) != null) {
      LOG.error("Candidate email: " + candidateDTO.getEmail() + " aleady exists!");
      throw new HttpException("Error, email already exists!");
    }

    if (this.ipBlockService.isClientIPBlocked()) {
      throw new HttpException("Error, client is ip blocked!", 401);
    }

    LOG.info(
        "Good, candidate's email dosen't exist in the table neither he is ip blocked. Now"
            + " registering him...");

    Candidate candidate = this.candidateMapper.toEntity(candidateDTO);

    Mode modeFound = this.modeRepository.getByMode(candidateDTO.getMode());
    candidate.setCurriculum(
        curriculumRepository.getByNameAndModeId(candidateDTO.getCurriculum(), modeFound.getId()));

    this.candidateRepository.save(candidate);
    LOG.info("Candidate saved!");
  }

  @Override
  @Transactional
  @CacheInvalidate(cacheName = "curriculum-cache")
  public void createCurriculum(CurriculumDTO curriculumDTO) {
    Boolean doesCurriculumNameExist =
        this.databaseModel.selectIfExists(
            "SELECT EXISTS (SELECT FROM curriculum WHERE name = $1)",
            Tuple.of(curriculumDTO.getName()));
    if (doesCurriculumNameExist) throw new HttpException("Curriculum name already exists!");

    LOG.info(
        "Received a service request to create a curriculum with name: " + curriculumDTO.getName());

    Curriculum curriculum = this.curriculumMapper.toEntity(curriculumDTO);
    curriculum.setMode(this.modeRepository.getByMode(curriculumDTO.getMode()));
    curriculum.setFaculty(this.facultyRepository.getByName(curriculumDTO.getFaculty()));

    curriculum.setSubjects(
        curriculumDTO.getSubjects().stream()
            .map((subject) -> this.subjectRepository.getByName(subject))
            .collect(Collectors.toSet()));

    this.curriculumRepository.save(curriculum);

    LOG.info("Curriculum saved!");
  }

  @Override
  @CacheResult(cacheName = "curriculum-cache")
  public List<CurriculumDTO> getCurriculums() {
    LOG.info("Received a service request to retrieve all curriculums");
    List<Curriculum> curriculums = this.curriculumRepository.getAll();
    List<CurriculumDTO> curriculumDTOs = new ArrayList<>();

    curriculums.forEach(
        (curriculum) -> curriculumDTOs.add(this.curriculumMapper.toDto(curriculum)));

    LOG.info("All curriculums retrieved!");
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
        this.databaseModel.selectMapEntity(
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
  @CacheResult(cacheName = "candidate-approved-status-cache")
  public List<CandidateDTO> getContests() {
    LOG.info(
        "Received a service request to retrieve all constests for accepted candidates into phd");

    List<CandidateDTO> candidateDTOs = new ArrayList<>();
    List<Candidate> candidates =
        this.databaseModel.selectMapEntity(
            "SELECT c.name, c.year_accepted, f.name AS facultyName FROM candidate c JOIN"
                + " candidate_status cs ON(c.status=cs.id) JOIN faculty f ON(c.faculty=f.id) WHERE"
                + " cs.status = 'accepted'",
            new Candidate());

    candidates.forEach(candidate -> candidateDTOs.add(this.candidateMapper.toDto(candidate)));

    LOG.info("All contests retrieved!");
    return candidateDTOs;
  }

  @Override
  @CacheResult(cacheName = "candidate-reviewing-status-cache")
  public List<CandidateDTO> getCandidatesInReview() {
    LOG.info("Received a service request to retrieve all candidates that are currently in review");

    List<CandidateDTO> candidateDTOs = new ArrayList<>();
    List<Candidate> candidates =
        this.databaseModel.selectMapEntity(
            "SELECT c.name, f.name AS facultyName FROM candidate c JOIN"
                + " candidate_status cs ON(c.status=cs.id) JOIN faculty f ON(c.faculty=f.id) WHERE"
                + " cs.status = 'reviewing'",
            new Candidate());

    candidates.forEach(candidate -> candidateDTOs.add(this.candidateMapper.toDto(candidate)));

    LOG.info("All candidates in review retrieved!");
    return candidateDTOs;
  }

  @Override
  @CacheResult(cacheName = "faculty-cache")
  public List<Faculty> getFaculties() {
    LOG.info("Received a service request to retrieve all faculties");
    List<Faculty> faculties = this.facultyRepository.listAll();

    LOG.info("Faculties retrieved");
    return faculties;
  }
}
