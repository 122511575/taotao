package com.taotao.service;

import com.taotao.pojo.LayuiResult;
import com.taotao.pojo.TaotaoResult;
import com.taotao.pojo.TbItem;

import java.util.Date;
import java.util.List;

public interface ItemService {
    TbItem findTbItemById(long itemId);

    LayuiResult findTbItemByPage(int page,int limit);

    TaotaoResult updateItem(List<TbItem> tbItem, int type, Date date);
}