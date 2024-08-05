package com.newsdigest.newsdigest.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.newsdigest.newsdigest.dto.NewsResponse;
import com.newsdigest.newsdigest.dto.NewsSimpleResponse;
import com.newsdigest.newsdigest.elasticsearch.ElasticNewsService;
import com.newsdigest.newsdigest.entity.News;
import com.newsdigest.newsdigest.exception.NewsNotFoundException;
import com.newsdigest.newsdigest.repository.NewsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NewsService {

    private final NewsRepository newsRepository;
    private final RestTemplate restTemplate;
    private final SummarizationService summarizationService;
    private final ElasticNewsService elasticNewsService;

    @Value("${newsApi.url}")
    private String apiUrl;

    @Value("${newsApi.key}")
    private String apiKey;

    @Transactional
    @CacheEvict(value = {"newsList", "news", "newsSearch"}, allEntries = true)
    public void fetchAndSaveNews() {
        String url = String.format("%s?apiKey=%s&sources=cnn,bbc-news,the-new-york-times,reuters,techcrunch", apiUrl, apiKey);
        String response = restTemplate.getForObject(url, String.class);
        log.info("responses = {}", response);

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            JsonNode rootNode = objectMapper.readTree(response);
            JsonNode articlesNode = rootNode.path("articles");

            List<News> newsList = StreamSupport.stream(articlesNode.spliterator(), false)
                    .map(this::convertJsonToNews)
                    .filter(Objects::nonNull)  // null 값을 필터링
                    .collect(Collectors.toList());

            if (!newsList.isEmpty()) {
                newsRepository.saveAll(newsList);
                newsList.forEach(elasticNewsService::saveNews);

                log.info("Successfully saved {} news articles", newsList.size());
            } else {
                log.warn("No valid news articles to save");
            }
        } catch (IOException e) {
            log.error("Error parsing news API response", e);
        }
    }

    @Cacheable(value = "newsList", key = "#page + '-' + #size")
    public List<NewsSimpleResponse> getAllNews(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<News> newsPage = newsRepository.findAll(pageable);

        return newsPage.stream()
                .map(NewsSimpleResponse::from)
                .collect(Collectors.toList());
    }

    @Cacheable(value = "news", key = "#newsId")
    public NewsResponse getNews(Long newsId) {
        News news = newsRepository.findById(newsId)
                .orElseThrow(() -> new NewsNotFoundException(newsId));

        return NewsResponse.from(news);
    }

    private News convertJsonToNews(JsonNode articleNode) {
        String content = articleNode.path("content").asText(null);
        String description = articleNode.path("description").asText(null);

        // content와 description이 모두 비어 있으면 null 반환
        if ((content == null || content.isEmpty()) && (description == null || description.isEmpty())) {
            return null;
        }

        // content가 비어 있으면 description을 content로 대체
        if (content == null || content.isEmpty()) {
            content = description;
        }

        String summarizedContent = summarizationService.summarize(content);
        if (summarizedContent.length() > content.length()) {
            content = summarizedContent;
        }

        String publishedAtString = articleNode.path("publishedAt").asText("");
        String urlToImage = articleNode.path("urlToImage").asText(null);
        String title = articleNode.path("title").asText(null);
        LocalDateTime publishedAt;

        try {
            publishedAt = LocalDateTime.parse(publishedAtString, DateTimeFormatter.ISO_DATE_TIME);
        } catch (DateTimeParseException e) {
            log.error("Error parsing publishedAt: {}", publishedAtString, e);
            publishedAt = LocalDateTime.now();
        }

        return News.builder()
                .title(title)
                .originalContent(content)
                .summarizedContent(summarizedContent)
                .publishedAt(publishedAt)
                .imageUrl(urlToImage)
                .build();
    }
}
