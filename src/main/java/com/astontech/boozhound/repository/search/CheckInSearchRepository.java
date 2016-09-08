package com.astontech.boozhound.repository.search;

import com.astontech.boozhound.domain.CheckIn;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the CheckIn entity.
 */
public interface CheckInSearchRepository extends ElasticsearchRepository<CheckIn, Long> {
}
