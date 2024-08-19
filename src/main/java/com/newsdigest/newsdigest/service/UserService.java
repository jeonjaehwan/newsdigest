package com.newsdigest.newsdigest.service;

import com.newsdigest.newsdigest.dto.UserRequest;
import com.newsdigest.newsdigest.dto.UserResponse;
import com.newsdigest.newsdigest.entity.User;
import com.newsdigest.newsdigest.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;

    @Transactional
    public UserResponse createUser(UserRequest userRequest) {
        userRequest.setPassword(encoder.encode(userRequest.getPassword()));
        User user = User.from(userRequest);
        User savedUser = userRepository.save(user);

        return UserResponse.from(savedUser);
    }

}
