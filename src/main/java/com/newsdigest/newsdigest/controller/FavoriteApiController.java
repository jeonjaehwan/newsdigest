package com.newsdigest.newsdigest.controller;

import com.newsdigest.newsdigest.dto.FavoriteSimpleResponse;
import com.newsdigest.newsdigest.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    /**
     * 저장한 뉴스 목록 조회
     */
    @GetMapping("/{userId}")
    public ResponseEntity<List<FavoriteSimpleResponse>> getFavoriteNews(@PathVariable("userId") Long userId,
                                                                        @RequestParam(name = "page", defaultValue = "0") int page,
                                                                        @RequestParam(name = "size", defaultValue = "10") int size) {
        List<FavoriteSimpleResponse> userFavorites = favoriteService.getUserFavorites(userId, page, size);

        return ResponseEntity.ok(userFavorites);
    }

}
