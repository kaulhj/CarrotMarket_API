package com.example.demo.src.Product.model;

import lombok.*;

@Getter // 해당 클래스에 대한 접근자 생성
@Setter // 해당 클래스에 대한 설정자 생성
@AllArgsConstructor // 해당 클래스의 모든 멤버 변수(email, password, nickname, profileImage)를 받는 생성자를 생성

public class Product {
    private int userId;
    private int productId;
    private String productDetail;
    private int Likes;
    private char dragcommitSt;
    private String productMainImg;
    private String productName;
    private int categoryId;
    private int price;
    private String price_suggest;
    private int use_ageId;
    private String status;

}
