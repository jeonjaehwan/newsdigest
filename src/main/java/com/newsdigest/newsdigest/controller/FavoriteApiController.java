package com.newsdigest.newsdigest.controller;

import com.newsdigest.newsdigest.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/favorite")
public class FavoriteApiController {

    private final FavoriteService favoriteService;

    /**
     * 뉴스 저장
     */
    @PostMapping("/{userId}/like/{newsId}")
    public ResponseEntity<Void> likeNews(@PathVariable("userId") Long userId,
                                         @PathVariable("newsId") Long newsId) {
        favoriteService.likeNews(userId, newsId);

        return ResponseEntity.noContent().build();
    }

    /**
     * 뉴스 저장 취소
     */
    @PostMapping("/{userId}/unlike/{newsId}")
    public ResponseEntity<Void> unlikeNews(@PathVariable("userId") Long userId,
                                           @PathVariable("newsId") Long newsId) {
        favoriteService.unlikeNews(userId, newsId);

        return ResponseEntity.noContent().build();
    }

}
