package com.example.demo.src.user_1;

import com.example.demo.config.BaseException;
import com.example.demo.config.secret.Secret;
import com.example.demo.src.user_1.model.*;
import com.example.demo.utils.AES128;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.*;

/**
 * Service란?
 * Controller에 의해 호출되어 실제 비즈니스 로직과 트랜잭션을 처리: Create, Update, Delete 의 로직 처리
 * 요청한 작업을 처리하는 관정을 하나의 작업으로 묶음
 * dao를 호출하여 DB CRUD를 처리 후 Controller로 반환
 */
@Service    // [Business Layer에서 Service를 명시하기 위해서 사용] 비즈니스 로직이나 respository layer 호출하는 함수에 사용된다.
// [Business Layer]는 컨트롤러와 데이터 베이스를 연결
public class user_1Service {
    final Logger logger = LoggerFactory.getLogger(this.getClass()); // Log 처리부분: Log를 기록하기 위해 필요한 함수입니다.

    // *********************** 동작에 있어 필요한 요소들을 불러옵니다. *************************
    private final User_1Dao userDao1;
    private final user_1Provider userProvider1;
    private final JwtService jwtService1; // JWT부분은 7주차에 다루므로 모르셔도 됩니다!


    @Autowired //readme 참고
    public user_1Service(User_1Dao userDao, user_1Provider userProvider, JwtService jwtService) {
        this.userDao1 = userDao;
        this.userProvider1 = userProvider;
        this.jwtService1 = jwtService; // JWT부분은 7주차에 다루므로 모르셔도 됩니다!

    }

    static int athentnum=1000; //암호화시작값
    static String pwd; //암호화 저장값
    // ******************************************************************************
//회원가입
    public Postuser_1Res createUser1(Postuser_1Req postUserReq1) throws BaseException {
        // 중복 확인: 해당 이메일을 가진 유저가 있는지 확인합니다. 중복될 경우, 에러 메시지를 보냅니다.
        //중복된 닉네임,주소,휴대폰번호 밸리데이션하기
        if(userProvider1.checkNickname(postUserReq1.getNickname()) == 1)
            throw new BaseException(POST_USERS_EXISTS_NICKNAME);

        String pw1 = Integer.toString(athentnum);
        try{
            pwd = new AES128(Secret.USER_INFO_PASSWORD_KEY).encrypt(pw1);
            athentnum+=1;
        }catch(Exception ignored) {
            throw new BaseException(AHTOURNUM_ENCRYPTION_ERROR);
        }
        // 암호화: postUserReq에서 제공받은 인증번호를 보안을 위해 암호화시켜 DB에 저장합니다.
        // ex) password123 -> dfhsjfkjdsnj4@!$!@chdsnjfwkenjfnsjfnjsd.fdsfaifsadjfjaf

        try {
            int userIdx = userDao1.createUser1(postUserReq1);
            return new Postuser_1Res(userIdx);
        } catch (Exception exception) { // DB에 이상이 있는 경우 에러 메시지를 보냅니다.
            throw new BaseException(DATABASE_ERROR);
        }
    }
        //이메일 추가, 수정
    public void modifyUserEmail(PatchUemailReq patchUemailReq)throws BaseException {
        try {
            int result = userDao1.modifyUemail(patchUemailReq);
            if (result == 0) { //수정 실패
                throw new BaseException(MODIFY_FAIL_USEREMAIL);
            }
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void modifyprofileImg(PatchprofImgReq patchprofImgReq)throws BaseException{
        try{
            int result = userDao1.modifyprofileImg(patchprofImgReq);
            if(result == 0){
                throw new BaseException(MODIFY_FAIL_PROFILEIMG);
            }
        }catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }

    };


    //12.내 동네 설정(동네 변경)
    public void changeUserAddress(PatchUAddressReq patchUAddressReq) throws BaseException{
        try{
            int result = userDao1.changeUAddress(patchUAddressReq);
            if(result == 0){
                throw new BaseException(CHANGE_FAIL_ADDRESS);
            }
        }catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    };

}