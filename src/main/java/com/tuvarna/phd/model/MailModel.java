package com.tuvarna.phd.model;

import io.quarkus.mailer.Mail;
import io.quarkus.mailer.reactive.ReactiveMailer;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class MailModel {
  @Inject private ReactiveMailer mailer;

  public enum TEMPLATES {
    GREETINGS
  }

  public void send(String title, TEMPLATES template, String email) {
    String body = "";

    this.mailer
        .send(Mail.withHtml(email, "Добре дошли в Технически университет Варна!", body))
        .onItem()
        .transform(
            v -> {
              // NOTE: Not sure if I need that
              return ("Candidate email sent!");
            });
  }
}
