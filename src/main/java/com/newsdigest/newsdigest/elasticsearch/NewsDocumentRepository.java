package com.newsdigest.newsdigest.elasticsearch;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewsDocumentRepository extends ElasticsearchRepository<NewsDocument, Long> {

    List<NewsDocument> findByQueryContainingIgnoreCase(String query);
}
