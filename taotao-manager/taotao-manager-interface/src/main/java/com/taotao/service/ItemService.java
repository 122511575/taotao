package com.taotao.service;

import com.taotao.pojo.TbItem;

public interface ItemService {
    TbItem findTbItemById(long itemId);
}