package com.newsdigest.newsdigest.entity;

import com.newsdigest.newsdigest.dto.UserRequest;
import com.newsdigest.newsdigest.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user")
public class User extends BaseTimeEntity{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Favorite> favorites = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    List<SearchHistory> searchHistories = new ArrayList<>();

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    public static User from(UserRequest userRequest) {
        return User.builder()
                .name(userRequest.getName())
                .username(userRequest.getUsername())
                .password(userRequest.getPassword())
                .email(userRequest.getEmail())
                .phoneNumber(userRequest.getPhoneNumber())
                .role(Role.USER)
                .build();
    }
}
