package app.preciojusto.application.exceptions;

import lombok.Getter;

public enum ApplicationExceptionCode {
    UNKNOWN_ERROR(-1),
    BADREQUEST_ERROR(-2),
    UNAUTHORIZED_ERROR(-3),
    BADGATEWAY_ERROR(-4),


    //////////////////////////////
    /////    API PRODUCTS    /////
    //////////////////////////////

    NOT_FOUND_ERROR(0),
    BRAND_NOT_FOUND_ERROR(1),
    CATEGORY_NOT_FOUND_ERROR(2),
    PACK_NOT_FOUND_ERROR(3),
    RECIPE_NOT_FOUND_ERROR(4),
    SUPERMARKET_NOT_FOUND_ERROR(5),
    CONTAINER_NOT_FOUND_ERROR(6),
    SUPERMARKETPRODUCT_NOT_FOUND_ERROR(7),
    PRODUCT_NOT_FOUND_ERROR(8),
    FOODPRODUCT_NOT_FOUND_ERROR(9),
    OFFER_NOT_FOUND_ERROR(10),
    OFFERPERCENTAGE_NOT_FOUND_ERROR(11),
    OFFERUNIT_NOT_FOUND_ERROR(12),
    OFFERUNITPERCENTAGE_NOT_FOUND_ERROR(13),
    OFFERUNKNOWN_NOT_FOUND_ERROR(14),
    OFFERUNITPLAINPRICE_NOT_FOUND_ERROR(15),
  
    ALREADY_EXISTS_ERROR(100),
    BRAND_ALREADY_EXISTS_ERROR(101),
    CATEGORY_ALREADY_EXISTS_ERROR(102),
    PACK_ALREADY_EXISTS_ERROR(103),
    RECIPE_ALREADY_EXISTS_ERROR(104),
    SUPERMARKET_ALREADY_EXISTS_ERROR(105),
    CONTAINER_ALREADY_EXISTS_ERROR(106),
    SUPERMARKETPRODUCT_ALREADY_EXISTS_ERROR(107),
    PRODUCT_ALREADY_EXISTS_ERROR(108),
    FOODPRODUCT_ALREADY_EXISTS_ERROR(109),
    OFFER_ALREADY_EXISTS_ERROR(110),
    OFFERPERCENTAGE_ALREADY_EXISTS_ERROR(111),
    OFFERUNIT_ALREADY_EXISTS_ERROR(112),
    OFFERUNITPERCENTAGE_ALREADY_EXISTS_ERROR(113),
    OFFERUNKNOWN_ALREADY_EXISTS_ERROR(114),
    OFFERUNITPLAINPRICE_ALREADY_EXISTS_ERROR(115),
  
    NO_CONTENT_ERROR(200),
    BRAND_NO_CONTENT_ERROR(201),
    CATEGORY_NO_CONTENT_ERROR(202),
    OFFER_NO_CONTENT_ERROR(203),
    PACK_NO_CONTENT_ERROR(204),
    RECIPE_NO_CONTENT_ERROR(205),
    SUPERMARKET_NO_CONTENT_ERROR(206),
    CONTAINER_NO_CONTENT_ERROR(207),
    SUPERMARKETPRODUCT_NO_CONTENT_ERROR(208),
    PRODUCT_NO_CONTENT_ERROR(209),
    FOODPRODUCT_NO_CONTENT_ERROR(210),
    OFFERPERCENTAGE_NO_CONTENT_ERROR(211),
    OFFERUNIT_NO_CONTENT_ERROR(212),
    OFFERUNITPERCENTAGE_NO_CONTENT_ERROR(213),
    OFFERUNKNOWN_NO_CONTENT_ERROR(214),
    OFFERUNITPLAINPRICE_NO_CONTENT_ERROR(215),

    ///////////////////////////////
    //////  API APPLICATION  //////
    ///////////////////////////////
    USER_NOT_FOUND_ERROR(50),
    IMAGE_NOT_FOUND_ERROR(51),
    SHOPPINGCART_NOT_FOUND_ERROR(52),
    SHOPPINGCARTPRODUCT_NOT_FOUND_ERROR(53),


    USER_ALREADY_EXISTS_ERROR(150),
    IMAGE_ALREADY_EXISTS_ERROR(151),
    SHOPPINGCART_ALREADY_EXISTS_ERROR(152),
    SHOPPINGCARTPRODUCT_ALREADY_EXISTS_ERROR(153),

    USER_NO_CONTENT_ERROR(250),
    IMAGE_NO_CONTENT_ERROR(251),
    SHOPPINGCART_NO_CONTENT_ERROR(252),
    SHOPPINGCARTPRODUCT_NO_CONTENT_ERROR(253),

    WRONG_EMAIL_ERROR(300),
    WRONG_PASSWORD_ERROR(301),
    EMAIL_VALIDATION_ERROR(302),
    EMAILEXISTS_VALIDATION_ERROR(303),
    USER_VALIDATION_ERROR(304),
    PASSWORD_VALIDATION_ERROR(305),
    SAMEPASSWORD_VALIDATION_ERROR(306),
    PHONENUMBER_VALIDATION_ERROR(307),
    USER_NATIVE_ERROR(308),
    USER_INACTIVE_ERROR(309),

    GOOGLE_SERVICE_ERROR(310),
    TWITTER_SERVICE_ERROR(311),
    FACEBOOK_SERVICE_ERRROR(312);

    @Getter
    private final int code;

    ApplicationExceptionCode(int code) {
        this.code = code;
    }
}
