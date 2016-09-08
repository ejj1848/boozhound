package com.astontech.boozhound.repository.search;

import com.astontech.boozhound.domain.Distiller;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Distiller entity.
 */
public interface DistillerSearchRepository extends ElasticsearchRepository<Distiller, Long> {
}
