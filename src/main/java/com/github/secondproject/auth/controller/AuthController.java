package com.github.secondproject.auth.controller;
import com.github.secondproject.auth.dto.LoginDto;
import com.github.secondproject.auth.dto.SignUpDto;
import com.github.secondproject.auth.entity.UserEntity;
import com.github.secondproject.auth.service.UserService;
import com.github.secondproject.global.config.auth.custom.CustomUserDetails;
import com.github.secondproject.global.dto.MsgResponseDto;
import com.github.secondproject.global.exception.ErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    // 회원가입
    @Operation(summary = "유저 회원가입", description = "회원가입 api 입니다.")
    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@Valid @RequestBody SignUpDto signUpDto, BindingResult bindingResult) throws Exception{
      log.info("[POST]: 회원가입 요청");

      if (bindingResult.hasErrors()) {
          return ResponseEntity.badRequest().body(ErrorCode.BINDING_RESULT_ERROR.getMessage());
      }

      userService.signUp(signUpDto);

      return ResponseEntity.ok(new MsgResponseDto("회원가입이 완료되었습니다.", HttpStatus.OK.value()));
    }

    // 로그인
    @Operation(summary = "유저 로그인", description = "로그인 api 입니다.")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto, HttpServletResponse httpServletResponse) {
        log.info("[POST]: 로그인 요청");

        return userService.login(loginDto, httpServletResponse);
    }

    // 회원탈퇴
    @PutMapping("/v1/withdrawal")
    public ResponseEntity<?> withdrawalUser(@RequestParam String password, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        String loginEmail = customUserDetails.getUsername();

        userService.withdrawalUser(loginEmail, password);
        return ResponseEntity.ok(new MsgResponseDto("회원탈퇴가 완료되었습니다.", HttpStatus.OK.value()));
    }
}
