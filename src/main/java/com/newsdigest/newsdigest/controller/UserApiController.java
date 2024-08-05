package com.newsdigest.newsdigest.controller;

import com.newsdigest.newsdigest.dto.UserRequest;
import com.newsdigest.newsdigest.dto.UserResponse;
import com.newsdigest.newsdigest.service.TokenBlacklistService;
import com.newsdigest.newsdigest.service.UserService;
import com.newsdigest.newsdigest.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserApiController {

    private final TokenBlacklistService tokenBlacklistService;
    private final JwtUtil jwtUtil;
    private final UserService userService;

    /**
     * 로그아웃
     */
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        String token = jwtUtil.extractToken(request);
        if (token != null) {
            long expirationTime = jwtUtil.getExpirationTimeFromToken(token);
            tokenBlacklistService.addToBlacklist(token, expirationTime);
        }
        SecurityContextHolder.clearContext();

        return ResponseEntity.ok().body("로그아웃 되었습니다");
    }

    /**
     * 회원가입
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@Validated @RequestBody UserRequest request,
                                      BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return buildValidationErrorResponse(bindingResult);
        }
        UserResponse userResponse = userService.createUser(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
    }


    private ResponseEntity<?> buildValidationErrorResponse(BindingResult bindingResult) {
        List<String> errorMessages = bindingResult.getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());
        return ResponseEntity.badRequest().body(errorMessages);
    }
}
