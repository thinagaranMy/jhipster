package com.escobar.bmsapp.repository.search;

import com.escobar.bmsapp.domain.Routes;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Routes entity.
 */
public interface RoutesSearchRepository extends ElasticsearchRepository<Routes, Long> {
}
