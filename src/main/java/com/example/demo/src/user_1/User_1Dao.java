package com.example.demo.src.user_1;

import com.example.demo.src.user_1.model.*;
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


public class User_1Dao {

    // *********************** 동작에 있어 필요한 요소들을 불러옵니다. *************************

    private JdbcTemplate jdbcTemplate;
    //public static int authennum = 1000; //인증번호 초기값
    @Autowired //readme 참고
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
// ******************************************************************************


    public int createUser1(Postuser_1Req postUserReq1) {
        String createUserQuery = "insert into user_1 (nickname, address, phonenum, authentication, authnum) " +
                "VALUES (?,?,?,?,?)"; // 실행될 동적 쿼리문
        //String authe = "1000";
        Object[] createUserParams = new Object[]{postUserReq1.getNickname(),postUserReq1.getAddress(),
                postUserReq1.getPhonenum(),user_1Service.pwd,user_1Service.athentnum}; // 동적 쿼리의 ?부분에 주입될 값
        this.jdbcTemplate.update(createUserQuery, createUserParams);
        //인증번호 1값 증가

        // email -> postUserReq.getEmail(), password -> postUserReq.getPassword(), nickname -> postUserReq.getNickname() 로 매핑(대응)시킨다음 쿼리문을 실행한다.
        // 즉 DB의 User Table에 (email, password, nickname)값을 가지는 유저 데이터를 삽입(생성)한다.

        String lastInserIdQuery = "select last_insert_id()"; // 가장 마지막에 삽입된(생성된) id값은 가져온다.


        return this.jdbcTemplate.queryForObject(lastInserIdQuery, int.class); // 해당 쿼리문의 결과 마지막으로 삽인된 유저의 userIdx번호를 반환한다.
    }

    public int checkEmail1(String useremailp) {
        String checkEmailQuery = "select exists(select useremail from user_1 where useremail = ?)"; // User Table에 해당 email 값을 갖는 유저 정보가 존재하는가?
        String checkEmailParams = useremailp; // 해당(확인할) 이메일 값
        return this.jdbcTemplate.queryForObject(checkEmailQuery,
                int.class,
                checkEmailParams); // checkEmailQuery, checkEmailParams를 통해 가져온 값(intgud)을 반환한다. -> 쿼리문의 결과(존재하지 않음(False,0),존재함(True, 1))를 int형(0,1)으로 반환됩니다.
    }

    public int checkNickname(String nickname){
        String checkNicknameQuery = "select exists(select nickname from user_1 where nickname = ?)";
        String checkNicknameParams = nickname;
        return this.jdbcTemplate.queryForObject(checkNicknameQuery,
                int.class,
                checkNicknameParams);
    }
    public user_1 getAuth(PostLoginReq1 postLoginReq1){
        String getAuthQuery = "select * from user_1 where authnum =?";
        String getAuthParams = postLoginReq1.getAuthnum();

        return this.jdbcTemplate.queryForObject(getAuthQuery,
                (rs, rowNum) -> new user_1(
                        rs.getInt("userId"),
                        rs.getString("nickname"),
                        rs.getString("address"),
                        rs.getString("authentication"),
                        rs.getString("useremail"),
                        rs.getString("phonenum"),
                        rs.getString("authnum"),
                        rs.getString("profileImg")
                ),
                getAuthParams //쿼리문에 ? 가 있을 경우 넣어주기
        );


    }

    public user_1 getphonenum(PostLoginReq1 postLoginReq1){
        String getphonenumQuery = "select * from user_1 where phonenum = ?";
        String getphonenumParams = postLoginReq1.getPhonenum();

        return this.jdbcTemplate.queryForObject(getphonenumQuery,
                (rs, rowNum) -> new user_1(
                        rs.getInt("userId"),
                        rs.getString("nickname"),
                        rs.getString("address"),
                        rs.getString("authentication"),
                        rs.getString("useremail"),
                        rs.getString("phonenum"),
                        rs.getString("authnum"),
                        rs.getString("profileImg")
                ),
                getphonenumParams
        );
    }

    public int modifyUemail(PatchUemailReq patchUemailReq){
        String modifyUemailQuery = "update user_1 set useremail = ? where userId = ?";
        Object[] modifyUemailParams = new Object[]{patchUemailReq.getUseremail(), patchUemailReq.getUserId()};

        return this.jdbcTemplate.update(modifyUemailQuery,modifyUemailParams);
    }

    public int modifyprofileImg(PatchprofImgReq patchprofImgReq){
        String modifyprofileImgQuery = "update user_1 set profileImg =? where userId =?";
        Object[] modifyprofileParams = new Object[]{patchprofImgReq.getProfileImg(), patchprofImgReq.getUserId()};

        return this.jdbcTemplate.update(modifyprofileImgQuery,modifyprofileParams);
    };

    public Getuser_1Res getUser(int userId){
        String getUserQuery = "select * from user_1 where userId = ?";
        int getUserParams = userId;
        return this.jdbcTemplate.queryForObject(getUserQuery,
                (rs,rowNum) -> new Getuser_1Res(
                        rs.getInt("userId"),
                        rs.getString("nickname"),
                        rs.getString("address"),
                        rs.getString("authentication"),
                        rs.getString("useremail"),
                        rs.getString("phonenum"),
                        rs.getString("authnum"),
                        rs.getString("profileImg")),
                getUserParams);
    }

    //해당주소를 갖는 유저정보들 조회
    public List<Getuser_1Res> getUsersByAddress(String address){
        String getUsersByAddressQuery = "select " +
                "userId, nickname,address,authentication,useremail," +
                "phonenum,authnum,profileImg  from user_1 where address =?";
        String getUsersByAddressParams = address;
        return this.jdbcTemplate.query(getUsersByAddressQuery,
                (rs,rowNum) -> new Getuser_1Res(
                        rs.getInt("userId"),
                        rs.getString("nickname"),
                        rs.getString("address"),
                        rs.getString("authentication"),
                        rs.getString("useremail"),
                        rs.getString("phonenum"),
                        rs.getString("authnum"),
                        rs.getString("profileImg")),
                getUsersByAddressParams);

    }

    public List<Getuser_1Res> getUsers(){
        String getUsersQuery = "select * from user_1";
        return this.jdbcTemplate.query(getUsersQuery,
                (rs,rowNum) -> new Getuser_1Res(
                        rs.getInt("userId"),
                        rs.getString("nickname"),
                        rs.getString("address"),
                        rs.getString("authentication"),
                        rs.getString("useremail"),
                        rs.getString("phonenum"),
                        rs.getString("authnum"),
                        rs.getString("profileImg"))

                );
    }


    //INTELLIJ, DAO에서 입력된 주소에 해당하는 상품정보들 가져오기 // user_1과 product join 실패..
    public List<GetHomeInfoRes> getHomeInfoByAdd(String address){

                    String getHomeInfoByAddQuery = "select  a.productName, " +
                            "a.price, a.productMainImg,a.Likes, a.updateAt ,b.address from product as a " +
                            " join user_1 as b on a.userId = b.userId " +
                            "where b.address = ?";
                    //"inner join user_1 as b on a.userId = b.userId"+
                    //"where b.address = ?";
                    String getHomeInfoByAddParam = address;
                    return this.jdbcTemplate.query(getHomeInfoByAddQuery,
                            (rs, rowNum) -> new GetHomeInfoRes(
                                    rs.getString("productName"),
                                    rs.getInt("price"),
                                    rs.getString("productMainImg"),
                                    rs.getInt("Likes"),
                                    rs.getString("address"),
                                    rs.getTimestamp("updateAt")),

                            getHomeInfoByAddParam
                    );

                }

          //8.특정 상품의 상세정보 조회

        public GetProdetailRes getProDetail(String prodname){
        String getProDetailQuery = "select productId, productName, CONCAT(price, '원')As price, productDetail,\n" +
                "       U.nickName, U.address, celcius,\n" +
                "        productMainImg," +
                "(CASE\n" +
                "            WHEN TIMESTAMPDIFF(MINUTE, p.updateAt, NOW()) < 60 THEN CONCAT(TIMESTAMPDIFF(MINUTE, p.updateAt, NOW()), '분 전')\n" +
                "            WHEN TIMESTAMPDIFF(HOUR, p.updateAT, NOW()) < 24 THEN CONCAT(TIMESTAMPDIFF(HOUR, p.updateAt, NOW()), '시간 전')\n" +
                "            WHEN TIMESTAMPDIFF(DAY, p.updateAt, NOW()) <30 THEN CONCAT(TIMESTAMPDIFF(DAY, p.updateAt, NOW()), '일 전')\n" +
                "            END) AS time," +
                "       category,\n" +
                "       (SELECT count(*)\n" +
                "FROM product as d\n" +
                "INNER JOIN viewcount v on d.productId = v.productId\n" +
                "GROUP BY d.productName\n" +
                "HAVING d.productName = ?) as count\n" +
                "FROM product as p\n" +
                "INNER JOIN user_1 U on p.userId = U.userId\n" +
                "INNER JOIN Category C on p.categoryId = C.categoryId\n" +
                "WHERE productName = ?";
            Object[] getProDetailParams = new Object[]{prodname, prodname};
        return this.jdbcTemplate.queryForObject(getProDetailQuery,
                    (rs, rowNum) -> new GetProdetailRes(
                            rs.getInt("productId"),
                            rs.getString("nickname"),
                            rs.getString("category"),
                            rs.getString("productName"),
                            rs.getString("price"),
                            rs.getString("productDetail"),
                            rs.getString("productMainImg"),
                            rs.getInt("count"),
                            rs.getString("time"),
                            rs.getDouble("celcius"),
                            rs.getString("address")),
            getProDetailParams
            );


        }


        //나의 거래 완료 상품 추출
            public List<GetCompProdRes> getCompProds(){
                String getCompProdsQuery = "SELECT p.productId, p.productName, CONCAT(price, '원')as price, productMainImg,\n" +
                        "       U.address, p.status,p.Likes, count(*) as comments,\n" +
                        "       (CASE\n" +
                        "            WHEN TIMESTAMPDIFF(MINUTE, p.updateAt, NOW()) < 60 THEN CONCAT(TIMESTAMPDIFF(MINUTE, p.updateAt, NOW()), '분 전')\n" +
                        "            WHEN TIMESTAMPDIFF(HOUR, p.updateAT, NOW()) < 24 THEN CONCAT(TIMESTAMPDIFF(HOUR, p.updateAt, NOW()), '시간 전')\n" +
                        "            WHEN TIMESTAMPDIFF(DAY, p.updateAt, NOW()) <30 THEN CONCAT(TIMESTAMPDIFF(DAY, p.updateAt, NOW()), '일 전')\n" +
                        "            END) AS time\n" +
                        "\n" +
                        "FROM chats as c1\n" +
                        "INNER JOIN product as p on p.productId = c1.productId\n" +
                        "INNER JOIN user_1 U on c1.userId = U.userId\n" +
                        "WHERE p.status = 'COMPLETE'\n" +
                        "GROUP BY p.productId\n" +
                        "ORDER BY productId";
                return this.jdbcTemplate.query(getCompProdsQuery,
                        (rs,rowNum) -> new GetCompProdRes(
                                rs.getInt("productId"),
                                rs.getString("productMainImg"),
                                rs.getString("productName"),
                                rs.getString("address"),
                                rs.getString("time"),
                                rs.getString("status"),
                                rs.getString("price"),
                                rs.getInt("comments"),
                                rs.getInt("Likes")));
            }

            public List<GetCompProdRes> getCompProdsByNickname(String nickname){
        String getCompProdByNickQuery = "SELECT p.productId, p.productName, CONCAT(price, '원')as price, productMainImg,\n" +
                "       U.address, p.status,p.Likes, count(*) as comments,\n" +
                "       (CASE\n" +
                "            WHEN TIMESTAMPDIFF(MINUTE, p.updateAt, NOW()) < 60 THEN CONCAT(TIMESTAMPDIFF(MINUTE, p.updateAt, NOW()), '분 전')\n" +
                "            WHEN TIMESTAMPDIFF(HOUR, p.updateAT, NOW()) < 24 THEN CONCAT(TIMESTAMPDIFF(HOUR, p.updateAt, NOW()), '시간 전')\n" +
                "            WHEN TIMESTAMPDIFF(DAY, p.updateAt, NOW()) <30 THEN CONCAT(TIMESTAMPDIFF(DAY, p.updateAt, NOW()), '일 전')\n" +
                "            END) AS time\n" +
                "\n" +
                "FROM chats as c1\n" +
                "INNER JOIN product as p on p.productId = c1.productId\n" +
                "INNER JOIN user_1 U on c1.userId = U.userId\n" +
                "WHERE U.nickname = ? AND p.status = 'COMPLETE'\n" +
                "GROUP BY p.productId\n" +
                "ORDER BY productId";
        String getComProdByNickParams = nickname;
        return this.jdbcTemplate.query(getCompProdByNickQuery,
                (rs,rowNum) -> new GetCompProdRes(
                        rs.getInt("productId"),
                        rs.getString("productMainImg"),
                        rs.getString("productName"),
                        rs.getString("address"),
                        rs.getString("time"),
                        rs.getString("status"),
                        rs.getString("price"),
                        rs.getInt("comments"),
                        rs.getInt("Likes")),
                getComProdByNickParams);
            }



    //12.내 동네 설정(동네 변경)
    public int changeUAddress(PatchUAddressReq patchUAddressReq){
        String changeUAddressQuery = "update user_1 set address =? where userId = ?";

        Object[]changeUAddressParams = new Object[]{patchUAddressReq.getAddress(), patchUAddressReq.getUserId()};

        return this.jdbcTemplate.update(changeUAddressQuery, changeUAddressParams);
    };





    }





