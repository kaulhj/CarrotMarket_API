package com.example.demo.src.user_1;

import com.example.demo.config.BaseException;
import com.example.demo.src.user_1.model.*;
import com.example.demo.utils.JwtService;
import org.hibernate.query.criteria.internal.BasicPathUsageException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

//Provider : Read의 비즈니스 로직 처리
@Service    // [Business Layer에서 Service를 명시하기 위해서 사용] 비즈니스 로직이나 respository layer 호출하는 함수에 사용된다.
// [Business Layer]는 컨트롤러와 데이터 베이스를 연결
/**
 * Provider란?
 * Controller에 의해 호출되어 실제 비즈니스 로직과 트랜잭션을 처리: Read의 비즈니스 로직 처리
 * 요청한 작업을 처리하는 관정을 하나의 작업으로 묶음
 * dao를 호출하여 DB CRUD를 처리 후 Controller로 반환
 */
public class user_1Provider {


    // *********************** 동작에 있어 필요한 요소들을 불러옵니다. *************************
    private final User_1Dao userDao1;
    private final JwtService jwtService; // JWT부분은 7주차에 다루므로 모르셔도 됩니다!


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired //readme 참고
    public user_1Provider(User_1Dao userDao, JwtService jwtService) {
        this.userDao1 = userDao;
        this.jwtService = jwtService; // JWT부분은 7주차에 다루므로 모르셔도 됩니다!
    }


    public int checkEmail(String email) throws BaseException {
        try {
            return userDao1.checkEmail1(email);
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkNickname(String nickname) throws BaseException {
        try {
            return userDao1.checkNickname(nickname);
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public PostLoginRes1 login(PostLoginReq1 postLoginReq1) throws BaseException {
        try {
            user_1 user1 = userDao1.getphonenum(postLoginReq1);
        } catch (Exception ignored) {
            throw new BaseException(INVALID_PHONENUM);
        }

        user_1 user1 = userDao1.getAuth(postLoginReq1); //저장되있는 인증번호
        String auth_input;    //입력할 인증번호
        /*
        try{
            auth_input = new AES128(Secret.USER_INFO_PASSWORD_KEY).decrypt(user1.getAuthentication());
        }catch(Exception ignored){
            throw new BaseException(AUTH_DECRYPTION_ERROR);
        }*/
        if (postLoginReq1.getAuthnum().equals(user1.getAuthnum())) {
            int userIdx = userDao1.getAuth(postLoginReq1).getUserId();
            return new PostLoginRes1(userIdx);
        } else {
            throw new BaseException(FAILED_TO_LOGIN);
        }
    }

    public Getuser_1Res getUser(int userId) throws BaseException {
        try {
            Getuser_1Res getuser1Res = userDao1.getUser(userId);
            return getuser1Res;
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }


    //go
    public List<Getuser_1Res> getUsersByAddress(String address) throws BaseException {
        try {
            List<Getuser_1Res> getuser_1Res = userDao1.getUsersByAddress(address);
            return getuser_1Res;
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }

    }

    public List<Getuser_1Res> getUsers() throws BaseException {
        try {
            List<Getuser_1Res> getuser_1Res = userDao1.getUsers();
            return getuser_1Res;
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //주소정보에 맞는 정보들 불러오기
    public List<GetHomeInfoRes> getHomeInfoByAdd(String address)throws BaseException {
        try {
            List<GetHomeInfoRes> getHomeInfoRes = userDao1.getHomeInfoByAdd(address);
            return getHomeInfoRes;
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
        }

    //특정 상품의 상세정보 조회
    public GetProdetailRes getProDetail(String prodname) throws BaseException{
        try{
            GetProdetailRes getProdetailRes = userDao1.getProDetail(prodname);
            return getProdetailRes;
        }catch (Exception exception){
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }


    //완료된 상품들 추출
    public List<GetCompProdRes> getCompleteProd() throws BaseException{
        try{
            List<GetCompProdRes> getCompProdRes = userDao1.getCompProds();
            return getCompProdRes;
        }catch(Exception exception){
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetCompProdRes> getCompProdByNickname(String nickname) throws BaseException{
        try{
            List<GetCompProdRes> getCompProdRes = userDao1.getCompProdsByNickname(nickname);
            return getCompProdRes;
        }catch (Exception exception){
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    }
