package com.escobar.bmsapp.repository.search;

import com.escobar.bmsapp.domain.Trip;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Trip entity.
 */
public interface TripSearchRepository extends ElasticsearchRepository<Trip, Long> {
}
