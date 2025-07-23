package com.tuvarna.phd.repository;

import com.tuvarna.phd.entity.CommitteeTitle;
import com.tuvarna.phd.exception.HttpException;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CommitteeTitleRepository implements PanacheRepositoryBase<CommitteeTitle, Integer> {

  public CommitteeTitle getByTitle(String title) {
    return find("title", title)
        .firstResultOptional()
        .orElseThrow(() -> new HttpException("Committee title: " + title + " doesn't exist!", 404));
  }
}
