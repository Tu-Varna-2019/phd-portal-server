package com.tuvarna.phd.service;

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
import com.tuvarna.phd.service.dto.LogDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

@ApplicationScoped
public final class LogServiceImpl implements LogService {

  @Inject private Logger LOG = Logger.getLogger(LogServiceImpl.class);
  @Inject private ElasticsearchClient client;

  @ConfigProperty(name = "elasticsearch.index-name")
  private String index;

  @Override
  // @CacheInvalidate(cacheName = "logs-cache")
  @Transactional
  public void save(LogDTO logDTO) {
    LOG.info("Service received a request to save a log: " + logDTO.toString());
    logDTO.setId(UUID.randomUUID().toString());

    this.index(logDTO);

    LOG.info("Log: " + logDTO.getTimestamp() + " saved to ElastiSearch!");
  }

  @Override
  // @CacheResult(cacheName = "logs-cache")
  @Transactional
  public List<LogDTO> get() {
    LOG.info("Service received a request to fetch logs for admin role");

    List<String> userCollection =
        new ArrayList<String>(Arrays.asList("phd", "committee", "manager", "expert", "admin"));

    List<LogDTO> userTraceLogs = new ArrayList<LogDTO>();
    for (String user : userCollection) userTraceLogs.addAll(this.searchByGroup(user));

    LOG.info("Logs retrieved!");
    return userTraceLogs;
  }

  @Override
  @Transactional
  public void delete(List<LogDTO> logs) {
    LOG.info("Service received a request to delete logs");
    this.deleteLogs(logs);

    LOG.info("Logs deleted!");
  }

  public void index(LogDTO logDTO) {

    try {
      IndexRequest<LogDTO> request =
          IndexRequest.of(b -> b.index(index).document(logDTO).id(logDTO.getId()));
      IndexResponse response = this.client.index(request);
      LOG.info("Response from creating an index: " + response.id());

    } catch (IOException exception) {
      LOG.error("Error in log indexing: " + exception.getMessage());
      throw new LogException("Error in log indexing!");
    }
  }

  private List<LogDTO> searchByGroup(String group) {
    return search("user.group", group);
  }

  private List<LogDTO> search(String term, String match) {
    try {
      SearchRequest searchRequest =
          SearchRequest.of(
              b ->
                  b.index(index)
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

  private void deleteLogs(List<LogDTO> logs) {
    try {
      BulkRequest.Builder br = new BulkRequest.Builder();

      for (var log : logs) br.operations(op -> op.delete(idx -> idx.index(index)));

      BulkResponse result = client.bulk(br.build());

      if (result.errors()) throw new LogException("The deleting operation encountered errors.");

    } catch (IOException exception) {
      throw new LogException("Error in deleting logs!");
    }
  }
}
