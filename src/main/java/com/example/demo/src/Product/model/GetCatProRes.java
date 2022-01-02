package com.example.demo.src.Product.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter // 해당 클래스에 대한 접근자 생성
@Setter // 해당 클래스에 대한 설정자 생성
@AllArgsConstructor // 해당 클래스의 모든 멤버 변수(userIdx, nickname, email, password)를 받는 생성자를 생성
@JsonInclude(JsonInclude.Include.NON_DEFAULT)

public class GetCatProRes {
    private String category;
    private String productMainImg;
    private String updateAt;
    private String price;
    private int comments;
    private int interestings;
}
