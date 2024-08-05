package com.newsdigest.newsdigest.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginUserRequest {

    @NotNull
    private String username;

    @NotNull
    private String password;
}
