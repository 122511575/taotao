package com.taotao.service;

import com.taotao.pojo.*;

import java.util.Date;
import java.util.List;

//商品服务
public interface ItemService {
    TbItem findTbItemById(long itemId);

    LayuiResult findTbItemByPage(int page,int limit);

    TaotaoResult updateItem(List<TbItem> tbItem, int type, Date date);

    LayuiResult getLikeItem(Integer page, Integer limit, String title, Integer priceMin, Integer priceMax, Long cid);

    PictureResult addPicture(String filename, byte[] bytes);

    TaotaoResult addItem(TbItem tbItem, String itemDesc, List<Integer> paramKeyIds, List<String> paramValue);

    TbItemDesc findTbItemDescByItemId(Long itemId);

    List<TbItemParamGroup> findTbItemGroupByItemId(Long itemId);
}