package com.taotao.constant;

public interface RedisConstant {
    String ITEM_INFO = "ITEM_INFO";
    String ITEM_DESC = "ITEM_DESC";
    String ITEM_PARAM = "ITEM_PARAM";
    Integer REDIS_TIME_OUT = 60*10;
    String USER_INFO = "USER_INFO";
    Integer USER_SESSION_EXPIRE = 60*60;
    Integer USER_SHORT_EXPIRE = 60*5;

    String TT_TOKEN = "TT_TOKEN";

    String TT_CART = "TT_CART";
    Integer CART_EXPIRE = 60*60*24*7;

    String SSO_LOGIN_URL = "http://localhost:8088/page/login";


    String ORDER_GEN_KEY = "ORDER_GEN_KEY";
    String ORDER_ID_BEGIN = "100544";
    String ORDER_ITEM_ID_GEN_KEY = "20190816";
}
