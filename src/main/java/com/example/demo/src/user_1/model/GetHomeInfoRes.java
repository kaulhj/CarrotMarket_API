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
public class GetHomeInfoRes {
    private String productName;
    private int price;
    private String productMainImg;
    private int Likes;
    private String address;
    private Timestamp updated;
}
