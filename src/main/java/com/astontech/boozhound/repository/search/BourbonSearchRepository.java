package com.astontech.boozhound.repository.search;

import com.astontech.boozhound.domain.Bourbon;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Bourbon entity.
 */
public interface BourbonSearchRepository extends ElasticsearchRepository<Bourbon, Long> {
}
