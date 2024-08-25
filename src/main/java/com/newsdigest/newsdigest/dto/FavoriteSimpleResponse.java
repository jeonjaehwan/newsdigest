package com.newsdigest.newsdigest.dto;

import com.newsdigest.newsdigest.entity.Favorite;
import lombok.*;

import java.time.LocalDate;

@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteSimpleResponse {

    private Long newsId;

    private String title;

    private String summarizedContent;

    private LocalDate publishedAt;

    public static FavoriteSimpleResponse from(Favorite favorite) {
        return FavoriteSimpleResponse.builder()
                .newsId(favorite.getNews().getId())
                .title(favorite.getNews().getTitle())
                .summarizedContent(favorite.getNews().getSummarizedContent())
                .publishedAt(favorite.getNews().getPublishedAt())
                .build();
    }
}
