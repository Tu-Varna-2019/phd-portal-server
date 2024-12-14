package com.tuvarna.phd.service.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.jboss.logging.Logger;

@ApplicationScoped
public class LogServiceImpl implements LogService {

  @Inject private Logger LOG = Logger.getLogger(LogServiceImpl.class);
  @Inject private ElasticsearchClient client;

  @Override
  @Transactional
  public void save(LogDTO logDTO) {
    LOG.info("Service received a request to save a log with timestamp" + logDTO.getTimestamp());
  }

  @Override
  @Transactional
  public List<LogDTO> fetch(String role) {
    return new ArrayList<>();
  }

  private void index(List<LogDTO> logs) {
    try {
      BulkRequest.Builder br = new BulkRequest.Builder();

      for (var log : logs) br.operations(op -> op.index(idx -> idx.index("logs").document(log)));

      BulkResponse result = client.bulk(br.build());

      if (result.errors()) throw new LogException("The indexing operation encountered errors.");

    } catch (IOException exception) {
      throw new LogException("Error in log indexing!");
    }
  }

  private List<LogDTO> searchByRole(String role) {
    return search("role", role);
  }

  private List<LogDTO> search(String term, String match) {
    try {
      SearchRequest searchRequest =
          SearchRequest.of(
              b ->
                  b.index("fruits")
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
}
