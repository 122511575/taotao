package com.taotao.sso.service;

import com.taotao.pojo.TaotaoResult;
import com.taotao.pojo.TbUser;

public interface UserService {
    TaotaoResult getCheckDateResult(String param, Integer type);

    TaotaoResult addUser(TbUser tbUser);

    TaotaoResult getTokenByNameAndPass(String userName, String passWord);

    TaotaoResult getUserByToken(String token);

    TaotaoResult deleteToken(String token);
}
