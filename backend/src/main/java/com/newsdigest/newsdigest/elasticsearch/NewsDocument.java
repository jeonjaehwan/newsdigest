package com.newsdigest.newsdigest.elasticsearch;

import com.newsdigest.newsdigest.entity.News;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "news")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class NewsDocument {

    @Id
    private Long id;

    @Field(type = FieldType.Text)
    private String query;

    @Field(type = FieldType.Text)
    private String originalContent;

    @Field(type = FieldType.Text)
    private String summarizedContent;

    @Field(type = FieldType.Text)
    private String imageUrl;

    public static NewsDocument from(News news) {
        return NewsDocument.builder()
                .id(news.getId())
                .query(news.getTitle())
                .originalContent(news.getOriginalContent())
                .summarizedContent(news.getSummarizedContent())
                .imageUrl(news.getImageUrl())
                .build();
    }
}
