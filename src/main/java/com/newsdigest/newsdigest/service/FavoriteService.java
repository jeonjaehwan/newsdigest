package com.newsdigest.newsdigest.service;

import com.newsdigest.newsdigest.dto.FavoriteSimpleResponse;
import com.newsdigest.newsdigest.entity.Favorite;
import com.newsdigest.newsdigest.entity.News;
import com.newsdigest.newsdigest.entity.User;
import com.newsdigest.newsdigest.exception.NewsNotFoundException;
import com.newsdigest.newsdigest.exception.UserNotFoundException;
import com.newsdigest.newsdigest.repository.FavoriteRepository;
import com.newsdigest.newsdigest.repository.NewsRepository;
import com.newsdigest.newsdigest.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final NewsRepository newsRepository;
    private final UserRepository userRepository;

    @Transactional
    public void likeNews(Long userId, Long newsId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        News news = newsRepository.findById(newsId)
                .orElseThrow(() -> new NewsNotFoundException(newsId));

        Favorite favorite = Favorite.from(user, news);

        favoriteRepository.save(favorite);

        log.info("User {} liked news {}", userId, newsId);
    }


    @Transactional
    public void unlikeNews(Long userId, Long newsId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        News news = newsRepository.findById(newsId)
                .orElseThrow(() -> new NewsNotFoundException(newsId));

        Favorite favorite = favoriteRepository.findByUserAndNews(user, news)
                .orElseThrow(() -> new NewsNotFoundException(newsId));

        favoriteRepository.delete(favorite);

        log.info("User {} unliked news {}", userId, newsId);
    }

    public List<FavoriteSimpleResponse> getUserFavorites(Long userId, int page, int size) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Favorite> favoritePage = favoriteRepository.findAllByUser(user, pageRequest);

        return favoritePage.stream()
                .map(FavoriteSimpleResponse::from)
                .collect(Collectors.toList());
    }

}
