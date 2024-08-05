package com.newsdigest.newsdigest.repository;

import com.newsdigest.newsdigest.entity.Favorite;
import com.newsdigest.newsdigest.entity.News;
import com.newsdigest.newsdigest.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    Optional<Favorite> findByUserAndNews(User user, News news);

    Page<Favorite> findAllByUser(User user, Pageable pageable);
}
