package com.tuvarna.phd.service;

import com.tuvarna.phd.dto.BlobDataDTO;
import com.tuvarna.phd.dto.CandidateApplyDTO;
import com.tuvarna.phd.dto.CandidateDTO;
import com.tuvarna.phd.dto.CurriculumCreateDTO;
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
import com.tuvarna.phd.model.MailModel;
import com.tuvarna.phd.model.MailModel.TEMPLATES;
import com.tuvarna.phd.model.S3Model;
import com.tuvarna.phd.repository.CandidateRepository;
import com.tuvarna.phd.repository.CandidateStatusRepository;
import com.tuvarna.phd.repository.CurriculumRepository;
import com.tuvarna.phd.repository.FacultyRepository;
import com.tuvarna.phd.repository.ModeRepository;
import com.tuvarna.phd.repository.PhdRepository;
import com.tuvarna.phd.repository.SubjectRepository;
import io.quarkus.cache.CacheInvalidate;
import io.quarkus.cache.CacheResult;
import io.vertx.mutiny.sqlclient.Tuple;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.ServerErrorException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.jboss.logging.Logger;

@ApplicationScoped
public final class CandidateServiceImpl implements CandidateService {
  @Inject CandidateRepository candidateRepository;
  @Inject PhdRepository phdRepository;
  @Inject CurriculumRepository curriculumRepository;
  @Inject FacultyRepository facultyRepository;
  @Inject ModeRepository modeRepository;
  @Inject CandidateStatusRepository candidateStatusRepository;
  @Inject SubjectRepository subjectRepository;

  @Inject S3Model s3Model;
  @Inject MailModel mailModel;

  @Inject DatabaseModel databaseModel;
  @Inject IPBlockService ipBlockService;

  @Inject CandidateMapper candidateMapper;
  @Inject CurriculumMapper curriculumMapper;
  @Inject SubjectMapper subjectMapper;

  @Inject private Logger LOG = Logger.getLogger(CandidateServiceImpl.class);

  @Override
  @Transactional
  public void apply(CandidateApplyDTO candidateDTO) {
    LOG.info("Recevived a service request to register a new candidate: " + candidateDTO.toString());

    this.checkIfCandidateEmailIsPresent(candidateDTO.getEmail());

    Candidate candidate =
        this.candidateMapper
            .toEntity(candidateDTO)
            .setFaculty(this.facultyRepository.getByName(candidateDTO.getFaculty()))
            .setStatus(this.candidateStatusRepository.getByStatus(candidateDTO.getStatus()));

    this.registerCandidate(candidate, candidateDTO.getCurriculum());
    this.sendCandidateApplyEmails(candidateDTO.getEmail());
  }

  private void registerCandidate(Candidate candidate, CurriculumCreateDTO curriculumCreateDTO) {
    try {
      Curriculum curriculum = this.curriculumRepository.getByName(curriculumCreateDTO.getName());
      candidate.setCurriculum(curriculum);
    } catch (HttpException exception) {
      LOG.info("Curriculum doesn't exist. Creating now for candidate: " + candidate.getEmail());
      Curriculum curriculum = this.curriculumMapper.toEntity(curriculumCreateDTO);

      Mode mode = this.modeRepository.getByMode(curriculumCreateDTO.getMode());
      Set<Subject> subjects = new HashSet<Subject>();
      for (String subjectDTO : curriculumCreateDTO.getSubjects()) {
        subjects.add(this.subjectRepository.getByName(subjectDTO));
      }

      curriculum.setIsPublic(false).setMode(mode).setSubjects(subjects);
      this.curriculumRepository.save(curriculum);

      LOG.info("Curriculum created!");
      candidate.setCurriculum(curriculum);
    }

    this.candidateRepository.save(candidate);
    LOG.info("Candidate saved!");
  }

  private void checkIfCandidateEmailIsPresent(String candidateEmail) {
    Arrays.asList("phd", "candidate")
        .forEach(
            (user) -> {
              String statement = ("SELECT EXISTS (SELECT 1 FROM " + user + " WHERE email = $1)");
              Boolean isEmailFound =
                  this.databaseModel.selectIfExists(statement, Tuple.of(candidateEmail));

              if (isEmailFound) {
                LOG.error(
                    "Candidate email: " + candidateEmail + " already exists for table " + user);
                throw new ClientErrorException("Error, email already exists!", 400);
              }
            });

    if (this.ipBlockService.isClientIPBlocked()) {
      LOG.error("Error, client is ip blocked!");
      throw new ClientErrorException("Error, client is ip blocked!", 400);
    }

    LOG.info(
        "Good, candidate's email dosen't exist in the table neither he is ip blocked. Now"
            + " registering him...");
  }

  private void sendCandidateApplyEmails(String candidateEmail) {
    LOG.info("Now sending email for the doc centers to review the candidate's application...");

    this.databaseModel
        .selectMapString(
            "SELECT d.email FROM doctoral_center d JOIN doctoral_center_role dc ON(d.role ="
                + " dc.id) WHERE dc.role = 'manager' OR dc.role = 'admin'",
            "email")
        .forEach(
            email -> {
              try {
                this.mailModel.send(
                    "Кандидат " + candidateEmail,
                    TEMPLATES.CANDIDATE_APPLY,
                    email,
                    Map.of("$CANDIDATE", candidateEmail));
              } catch (IOException exception) {
                LOG.error("Error in sending email to the doc centers: " + exception);
                throw new ServerErrorException("Error in sending email to the doc centers!", 500);
              }
            });

    LOG.info("Now sending the confirmation email to the candidate...");
    try {
      this.mailModel.send(
          "Вашата кандидатура беше изпратена успешно!",
          TEMPLATES.CANDIDATE_APPLY_CONFIRMATION,
          candidateEmail);
    } catch (IOException exception) {
      LOG.error("Error in sending email to the candidate: " + exception);
      throw new ServerErrorException("Error in sending email to the candidate!", 500);
    }
  }

  @Override
  public void uploadBiography(BlobDataDTO file, String candidateName) {
    LOG.info(
        "Received a service request to upload a biography file: "
            + file.getFilename()
            + " with mimeType: "
            + file.getMimetype());

    this.s3Model.uploadBlob(
        "candidates/" + candidateName + "/biography/" + file.getFilename(), file);

    LOG.info("Saving file with name: " + file.getFilename());
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
