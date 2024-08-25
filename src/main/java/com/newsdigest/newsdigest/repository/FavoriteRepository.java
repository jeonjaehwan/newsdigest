package com.newsdigest.newsdigest.repository;

import com.newsdigest.newsdigest.entity.Favorite;
import com.newsdigest.newsdigest.entity.News;
import com.newsdigest.newsdigest.entity.User;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    Optional<Favorite> findByUserAndNews(User user, News news);

    @Query("SELECT f FROM Favorite f JOIN FETCH f.news WHERE f.user = :user")
    Page<Favorite> findAllByUserWithNews(@Param("user") User user, Pageable pageable);
}

