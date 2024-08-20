package com.newsdigest.newsdigest.controller;

import com.newsdigest.newsdigest.dto.NewsSimpleResponse;
import com.newsdigest.newsdigest.dto.SearchHistoryResponse;
import com.newsdigest.newsdigest.elasticsearch.ElasticNewsService;
import com.newsdigest.newsdigest.security.CustomUserDetails;
import com.newsdigest.newsdigest.service.SearchHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/search-history")
public class SearchHistoryApiController {

    private final ElasticNewsService elasticNewsService;
    private final SearchHistoryService searchHistoryService;

    /**
     * 뉴스 검색 및 검색 기록 저장
     */
    @GetMapping
    public ResponseEntity<List<NewsSimpleResponse>> searchNewsByTitle(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                      @RequestParam("query") String query) {
        List<NewsSimpleResponse> newsSimpleResponses = elasticNewsService.searchNewsByQuery(userDetails.getId(), query);

        return ResponseEntity.ok(newsSimpleResponses);
    }

    /**
     * 뉴스 검색 기록 삭제
     */
    @DeleteMapping
    public ResponseEntity<Void> deleteSearchHistory(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                    @RequestParam("query") String query) {
        searchHistoryService.deleteSearchHistory(userDetails.getId(), query);

        return ResponseEntity.noContent().build();
    }

    /**
     * 최근 검색 기록 5개 조회
     */
    @GetMapping("/recent")
    public ResponseEntity<List<SearchHistoryResponse>> getRecentSearchQueries(@AuthenticationPrincipal CustomUserDetails userDetails) {
        List<SearchHistoryResponse> recentQueries = searchHistoryService.getSearchHistory(userDetails.getId());

        return ResponseEntity.ok(recentQueries);
    }
}
