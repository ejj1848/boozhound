package com.astontech.boozhound.repository.search;

import com.astontech.boozhound.domain.Person;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Person entity.
 */
public interface PersonSearchRepository extends ElasticsearchRepository<Person, Long> {
}
