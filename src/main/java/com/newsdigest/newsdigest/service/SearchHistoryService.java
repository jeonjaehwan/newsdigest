package com.newsdigest.newsdigest.service;

import com.newsdigest.newsdigest.entity.SearchHistory;
import com.newsdigest.newsdigest.entity.User;
import com.newsdigest.newsdigest.exception.UserNotFoundException;
import com.newsdigest.newsdigest.repository.SearchHistoryRepository;
import com.newsdigest.newsdigest.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class SearchHistoryService {

    private static final int MAX_SEARCH_HISTORY = 5;

    private final SearchHistoryRepository searchHistoryRepository;
    private final UserRepository userRepository;

    @Transactional
    public void saveSearchHistory(Long userId, String query) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        SearchHistory searchHistory = SearchHistory.from(user, query);

        searchHistoryRepository.save(searchHistory);

        List<SearchHistory> searchHistoryList = searchHistoryRepository.findByUserOrderByCreatedDateAsc(user);

        if (searchHistoryList.size() > MAX_SEARCH_HISTORY) {
            List<SearchHistory> deleteList = searchHistoryList.stream()
                    .limit(searchHistoryList.size() - MAX_SEARCH_HISTORY)
                    .collect(Collectors.toList());
            searchHistoryRepository.deleteAll(deleteList);
        }

    }

    @Transactional
    public void deleteSearchHistory(Long userId, String query) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        List<SearchHistory> searchHistoryList = searchHistoryRepository.findByUserAndQuery(user, query);

       searchHistoryRepository.deleteAll(searchHistoryList);
    }
}
