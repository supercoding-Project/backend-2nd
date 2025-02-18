package com.github.secondproject.auth.service;

import com.github.secondproject.auth.dto.LoginDto;
import com.github.secondproject.auth.dto.SignUpDto;
import com.github.secondproject.auth.entity.Role;
import com.github.secondproject.auth.entity.UserEntity;
import com.github.secondproject.auth.repository.UserRepository;
import com.github.secondproject.global.config.auth.JwtTokenProvider;
import com.github.secondproject.global.exception.AppException;
import com.github.secondproject.global.exception.ErrorCode;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public void signUp(SignUpDto signUpDto) throws Exception{
        // 이메일 중복 확인
        if (userRepository.findByEmail(signUpDto.getEmail()).isPresent()) {
            throw new AppException(ErrorCode.USERNAME_DUPLICATED, ErrorCode.USERNAME_DUPLICATED.getMessage());
        }

        // 유저네임 중복 확인
        if (userRepository.findByUsername(signUpDto.getUsername()).isPresent()) {
            throw new AppException(ErrorCode.USERNAME_DUPLICATED, ErrorCode.USERNAME_DUPLICATED.getMessage());
        }

        UserEntity userEntity = UserEntity.builder()
                .email(signUpDto.getEmail())
                .password(passwordEncoder.encode(signUpDto.getPassword()))
                .username(signUpDto.getUsername())
                .address(signUpDto.getAddress())
                .phone(signUpDto.getPhone())
                .gender(signUpDto.getGender())
                .role(Role.ROLE_USER)
                .createdAt(LocalDateTime.now())
                .build();

        userRepository.save(userEntity);
    }

    @Transactional
    public ResponseEntity<?> login(LoginDto loginDto, HttpServletResponse httpServletResponse) {
        UserEntity userEntity = userRepository.findByEmail(loginDto.getEmail()).isEmpty() ? null : userRepository.findByEmail(loginDto.getEmail()).get();

        if (userEntity == null) {
            throw new AppException(ErrorCode.CHECK_EMAIL_OR_PASSWORD, ErrorCode.CHECK_EMAIL_OR_PASSWORD.getMessage());
        }

        Map<String, String> response = new HashMap<>();

        String accessToken = "";
        String refreshToken = "";

        try {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword());

            if (!passwordEncoder.matches(loginDto.getPassword(), userEntity.getPassword())) {
                throw new AppException(ErrorCode.NOT_EQUAL_PASSWORD, ErrorCode.NOT_EQUAL_PASSWORD.getMessage());
            }

            accessToken = jwtTokenProvider.createAccessToken(authenticationToken.getName());
            refreshToken = jwtTokenProvider.createRefreshToken(authenticationToken.getName());

            log.info(authenticationToken.getName());
            log.info("accessToken: {}", accessToken);
            log.info("refreshToken: {}", refreshToken);


            httpServletResponse.addHeader("Authorization", accessToken);
            httpServletResponse.addCookie(new Cookie("refresh_token", refreshToken));

            if (userEntity.getRole() != null) {
                response.put("isAuth", "true");
            } else {
                response.put("isAuth", "false");
            };

            response.put("message", "로그인 되었습니다");
            response.put("token_type", "Bearer");
            response.put("access_token", accessToken);
            response.put("refresh_token", refreshToken);
            response.put("email", userEntity.getEmail());

            return ResponseEntity.status(HttpStatus.OK).body(response);

        } catch (BadCredentialsException e) {
            log.error("BadCredentialsException: {}", e.getMessage());
            response.put("message", "잘못된 자격 증명입니다");
            response.put("http_status", HttpStatus.UNAUTHORIZED.toString());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }
    }

    public void withdrawalUser(String loginEmail, String password) {
        UserEntity userEntity = userRepository.findByEmail(loginEmail).orElseThrow(
                () -> new AppException(ErrorCode.USER_EMAIL_NOT_FOUND, ErrorCode.USER_EMAIL_NOT_FOUND.getMessage())
        );

        String encodedPassword = passwordEncoder.encode(password);
        String userEntityPassword = userEntity.getPassword();
        log.info("암호화된 비밀번호: {}", encodedPassword);
        log.info("userEntity에서 가져온 비밀번호: {}", userEntityPassword);
        Date now = new Date();
        LocalDateTime localDateTime = now.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        if (!passwordEncoder.matches(encodedPassword, userEntityPassword)) {
            throw new AppException(ErrorCode.NOT_EQUAL_PASSWORD, ErrorCode.NOT_EQUAL_PASSWORD.getMessage());
        } else {
            userEntity.setDeletedAt(localDateTime);
            UserEntity deletedUser = userRepository.save(userEntity);
        }
    }
}
