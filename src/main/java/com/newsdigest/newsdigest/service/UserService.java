package com.newsdigest.newsdigest.service;

import com.newsdigest.newsdigest.dto.FavoriteSimpleResponse;
import com.newsdigest.newsdigest.dto.UserRequest;
import com.newsdigest.newsdigest.dto.UserResponse;
import com.newsdigest.newsdigest.entity.Favorite;
import com.newsdigest.newsdigest.entity.User;
import com.newsdigest.newsdigest.exception.UserNotFoundException;
import com.newsdigest.newsdigest.repository.FavoriteRepository;
import com.newsdigest.newsdigest.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final FavoriteRepository favoriteRepository;
    private final BCryptPasswordEncoder encoder;

    @Transactional
    public UserResponse createUser(UserRequest userRequest) {
        userRequest.setPassword(encoder.encode(userRequest.getPassword()));
        User user = User.from(userRequest);
        User savedUser = userRepository.save(user);

        return UserResponse.from(savedUser);
    }

    public List<FavoriteSimpleResponse> getUserFavorites(Long userId, int page, int size) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Favorite> favoritePage = favoriteRepository.findAllByUser(user, pageRequest);

        return favoritePage.stream()
                .map(FavoriteSimpleResponse::from)
                .collect(Collectors.toList());
    }

}
