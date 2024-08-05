package com.newsdigest.newsdigest.dto;

import com.newsdigest.newsdigest.elasticsearch.NewsDocument;
import com.newsdigest.newsdigest.entity.News;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewsSimpleResponse {

    private Long id;
    private String title;
    private String summarizedContent;

    public static NewsSimpleResponse from(NewsDocument newsDocument) {
        return NewsSimpleResponse.builder()
                .id(newsDocument.getId())
                .title(newsDocument.getQuery())
                .summarizedContent(newsDocument.getSummarizedContent())
                .build();
    }

    public static NewsSimpleResponse from(News news) {
        return NewsSimpleResponse.builder()
                .id(news.getId())
                .title(news.getTitle())
                .summarizedContent(news.getSummarizedContent())
                .build();
    }
}
