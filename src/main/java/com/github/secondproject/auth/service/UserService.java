package com.github.secondproject.auth.service;

import com.github.secondproject.auth.dto.LoginDto;
import com.github.secondproject.auth.dto.SignUpDto;
import com.github.secondproject.auth.entity.RefreshTokenEntity;
import com.github.secondproject.auth.entity.Role;
import com.github.secondproject.auth.entity.UserEntity;
import com.github.secondproject.auth.entity.UserStatus;
import com.github.secondproject.auth.repository.RefreshTokenRepository;
import com.github.secondproject.auth.repository.UserRepository;
import com.github.secondproject.global.config.auth.JwtTokenProvider;
import com.github.secondproject.global.config.auth.filter.JwtAuthenticationFilter;
import com.github.secondproject.global.exception.AppException;
import com.github.secondproject.global.exception.ErrorCode;
import com.github.secondproject.global.util.PasswordUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordUtil passwordUtil = new PasswordUtil();
    private final UserImageService userImageService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public void signUp(SignUpDto signUpDto, MultipartFile image) throws Exception{
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
                .password(passwordUtil.encrypt(signUpDto.getPassword()))
                .username(signUpDto.getUsername())
                .address(signUpDto.getAddress())
                .phone(signUpDto.getPhone())
                .gender(signUpDto.getGender())
                .budget(0)
                .role(Role.ROLE_USER)
                .status(UserStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .build();

        if (image != null) {
            userImageService.uploadUserImage(userEntity, image);
        }

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

            if (!Objects.equals(passwordUtil.encrypt(loginDto.getPassword()), userEntity.getPassword())) {
                throw new AppException(ErrorCode.NOT_EQUAL_PASSWORD, ErrorCode.NOT_EQUAL_PASSWORD.getMessage());
            }

            accessToken = jwtTokenProvider.createAccessToken(authenticationToken.getName());
            refreshToken = jwtTokenProvider.createRefreshToken(authenticationToken.getName());

            log.info(authenticationToken.getName());
            log.info("accessToken: {}", accessToken);
            log.info("refreshToken: {}", refreshToken);

            httpServletResponse.addHeader("Authorization", accessToken);
            httpServletResponse.addCookie(new Cookie("refresh_token", refreshToken));

            addRefresh(loginDto.getEmail(), refreshToken, 10800);

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

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public void withdrawalUser(String loginEmail, String requestBodyPassword) throws NoSuchAlgorithmException {
        UserEntity userEntity = userRepository.findByEmail(loginEmail).orElseThrow(
                () -> new AppException(ErrorCode.USER_EMAIL_NOT_FOUND, ErrorCode.USER_EMAIL_NOT_FOUND.getMessage())
        );

        String encodedPassword = passwordUtil.encrypt(requestBodyPassword);
        String userEntityPassword = userEntity.getPassword();
        log.info("암호화된 비밀번호: {}", encodedPassword);
        log.info("userEntity에서 가져온 비밀번호: {}", userEntityPassword);
        Date now = new Date();
        LocalDateTime localDateTime = now.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        if (!Objects.equals(encodedPassword, userEntityPassword)) {
            throw new AppException(ErrorCode.NOT_EQUAL_PASSWORD, ErrorCode.NOT_EQUAL_PASSWORD.getMessage());
        } else {
            userEntity.setEmail("");
            userEntity.setPassword("");
            userEntity.setUsername("삭제한 회원");
            userEntity.setAddress("");
            userEntity.setPhone("");
            userEntity.setGender("");
            userEntity.setStatus(UserStatus.DELETED);
            userEntity.setDeletedAt(localDateTime);
            UserEntity deletedUser = userRepository.save(userEntity);
        }
    }

    @Transactional
    public ResponseEntity<?> refreshToken(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        // 쿠키에 있는 Refresh Token 가져오기
        String refreshToken = findRefreshTokenCookie(httpServletRequest);

        // 새로운 Access Token 생성
        String email = jwtTokenProvider.getEmailByToken(refreshToken);
        UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(
                () -> new AppException(ErrorCode.NOT_FOUND_USER, ErrorCode.NOT_FOUND_USER.getMessage())
        );

        String newAccessToken = jwtTokenProvider.createAccessToken(email);
        String newRefreshToken  = jwtTokenProvider.createRefreshToken(email);
        Role role = userEntity.getRole();

        jwtTokenProvider.setRole(newAccessToken, role.getType());

        httpServletResponse.addHeader("Authorization", newAccessToken);
        httpServletResponse.addCookie(new Cookie("refresh_token", newRefreshToken));

        // Refresh Token Entity 변경
        refreshTokenRepository.deleteByRefreshToken(refreshToken);
        addRefresh(email, newRefreshToken, 12*60);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    public String findRefreshTokenCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refresh_token".equals(cookie.getName())) {
                    return cookie.getValue();
                }

                if (cookie.getValue() == null) {
                    throw new AppException(ErrorCode.NOT_FOUND_REFRESH_TOKEN, ErrorCode.NOT_FOUND_REFRESH_TOKEN.getMessage());
                }
                return cookie.getValue();
            }

        } else {
            throw new AppException(ErrorCode.NOT_FOUND_COOKIE, ErrorCode.NOT_FOUND_COOKIE.getMessage());
        }
        return null;
    }

    protected void addRefresh(String email, String refreshToken, int expiredMinute){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, expiredMinute);
        Date date = calendar.getTime();

        RefreshTokenEntity refreshTokenEntity = new RefreshTokenEntity();
        refreshTokenEntity.setEmail(email);
        refreshTokenEntity.setRefreshToken(refreshToken);
        refreshTokenEntity.setExpiration(date.toString());

        refreshTokenRepository.save(refreshTokenEntity);
    }
}
