package com.escobar.bmsapp.repository.search;

import com.escobar.bmsapp.domain.UserRole;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the UserRole entity.
 */
public interface UserRoleSearchRepository extends ElasticsearchRepository<UserRole, Long> {
}
