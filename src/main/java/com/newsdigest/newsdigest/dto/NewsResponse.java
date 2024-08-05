package com.newsdigest.newsdigest.dto;

import com.newsdigest.newsdigest.entity.News;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewsResponse {

    private Long id;
    private String title;
    private String originalContent;
    private String summarizedContent;

    public static NewsResponse from(News news) {
        return NewsResponse.builder()
                .id(news.getId())
                .title(news.getTitle())
                .originalContent(news.getOriginalContent())
                .summarizedContent(news.getSummarizedContent())
                .build();
    }

}
