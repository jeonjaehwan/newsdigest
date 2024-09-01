package com.newsdigest.newsdigest.repository;

import com.newsdigest.newsdigest.entity.News;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsRepository extends JpaRepository<News, Long> {

    Page<News> findAllByOrderByPublishedAtDesc(Pageable pageable);
}
