package com.tuvarna.phd.utils;

import com.tuvarna.phd.dto.UserDTO;
import com.tuvarna.phd.entity.Candidate;
import com.tuvarna.phd.entity.Phd;
import com.tuvarna.phd.exception.HttpException;
import com.tuvarna.phd.model.DatabaseModel;
import io.vertx.mutiny.sqlclient.Tuple;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.jboss.logging.Logger;

@ApplicationScoped
public class GradeUtils {
  @Inject DatabaseModel databaseModel;
  @Inject private Logger LOG = Logger.getLogger(GradeUtils.class);

  public static enum EVAL_USER_TYPE {
    phd,
    candidate,
    phd_candidate
  }

  private List<String> getUserGradeTable(EVAL_USER_TYPE eOptional) {

    return switch (eOptional) {
      case EVAL_USER_TYPE.phd -> List.of("phd_grades");
      case EVAL_USER_TYPE.candidate -> List.of("candidate_grades");
      case EVAL_USER_TYPE.phd_candidate -> List.of("phd_grades", "candidate_grades");
    };
  }

  private List<String> getUserColumnIds(EVAL_USER_TYPE eOptional) {

    return switch (eOptional) {
      case EVAL_USER_TYPE.phd -> List.of("phd_id");
      case EVAL_USER_TYPE.candidate -> List.of("candidate_id");
      case EVAL_USER_TYPE.phd_candidate -> List.of("phd_id", "candidate_id");
    };
  }

  public UserDTO queryEvaluatedUsers(Long gradeId, EVAL_USER_TYPE evalUserType) {
    List<String> evaluatedUserTypes = getUserGradeTable(evalUserType);
    List<String> evaluatedUserTypeIDs = getUserColumnIds(evalUserType);
    List<UserDTO> userDto = new ArrayList<>();

    evaluatedUserTypes.forEach(
        (userType) -> {
          String columnName = evaluatedUserTypeIDs.get(evaluatedUserTypes.indexOf(userType));
          Long columnId;

          try {
            columnId =
                this.databaseModel.getLong(
                    "SELECT " + columnName + " FROM " + userType + " WHERE grade_id = $1",
                    Tuple.of(gradeId),
                    columnName);
          } catch (NoSuchElementException exception) {
            columnId = -1L;
          }

          if (columnId != -1L) {
            LOG.info(
                "Good. Evaluated user: " + userType + " is found to grade id: " + gradeId + " !");

            if (userType.equals("phd_grades")) {
              Phd phd =
                  this.databaseModel
                      .getListEntity(
                          "SELECT oid, name, email, pin FROM phd WHERE id = $1",
                          Optional.of(Tuple.of(columnId)),
                          new Phd())
                      .get(0);
              userDto.add(
                  new UserDTO(phd.getOid(), phd.getName(), phd.getEmail(), phd.getPin(), "phd"));

            } else if (userType.equals("candidate_grades")) {
              Candidate candidate =
                  this.databaseModel
                      .getListEntity(
                          "SELECT name, email, pin FROM candidate WHERE id = $1",
                          Optional.of(Tuple.of(columnId)),
                          new Candidate())
                      .get(0);
              userDto.add(
                  new UserDTO(
                      candidate.getName(), candidate.getEmail(), candidate.getPin(), "candidate"));
            }
          }
        });

    if (userDto.isEmpty()) {
      // NOTE: throw an error if it hasn't found the user in ANY of the tables
      if (evalUserType == EVAL_USER_TYPE.phd_candidate) {
        LOG.error("Grade id: " + gradeId + " not found in " + evalUserType);
        throw new HttpException(
            "Grade id: "
                + gradeId
                + " not found in any of the tables: "
                + EVAL_USER_TYPE.phd_candidate,
            500);
      } else {
        LOG.warn("Grade id: " + gradeId + " not found in " + evalUserType);
        return null;
      }
    }

    return userDto.get(0);
  }
}
