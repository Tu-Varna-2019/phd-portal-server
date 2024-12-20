package com.tuvarna.phd.service.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.HitsMetadata;
import com.tuvarna.phd.exception.LogException;
import com.tuvarna.phd.service.LogService;
import com.tuvarna.phd.service.dto.LogDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import org.jboss.logging.Logger;

@ApplicationScoped
public class LogServiceImpl implements LogService {

  @Inject private Logger LOG = Logger.getLogger(LogServiceImpl.class);
  @Inject private ElasticsearchClient client;
  private final String INDEX = "logs";

  @Override
  @Transactional
  public void save(LogDTO logDTO) {
    LOG.info("Service received a request to save a log with timestamp" + logDTO.getTimestamp());
    this.index(logDTO);
    LOG.info("Log: " + logDTO.getTimestamp() + " saved to ElastiSearch!");
  }

  @Override
  @Transactional
  public List<LogDTO> fetch(String role) {
    LOG.info("Service received a request to fetch logs for role: " + role);

    // NOTE: fetch user logs if the role is manager, fetch user and manager logs for expert
    List<LogDTO> logs = this.searchByRole("user");
    if ("expert".equals(role)) logs.addAll(this.searchByRole("manager"));

    LOG.info("Logs retrieved!");

    return logs;
  }

  @Override
  @Transactional
  public void deleteLogs(List<LogDTO> logs) {
    LOG.info("Service received a request to delete logs");
    this.delete(logs);

    LOG.info("Logs deleted!");
  }

  public void index(LogDTO logDTO) {
    try {
      IndexRequest<LogDTO> request = IndexRequest.of(b -> b.index(INDEX).document(logDTO));
      IndexResponse response = this.client.index(request);
      LOG.info("Response from creating an index: " + response.id());

    } catch (IOException exception) {
      throw new LogException("Error in log indexing!");
    }
  }

  private void index(List<LogDTO> logs) {
    try {
      BulkRequest.Builder br = new BulkRequest.Builder();

      for (var log : logs) br.operations(op -> op.index(idx -> idx.index(INDEX).document(log)));

      BulkResponse result = client.bulk(br.build());

      if (result.errors()) throw new LogException("The indexing operation encountered errors.");

    } catch (IOException exception) {
      throw new LogException("Error in log indexing!");
    }
  }

  private List<LogDTO> searchByRole(String role) {
    return search("userPrincipal.role", role);
  }

  private List<LogDTO> search(String term, String match) {
    try {
      SearchRequest searchRequest =
          SearchRequest.of(
              b ->
                  b.index(INDEX)
                      .query(
                          QueryBuilders.match()
                              .field(term)
                              .query(FieldValue.of(match))
                              .build()
                              ._toQuery()));

      SearchResponse<LogDTO> searchResponse = client.search(searchRequest, LogDTO.class);
      HitsMetadata<LogDTO> hits = searchResponse.hits();
      return hits.hits().stream().map(hit -> hit.source()).collect(Collectors.toList());
    } catch (IOException exception) {
      throw new LogException("Error in log searching!");
    }
  }

  private void delete(List<LogDTO> logs) {

    try {
      BulkRequest.Builder br = new BulkRequest.Builder();

      for (var log : logs) br.operations(op -> op.delete(idx -> idx.index(INDEX)));

      BulkResponse result = client.bulk(br.build());

      if (result.errors()) throw new LogException("The deleting operation encountered errors.");

    } catch (IOException exception) {
      throw new LogException("Error in deleting logs!");
    }
  }
}
