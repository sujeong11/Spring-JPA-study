package com.dku.springstudy.controller;

import com.dku.springstudy.dto.common.SuccessResponse;
import com.dku.springstudy.dto.product.request.ProductCreateRequestDto;
import com.dku.springstudy.dto.product.response.ProductCreateResponseDto;
import com.dku.springstudy.dto.product.response.ProductInfoResponseDto;
import com.dku.springstudy.security.CustomUserDetails;
import com.dku.springstudy.service.ProductService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@Api(tags = "상품 API")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProductController {
    
    private final ProductService productService;

    @Operation(
            summary = "상품 등록",
            description = "로그인된 사용자가 상품의 사진 / 제목 / 카테고리 / 가격 / 내용을 입력하면 상품 글을 등록한다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "404", description = "아이디(PK)가 존재하지 않는 경우"),
            @ApiResponse(responseCode = "500", description = "파일의 업로드가 실패했거나 파일 확장자가 올바르지 않는 경우")
    })
    @PostMapping("/product")
    public ResponseEntity<SuccessResponse<ProductCreateResponseDto>> createPost(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Parameter(description = "<code>data</code> 키 값으로 CreateRequestDto의 필드들을 입력한다.")
            @Valid @RequestPart("data") ProductCreateRequestDto dto,
            @Parameter(description = "<code>file</code> 키 값으로 이미지들을 입력한다.")
            @RequestPart(value = "file", required = false) List<MultipartFile> file
    ) {

        ProductCreateResponseDto response = productService.createPost(dto, file, customUserDetails.getId());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new SuccessResponse<>(response));
    }

    @Operation(
            summary = "상품 상세 조회",
            description = "상품 아이디를 입력받아 상세 정보를 조회한다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "404", description = "회원 또는 상품의 아이디(PK)가 존재하지 않는 경우"),
    })
    @GetMapping("/product/{productId}")
    public ResponseEntity<SuccessResponse<ProductInfoResponseDto>> getProductInfo(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable("productId") Long productId
    ) {
        ProductInfoResponseDto response = productService.getProductInfo(customUserDetails.getId(), productId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new SuccessResponse<>(response));
    }
}
