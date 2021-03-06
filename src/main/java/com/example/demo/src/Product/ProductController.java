package com.example.demo.src.Product;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.Product.model.*;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.demo.src.Product.model.PostProRegReq;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;


@RestController

@RequestMapping("/app/products")



public class ProductController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired  // 객체 생성을 스프링에서 자동으로 생성해주는 역할. 주입하려 하는 객체의 타입이 일치하는 객체를 자동으로 주입한다.
    // IoC(Inversion of Control, 제어의 역전) / DI(Dependency Injection, 의존관계 주입)에 대한 공부하시면, 더 깊이 있게 Spring에 대한 공부를 하실 수 있을 겁니다!(일단은 모르고 넘어가셔도 무방합니다.)
    // IoC 간단설명,  메소드나 객체의 호출작업을 개발자가 결정하는 것이 아니라, 외부에서 결정되는 것을 의미
    // DI 간단설명, 객체를 직접 생성하는 게 아니라 외부에서 생성한 후 주입 시켜주는 방식
    private final ProductProvider productProvider;
    @Autowired
    private final ProductService productService;
    @Autowired
    private final JwtService jwtService; // JWT부분은 7주차에 다루므로 모르셔도 됩니다!

    public ProductController(ProductProvider productProvider, ProductService productService, JwtService jwtService) {
        this.productProvider = productProvider;
        this.productService = productService;
        this.jwtService = jwtService; // JWT부분은 7주차에 다루므로 모르셔도 됩니다!

    }


    //13. 상품 등록하기
    @ResponseBody
    @PostMapping("/register")
    public BaseResponse<PostProRegRes> registerProd(@RequestBody PostProRegReq postProRegReq) {
        if (postProRegReq.getProductName() == null) {
            return new BaseResponse<>(POST_PRODUCT_EMPTY_PRODUCTNAME);
        }
        try {
            PostProRegRes postProRegRes = productService.registerProd(postProRegReq);
            return new BaseResponse<>(postProRegRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    //14.해당상품에 거래 채팅보내기
    @ResponseBody
    @PostMapping("/chat-deal")
    public BaseResponse<PostProChatRes> createChatDeal(@RequestBody PostProChatReq postProChatReq) {

        if (postProChatReq.getChatDetail() == null) {
            return new BaseResponse<>(POST_PRODUCT_EMPTY_SENDCHAT);
        }
        try {
            PostProChatRes postProChatRes = productService.createChatDeal(postProChatReq);
            return new BaseResponse<>(postProChatRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    //15.해당상품 관심목록에 등록하기
    @ResponseBody
    @PostMapping("/register-Like")
    public BaseResponse<PostProLikeRes> register_Like(@RequestBody PostProLikeReq postProLikeReq) {
        if (postProLikeReq.getProductId() == 0) {
            return new BaseResponse<>(POST_PRODUCT_EMPTY_PRODUCTID);
        }
        try {
            PostProLikeRes postProLikeRes = productService.register_Like(postProLikeReq);
            return new BaseResponse<>(postProLikeRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    //16.유저의 판매중인 상품 수정
    @ResponseBody
    @PatchMapping("/{productId}")
    public BaseResponse<String> modifyProduct(@PathVariable("productId") int productId, @RequestBody
            Product product) {
        try {

            PatchProdReq patchProdReq = new PatchProdReq(productId, product.getProductMainImg(),
                    product.getProductName(), product.getCategoryId(), product.getPrice(),
                    product.getPrice_suggest(), product.getUse_ageId(), product.getStatus());
            productService.modifyProduct(patchProdReq);

            String result = "상품정보가 수정되었습니다.";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }


    //. 17판매중인 상품  삭제하기
    @ResponseBody
    @DeleteMapping("/delete/{productId}")
    public BaseResponse<String> deleteProd(@PathVariable("productId") int productId) {
        try {
            DeleteProdReq deleteProdReq1 = new DeleteProdReq(productId);
            productService.deleteProd(productId);

            String result = "해당 상품이 상품목록에서 삭제되었습니다.";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            exception.printStackTrace();
            return new BaseResponse<>(exception.getStatus());
        }


    }

    //18. 해당유저의 구매내역 조회
    @ResponseBody
    @GetMapping("/BuyList")
    public BaseResponse<List<GetBuyListRes>> getBuyList(@RequestParam(required = true)
                                                                int userId) {
        try {

            List<GetBuyListRes> getBuyListRes = productProvider.getBuyList(userId);
            return new BaseResponse<>(getBuyListRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }

    }


    //20. 해당 카테고리의 상품 조회
    @ResponseBody
    @GetMapping("")
    public BaseResponse<List<GetCatProRes>> getProByCategory(@RequestParam(required = true)
                                                             String category){
        try{
            List<GetCatProRes> getCatProRes = productProvider.getProByCategory(category);
            return new BaseResponse<>(getCatProRes);
        }catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }
}