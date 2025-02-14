package com.tuvarna.phd.model;

import io.quarkus.mailer.Mail;
import io.quarkus.mailer.reactive.ReactiveMailer;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@ApplicationScoped
public class MailModel {
  @Inject private ReactiveMailer mailer;

  public enum TEMPLATES {
    ACCEPTED("phd_accepted.html"),
    REJECTED("phd_rejected.html"),
    CREATE_USER("create_new_phd_user.html");

    private String path;

    TEMPLATES(String path) {
      this.path = "./templates/" + path;
    }

    String getPath() {
      return this.path;
    }
  }

  public void send(String title, TEMPLATES template, String email) throws IOException {
    String absolutePath = new File(template.getPath()).getCanonicalPath();
    String body = Files.readString(Path.of(absolutePath));

    this.mailer
        .send(Mail.withHtml(email, title, body))
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
