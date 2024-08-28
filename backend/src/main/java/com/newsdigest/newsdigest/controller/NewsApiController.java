package com.newsdigest.newsdigest.controller;

import com.newsdigest.newsdigest.dto.NewsResponse;
import com.newsdigest.newsdigest.dto.NewsSimpleResponse;
import com.newsdigest.newsdigest.service.NewsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/news")
public class NewsApiController {

    private final NewsService newsService;

    /**
     * 전체 뉴스 목록 조회
     */
    @GetMapping
    public ResponseEntity<List<NewsSimpleResponse>> getNews(@RequestParam(name = "page",defaultValue = "0") int page,
                                                            @RequestParam(name = "size", defaultValue = "15") int size) {
        List<NewsSimpleResponse> newsResponses = newsService.getAllNews(page, size);

        return ResponseEntity.ok(newsResponses);
    }

    /**
     * 특정 뉴스 상세 조회
     */
    @GetMapping("/{news-id}")
    public ResponseEntity<NewsResponse> getNewsById(@PathVariable("news-id") Long newsId) {
        NewsResponse newsById = newsService.getNews(newsId);

        return ResponseEntity.ok(newsById);
    }
}
