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

    public int modifyProduct(PatchProdReq patchProdReq){
        String modifyProdQuery = "update product  set  " +
                "productMainImg= ?, " +
                "productName =?, " +
                "categoryId = ?," +
                "price = ?, " +
                "price_suggest = ?," +
                "useAgeId =?, " +
                "status =? where productId = ? limit 1";

        Object[] modifyProdParams = new Object[]{
                patchProdReq.getProductMainImg(),patchProdReq.getProductName(),
                patchProdReq.getCategoryId(), patchProdReq.getPrice(),
                patchProdReq.getPrice_suggest(), patchProdReq.getUse_ageId(),
                patchProdReq.getStatus(),patchProdReq.getProductId()};


        return this. jdbcTemplate.update(modifyProdQuery, modifyProdParams);
    }

    public int deleteProd(int productId){
        String DeleteProdQuery = "delete from product where productId = ? ";
        int DeleteProdParams = productId;

        this.jdbcTemplate.update(DeleteProdQuery,DeleteProdParams);

        return productId;
    }

    public List<GetBuyListRes> getBuyListByUId(int userId){
        String getBuyListQuery = "SELECT p.productId, p.productName, CONCAT(price, '원')as price, productMainImg,\n" +
                "                       U.address, p.status,p.Likes,U.nickname,b.userId,count(c.productId)as comments,\n" +
                "                     (CASE\n" +
                "                            WHEN TIMESTAMPDIFF(MINUTE, p.updateAt, NOW()) < 60 THEN CONCAT(TIMESTAMPDIFF(MINUTE, p.updateAt, NOW()), '분 전')\n" +
                "                          WHEN TIMESTAMPDIFF(HOUR, p.updateAT, NOW()) < 24 THEN CONCAT(TIMESTAMPDIFF(HOUR, p.updateAt, NOW()), '시간 전')\n" +
                "                           WHEN TIMESTAMPDIFF(DAY, p.updateAt, NOW()) <30 THEN CONCAT(TIMESTAMPDIFF(DAY, p.updateAt, NOW()), '일 전')\n" +
                "                           END) AS time\n" +
                "\n" +
                "                FROM product as p\n" +
                "                LEFT OUTER JOIN chats as c on c.productId= p.productId\n" +
                "                LEFT JOIN user_1 U on U.userId  = p.userId\n" +
                "                INNER JOIN BuyTransaction b on p.buyId = b.buyId\n" +
                "                where b.userId =? AND\n" +
                "                 p.status = 'COMPLETE'\n" +
                "                GROUP BY p.productId" +
                "                ORDER BY productId";
        int getBuyListParams = userId;
        return this.jdbcTemplate.query(getBuyListQuery,
                (rs,rowNum) -> new GetBuyListRes(
                        rs.getInt("productId"),
                        rs.getString("productMainImg"),
                        rs.getString("productName"),
                        rs.getString("address"),
                        rs.getString("time"),
                        rs.getString("status"),
                        rs.getString("price"),
                        rs.getInt("comments"),
                        rs.getInt("Likes")),
                        getBuyListParams);
    }



    //.해당카테고리의 상품 조회

    public List<GetCatProRes> getCatProByUId(String category){
        String getCatProByUIdQuery = "select p.productId,c.category,\n" +
                "               productName,productMainImg,\n" +
                "              u.address,\n" +
                "               (CASE\n" +
                "                            WHEN TIMESTAMPDIFF(MINUTE, p.updateAt, NOW()) < 60 THEN CONCAT(TIMESTAMPDIFF(MINUTE, p.updateAt, NOW()), '분 전')\n" +
                "                          WHEN TIMESTAMPDIFF(HOUR, p.updateAT, NOW()) < 24 THEN CONCAT(TIMESTAMPDIFF(HOUR, p.updateAt, NOW()), '시간 전')\n" +
                "                           WHEN TIMESTAMPDIFF(DAY, p.updateAt, NOW()) <30 THEN CONCAT(TIMESTAMPDIFF(DAY, p.updateAt, NOW()), '일 전')\n" +
                "                           END) AS time,\n" +
                "               price,\n" +
                "                count(c1.productId)as comments,\n" +
                "               Likes\n" +
                "            from product p\n" +
                "            LEFT JOIN chats c1 on c1.productId = p.productId\n" +
                "LEFT JOIN Category c on c.categoryId = p.categoryId\n" +
                "LEFT JOIN user_1 u on u.userId = p.userId\n" +
                "where c.category = ? \n" +
                "GROUP BY p.productId";
        String getCatProByUIdParams = category;
        return this.jdbcTemplate.query(getCatProByUIdQuery,
                (rs,rowNum) -> new GetCatProRes(
                        rs.getString("category"),
                        rs.getString("productMainImg"),
                        rs.getString("time"),
                        rs.getString("price"),
                        rs.getInt("comments"),
                        rs.getInt("Likes")
                ),
                getCatProByUIdParams);
    }

}