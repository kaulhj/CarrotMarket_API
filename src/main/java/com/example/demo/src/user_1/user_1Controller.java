package com.example.demo.src.user_1;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.user_1.model.*;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.isRegexphonenum;


@RestController

@RequestMapping("/app/users")



public class user_1Controller {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired  // 객체 생성을 스프링에서 자동으로 생성해주는 역할. 주입하려 하는 객체의 타입이 일치하는 객체를 자동으로 주입한다.
    // IoC(Inversion of Control, 제어의 역전) / DI(Dependency Injection, 의존관계 주입)에 대한 공부하시면, 더 깊이 있게 Spring에 대한 공부를 하실 수 있을 겁니다!(일단은 모르고 넘어가셔도 무방합니다.)
    // IoC 간단설명,  메소드나 객체의 호출작업을 개발자가 결정하는 것이 아니라, 외부에서 결정되는 것을 의미
    // DI 간단설명, 객체를 직접 생성하는 게 아니라 외부에서 생성한 후 주입 시켜주는 방식
    private final user_1Provider userProvider1;
    @Autowired
    private final user_1Service userService1;
    @Autowired
    private final JwtService jwtService; // JWT부분은 7주차에 다루므로 모르셔도 됩니다!

    public user_1Controller(user_1Provider userProvider, user_1Service userService, JwtService jwtService) {
        this.userProvider1 = userProvider;
        this.userService1 = userService;
        this.jwtService = jwtService; // JWT부분은 7주차에 다루므로 모르셔도 됩니다!

    }

    //1.당근마켓 회원가입
    @ResponseBody
    @PostMapping("/sign-up1")
    public BaseResponse<Postuser_1Res> createUser(@RequestBody Postuser_1Req postUserReq1) {
        //  @RequestBody란, 클라이언트가 전송하는 HTTP Request Body(우리는 JSON으로 통신하니, 이 경우 body는 JSON)를 자바 객체로 매핑시켜주는 어노테이션
        // TODO: email 관련한 짧은 validation 예시입니다. 그 외 더 부가적으로 추가해주세요!
        // email에 값이 존재하는지, 빈 값으로 요청하지는 않았는지 검사합니다. 빈값으로 요청했다면 에러 메시지를 보냅니다.

        if (postUserReq1.getNickname() == null) {
            return new BaseResponse<>(POST_USERS_EMPTY_NICKNAME);
        }

        //폰번호 정규식
        if (!isRegexphonenum(postUserReq1.getPhonenum())) {
            return new BaseResponse<>(POST_USERS_INVALID_PHONENUM);
        }


        try {
            Postuser_1Res postUserRes1 = userService1.createUser1(postUserReq1);
            return new BaseResponse<>(postUserRes1);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    //2. 로그인
    @ResponseBody
    @PostMapping("/log-in")
    public BaseResponse<PostLoginRes1> login(@RequestBody PostLoginReq1 postLoginReq1) {
        //
        if (postLoginReq1.getPhonenum() == null) {
            return new BaseResponse<>(POST_USERS_EMPTY_PHONENUM);
        }
        if (!isRegexphonenum(postLoginReq1.getPhonenum())) {
            return new BaseResponse<>(POST_USERS_INVALID_PHONENUM);
        }
        try {
            PostLoginRes1 postLoginRes1 = userProvider1.login(postLoginReq1);
            return new BaseResponse<>(postLoginRes1);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }

    }

    @ResponseBody
    @PatchMapping("/useremail/{userId}")  //3. 이메일 등록 혹은 수정
    public BaseResponse<String> modifyEmail(@PathVariable("userId") int userId, @RequestBody user_1 user1) {

        //이메일이 널값인지 검사
        if (user1.getUseremail() == null) {
            return new BaseResponse<>(POST_USERS_EMPTY_EMAIL);
        }

        try {
            int userIdxByJwt = jwtService.getUserIdx();
            if(userId !=userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            PatchUemailReq patchUemailReq = new PatchUemailReq(userId, user1.getUseremail());
            userService1.modifyUserEmail(patchUemailReq);

            String result = "이메일이 수정(추가) 되었습니다. ";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }


    }

    @ResponseBody
    @PatchMapping("/profileImg/{userId}")  //4. 프로필사진 등록 혹은 수정
    public BaseResponse<String> modifyprofileImg(@PathVariable("userId") int userId, @RequestBody user_1 user1) {

        //이메일이 널값인지 검사
        if (user1.getProfileImg() == null) {
            return new BaseResponse<>(POST_USERS_EMPTY_PROFILEIMG);
        }

        try {
            PatchprofImgReq patchprofImgReq = new PatchprofImgReq(userId, user1.getProfileImg());
            userService1.modifyprofileImg(patchprofImgReq);

            String result = "프로필 수정(추가) 되었습니다. ";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }


    }

    //5. 회원 1명 조회
    @ResponseBody
    @GetMapping("/{userId}")
    public BaseResponse<Getuser_1Res> getUser(@PathVariable("userId") int userId) {
        try {
            Getuser_1Res getuser1Res = userProvider1.getUser(userId);
            return new BaseResponse<>(getuser1Res);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    //6.7 해당주소 유저들 조회 or 전체 유저 조회
    @ResponseBody
    @GetMapping("")
    public BaseResponse<List<Getuser_1Res>> getUsers(@RequestParam(required = false)
                                                             String address) {
        try {
            if (address == null) { //전체유저조회
                List<Getuser_1Res> getuser_1Res = userProvider1.getUsers();
                return new BaseResponse<>(getuser_1Res);
            }

            List<Getuser_1Res> getuser_1Res = userProvider1.getUsersByAddress(address);
            return new BaseResponse<>(getuser_1Res); //특정닉네임 유저 조회
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }

    }

    //8. 검색된 지역의 업로드된 상품정보 추출
    @ResponseBody
    @GetMapping("/home")
    public BaseResponse<List<GetHomeInfoRes>> getHomeInfo(@RequestParam(required = true)
                                                                  String address) {
        if (address == null)
            return new BaseResponse<>(EMPTY_ADDRESS);
        try {
            List<GetHomeInfoRes> getHomeInfoRes = userProvider1.getHomeInfoByAdd(address);
            return new BaseResponse<>(getHomeInfoRes);

        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));


        }
    }

    //9.특정상품의 상세정보 조회
    @ResponseBody
    @GetMapping("/{productName}/detail")
    public BaseResponse<GetProdetailRes> getproductdetail(@PathVariable("productName") String prodname) {
        try {
            GetProdetailRes getProdetailRes = userProvider1.getProDetail(prodname);
            return new BaseResponse<>(getProdetailRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }


    //10,11 전체 거래 완료, 특정유저의 거래완료 상품 추출
    @ResponseBody
    @GetMapping("/product")
    public BaseResponse<List<GetCompProdRes>> getCompletProd(@RequestParam(required = false) String nickname) {

        try {
            if(nickname == null) {
                List<GetCompProdRes> getCompProdsRes = userProvider1.getCompleteProd();
                return new BaseResponse<>(getCompProdsRes);
            }
            List<GetCompProdRes> getCompProdsRes = userProvider1.getCompProdByNickname(nickname);
            return new BaseResponse<>(getCompProdsRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    //12.내 동네 설정(동네 변경)
    @ResponseBody
    @PatchMapping("/{userId}/address/change")
    public BaseResponse<String> changeAddress(@PathVariable("userId") int userId,
                                              @RequestBody user_1 user1){
        try{
            PatchUAddressReq patchUAddressReq = new PatchUAddressReq(userId, user1.getAddress());
            userService1.changeUserAddress(patchUAddressReq);

            String result = "동네가 변경되었습니다.";
            return new BaseResponse<>(result);
        }catch (BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @DeleteMapping("/delete/{userId}")
    public BaseResponse<String> deleteUser(@PathVariable("userId") int userId){
    try{
        userService1.deleteUser(userId);

        String result = "회원탈퇴가 성공적으로 진행되었습니다.";
        return new BaseResponse<>(result);
    }catch (BaseException exception){
        exception.printStackTrace();
        return new BaseResponse<>(exception.getStatus());
        }
    }

}


