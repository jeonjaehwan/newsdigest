package com.newsdigest.newsdigest.dto;

import com.newsdigest.newsdigest.entity.User;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    private String name;

    private String username;

    private String password;

    private String email;

    private String phoneNumber;

    public static UserResponse from(User user) {
        return UserResponse.builder()
                .name(user.getName())
                .username(user.getUsername())
                .password(user.getPassword())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .build();
    }
}
