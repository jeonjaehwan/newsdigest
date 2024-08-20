package com.newsdigest.newsdigest.dto;

import com.newsdigest.newsdigest.entity.SearchHistory;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchHistoryResponse {

    private String query;

    public static SearchHistoryResponse from(SearchHistory searchHistory) {
        return SearchHistoryResponse
                .builder()
                .query(searchHistory.getQuery())
                .build();
    }
}
