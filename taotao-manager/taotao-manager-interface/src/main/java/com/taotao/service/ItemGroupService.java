package com.taotao.service;

import com.taotao.pojo.TaotaoResult;

public interface ItemGroupService {
    TaotaoResult findTbItemGroupByCId(Long CId);

    TaotaoResult addGroup(Long cId, String params);
}
