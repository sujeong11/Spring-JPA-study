package com.dku.springstudy.controller;

import com.dku.springstudy.dto.common.SuccessResponse;
import com.dku.springstudy.dto.auth.request.LoginRequestDto;
import com.dku.springstudy.dto.auth.request.SignUpRequestDto;
import com.dku.springstudy.dto.auth.response.LoginResponseDto;
import com.dku.springstudy.dto.auth.response.SignUpResponseDto;
import com.dku.springstudy.service.AuthService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Api(tags = "인증 API")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(
            summary = "회원가입",
            description = "사용자에게 회원가입에 필요한 데이터를 입력받고, 가입되지 않는 이메일이라면 회원가입을 진행한다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "409", description = "이미 가입된 이메일로 회원가입을 시도하는 경우")
    })
    @PostMapping("/sign-up")
    public ResponseEntity<SuccessResponse<SignUpResponseDto>> signUp(@Valid @RequestBody SignUpRequestDto signUpRequestDto) {
        SignUpResponseDto userId = authService.signUp(signUpRequestDto);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new SuccessResponse<>(userId));
    }

    @Operation(
            summary = "로그인",
            description = "사용자로부터 입력받은 아이디와 비밀번호가 존재한다면 access-token과 refresh-token을 발급한다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "404", description = "이메일(ID)이 존재하지 않는 경우"),
            @ApiResponse(responseCode = "401", description = "비밀번호가 일치하지 않는 경우")
    })
    @PostMapping("/login")
    public ResponseEntity<SuccessResponse<LoginResponseDto>> login(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        LoginResponseDto tokens = authService.login(loginRequestDto);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new SuccessResponse<>(tokens));
    }
}
