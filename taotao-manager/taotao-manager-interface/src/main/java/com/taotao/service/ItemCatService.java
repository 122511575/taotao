package com.taotao.service;

import com.taotao.pojo.ItemCatResult;
import com.taotao.pojo.ZtreeResult;

import java.util.List;

//商品分类服务
public interface ItemCatService {
    /**
     * 根据当前ID来作为参数查询这个分类下的子集
     * @param id 当前节点ID
     * @return Ztree需要的json格式
     */
    List<ZtreeResult> getZtreeResult(Long id);

    ItemCatResult getItemCats();
}
