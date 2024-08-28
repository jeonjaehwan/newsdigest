package com.newsdigest.newsdigest.controller;

import com.newsdigest.newsdigest.dto.FavoriteSimpleResponse;
import com.newsdigest.newsdigest.security.CustomUserDetails;
import com.newsdigest.newsdigest.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/favorites")
public class FavoriteApiController {

    private final FavoriteService favoriteService;

    /**
     * 뉴스 저장
     */
    @PostMapping("/{news-id}")
    public ResponseEntity<Void> likeNews(@AuthenticationPrincipal CustomUserDetails userDetails,
                                         @PathVariable("news-id") Long newsId) {
        favoriteService.likeNews(userDetails.getId(), newsId);

        return ResponseEntity.noContent().build();
    }

    /**
     * 뉴스 저장 취소
     */
    @DeleteMapping("/{news-id}")
    public ResponseEntity<Void> unlikeNews(@AuthenticationPrincipal CustomUserDetails userDetails,
                                           @PathVariable("news-id") Long newsId) {
        favoriteService.unlikeNews(userDetails.getId(), newsId);

        return ResponseEntity.noContent().build();
    }

    /**
     * 저장한 뉴스 목록 조회
     */
    @GetMapping
    public ResponseEntity<List<FavoriteSimpleResponse>> getFavoriteNews(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                        @RequestParam(name = "page", defaultValue = "0") int page,
                                                                        @RequestParam(name = "size", defaultValue = "10") int size) {
        List<FavoriteSimpleResponse> userFavorites = favoriteService.getUserFavorites(userDetails.getId(), page, size);

        return ResponseEntity.ok(userFavorites);
    }

}
