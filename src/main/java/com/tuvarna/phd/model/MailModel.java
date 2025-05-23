package com.tuvarna.phd.model;

import io.quarkus.mailer.Mail;
import io.quarkus.mailer.reactive.ReactiveMailer;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

@ApplicationScoped
public class MailModel {
  @Inject private ReactiveMailer mailer;

  public enum TEMPLATES {
    ACCEPTED("phd_accepted.html"),
    REJECTED("phd_rejected.html"),
    CREATE_USER("create_new_phd_user.html"),
    CANDIDATE_APPLY("candidate_apply.html"),
    CANDIDATE_APPLY_CONFIRMATION("candidate_apply_confirmation.html");

    private String path;

    TEMPLATES(String path) {
      this.path = "./templates/mail/" + path;
    }

    String getPath() {
      return this.path;
    }
  }

  public void send(
      String title, TEMPLATES template, String email, Map<String, String> replaceTemplateParams)
      throws IOException {
    String absolutePath = new File(template.getPath()).getCanonicalPath();
    StringBuilder body = new StringBuilder(Files.readString(Path.of(absolutePath)));

    replaceTemplateParams.forEach(
        (staticVal, dynamicVal) -> {
          body.replace(0, body.length(), body.toString().replace(staticVal, dynamicVal));
        });

    this.mailer
        .send(Mail.withHtml(email, title, body.toString()))
        .onItem()
        .transform(
            v -> {
              // NOTE: Not sure if I need that
              return ("Candidate email sent!");
            })
        .await()
        .indefinitely();
  }

  public void send(String title, TEMPLATES template, String email) throws IOException {
    String absolutePath = new File(template.getPath()).getCanonicalPath();
    String body = Files.readString(Path.of(absolutePath));

    this.mailer
        .send(Mail.withHtml(email, title, body.toString()))
        .onItem()
        .transform(
            v -> {
              // NOTE: Not sure if I need that
              return ("Candidate email sent!");
            })
        .await()
        .indefinitely();
  }
}
