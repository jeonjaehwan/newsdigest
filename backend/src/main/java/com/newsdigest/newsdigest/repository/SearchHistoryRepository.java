package com.newsdigest.newsdigest.repository;

import com.newsdigest.newsdigest.entity.SearchHistory;
import com.newsdigest.newsdigest.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SearchHistoryRepository extends JpaRepository<SearchHistory, Long> {

    List<SearchHistory> findByUserAndQuery(User user, String query);

    List<SearchHistory> findByUserOrderByCreatedDateAsc(User user);

    Page<SearchHistory> findByUserOrderByCreatedDateDesc(User user, Pageable pageable);
}
