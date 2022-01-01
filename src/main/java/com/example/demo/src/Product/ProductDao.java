package com.example.demo.src.Product;

import com.example.demo.src.Product.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sound.midi.Patch;
import javax.sql.DataSource;
import java.lang.reflect.GenericArrayType;
import java.sql.Timestamp;
import java.util.List;

@Repository //  [Persistence Layer에서 DAO를 명시하기 위해 사용]

/**
 * DAO란?
 * 데이터베이스 관련 작업을 전담하는 클래스
 * 데이터베이스에 연결하여, 입력 , 수정, 삭제, 조회 등의 작업을 수행
 */


public class ProductDao {

    // *********************** 동작에 있어 필요한 요소들을 불러옵니다. *************************

    private JdbcTemplate jdbcTemplate;

    //public static int authennum = 1000; //인증번호 초기값
    @Autowired //readme 참고
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    public int checkProd(PostProRegReq postProRegReq){
        String checkProdQuery = "select exists(select userId from product where userId=? AND " +
                "productMainImg = ? AND productName = ?)";
        Object[] checkProdParams = new Object[]{postProRegReq.getUserId(), postProRegReq.getProductMainImg(),
        postProRegReq.getProductName()};
        return this. jdbcTemplate.queryForObject(checkProdQuery,
                int.class,
                checkProdParams);
    }


    public int registerProd(PostProRegReq postProRegReq){
        String regiProdQuery = "insert into product (userId, productMainImg,"+
        "productName, categoryId, price, price_suggest, ProductDetail) VALUES (?,?,?,?,?,?,?)";
        Object[] regiProdParmas = new Object[]{postProRegReq.getUserId(), postProRegReq.getProductMainImg(),
        postProRegReq.getProductName(), postProRegReq.getCategory(),postProRegReq.getPrice(),
        postProRegReq.getPrice_suggest(), postProRegReq.getProductDetail()};

        this.jdbcTemplate.update(regiProdQuery,regiProdParmas);

        String lastIneserIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastIneserIdQuery, int.class);

    }



    public int createChatDeal(PostProChatReq postProChatReq){
        String creChatDealQuery = "insert into chats (productId, userId, detail " +
                ") VALUES (?, ?, ?)";
        Object[] creChatDealParams = new Object[]{postProChatReq.getProductId(),postProChatReq.getUserId(),
        postProChatReq.getChatDetail() };

        this.jdbcTemplate.update(creChatDealQuery, creChatDealParams);

        String lastIneserIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastIneserIdQuery, int.class);
    }

    public String registerLike(PostProLikeReq postProLikeReq){
        String reg_LikeQuery1 = "INSERT INTO viewcount(userId, productId) VALUES(?,?)";
        String reg_LikeQuery2 = "INSERT INTO Interesting(userId, productId) VALUES(?,?)";

        Object[] reg_LikeParams = new Object[]{postProLikeReq.getUserId(), postProLikeReq.getProductId()};

        this.jdbcTemplate.update(reg_LikeQuery1, reg_LikeParams);
        this.jdbcTemplate.update(reg_LikeQuery2, reg_LikeParams);

        String SuccessTextQuery = "select productName from product where productId = ?";
        int SuccessTextParams = postProLikeReq.getProductId();
        char quotes = '"';
        return new String("상품 "+String.valueOf(this.jdbcTemplate.queryForObject(SuccessTextQuery,String.class
                ,SuccessTextParams))+quotes+" 이 관심목록에 등록되었습니다");
    }
















}