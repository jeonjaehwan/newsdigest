package com.newsdigest.newsdigest.controller;

import com.newsdigest.newsdigest.dto.NewsSimpleResponse;
import com.newsdigest.newsdigest.elasticsearch.ElasticNewsService;
import com.newsdigest.newsdigest.service.SearchHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    @GetMapping("/{userId}")
    public ResponseEntity<List<NewsSimpleResponse>> searchNewsByTitle(@PathVariable("userId") Long userId,
                                                                      @RequestParam("query") String query) {
        List<NewsSimpleResponse> newsSimpleResponses = elasticNewsService.searchNewsByQuery(userId, query);

        return ResponseEntity.ok(newsSimpleResponses);
    }

    /**
     * 뉴스 검색 기록 삭제
     */
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteSearchHistory(@PathVariable("userId") Long userId,
                                                    @RequestParam("query") String query) {
        searchHistoryService.deleteSearchHistory(userId, query);

        return ResponseEntity.noContent().build();
    }
}
