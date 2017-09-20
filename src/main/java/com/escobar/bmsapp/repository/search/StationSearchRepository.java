package com.escobar.bmsapp.repository.search;

import com.escobar.bmsapp.domain.Station;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Station entity.
 */
public interface StationSearchRepository extends ElasticsearchRepository<Station, Long> {
}
