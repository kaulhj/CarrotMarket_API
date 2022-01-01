package com.example.demo.src.user_1.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter // 해당 클래스에 대한 접근자 생성
@Setter // 해당 클래스에 대한 설정자 생성
@AllArgsConstructor

@JsonInclude(JsonInclude.Include.NON_DEFAULT)

public class GetProdetailRes {
    private int productId;
    private String nickname;
    private String category;
    private String productName;
    private String price;
    private String productDetail;
    private String productMainImg;
    private int viewCount;
    private String updated;
    private double celcius;
    private String address;

}
//유저, 프로덕트, 카테고리, 뷰카운트에서 해당 상품인덱스의 조회수