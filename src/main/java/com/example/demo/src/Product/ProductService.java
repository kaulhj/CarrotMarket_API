package com.example.demo.src.Product;

import com.example.demo.config.BaseException;
import com.example.demo.config.secret.Secret;
import com.example.demo.src.Product.*;
import com.example.demo.src.Product.model.*;
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
public class ProductService {
    final Logger logger = LoggerFactory.getLogger(this.getClass()); // Log 처리부분: Log를 기록하기 위해 필요한 함수입니다.

    // *********************** 동작에 있어 필요한 요소들을 불러옵니다. *************************
    private final ProductDao productDao;
    private final ProductProvider productProvider;
    private final JwtService jwtService1; // JWT부분은 7주차에 다루므로 모르셔도 됩니다!


    @Autowired //readme 참고
    public ProductService(ProductDao productDao, ProductProvider productProvider, JwtService jwtService) {
        this.productDao = productDao;
        this.productProvider = productProvider;
        this.jwtService1 = jwtService; // JWT부분은 7주차에 다루므로 모르셔도 됩니다!

    }


    public PostProRegRes registerProd(PostProRegReq postProRegReq) throws BaseException{
        if(productProvider.checkProd( postProRegReq) == 1){
            throw new BaseException(POST_PRODUCT__ALREADY_REGISTERED);
        }
        try{
            int productId = productDao.registerProd(postProRegReq);
            return new PostProRegRes(productId);
        }catch (Exception exception){
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public PostProChatRes createChatDeal(PostProChatReq postProChatReq)throws BaseException{
        try{
            int chatId = productDao.createChatDeal(postProChatReq);
            return new PostProChatRes(chatId);
        }catch(Exception exception){
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public PostProLikeRes register_Like(PostProLikeReq postProLikeReq)throws BaseException{
        try{
            String reg_LikeText = productDao.registerLike(postProLikeReq);
            return new PostProLikeRes(reg_LikeText);
        }catch (Exception exception){
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void modifyProduct(PatchProdReq patchProdReq)throws BaseException{
        try{
            int result = productDao.modifyProduct(patchProdReq);
            if(result == 0){
                throw new BaseException(MODIFY_FAIL_PRODUCT);
            }
        }catch (Exception exception){
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void deleteProd(int productId) throws BaseException{
        try{
            int result = productDao.deleteProd(productId);
            if(result != productId){
                throw new BaseException(DELETE_FAIL_PRODUCT);
            }
        }catch (Exception exception){
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }


}
