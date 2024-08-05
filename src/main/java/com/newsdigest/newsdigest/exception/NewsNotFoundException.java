package com.newsdigest.newsdigest.exception;

public class NewsNotFoundException extends RuntimeException {

    public NewsNotFoundException(Long newsId) {
        super(newsId + "의 뉴스를 찾을 수 없습니다.");
    }
}
