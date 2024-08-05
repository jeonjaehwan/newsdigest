package com.newsdigest.newsdigest.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "news")
public class News extends BaseTimeEntity{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "news_id")
    private Long id;

    @OneToMany(mappedBy = "news", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Favorite> favorites = new ArrayList<>();

    @Column(name = "title", nullable = false)
    private String title;

    @Lob
    @Column(name = "original_content", nullable = false, columnDefinition = "LONGTEXT")
    private String originalContent;

    @Lob
    @Column(name = "summarized_content", nullable = false, columnDefinition = "LONGTEXT")
    private String summarizedContent;

    @Column(name = "published_at", nullable = false)
    private LocalDateTime publishedAt;

    @Column(name = "image_url")
    private String imageUrl;

}
