package com.taotao.service.impl;

import com.taotao.mapper.TbItemCatMapper;
import com.taotao.pojo.ItemCat;
import com.taotao.pojo.ItemCatResult;
import com.taotao.pojo.TbItemCat;
import com.taotao.pojo.ZtreeResult;
import com.taotao.service.ItemCatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ItemCatServiceImpl implements ItemCatService {
    @Autowired
    private TbItemCatMapper tbItemCatMapper;
    @Override
    public List<ZtreeResult> getZtreeResult(Long id) {
        List<TbItemCat> tbItemCats = tbItemCatMapper.findTbItemCatByParentId(id);
        List<ZtreeResult> results = new ArrayList<ZtreeResult>();
        for (TbItemCat tbItemCat: tbItemCats){
            ZtreeResult result = new ZtreeResult();
            result.setId(tbItemCat.getId());
            result.setName(tbItemCat.getName());
            result.setIsParent(tbItemCat.getIsParent());
            results.add(result);
        }
        return results;
    }

    @Override
    public ItemCatResult getItemCats() {
        ItemCatResult result = new ItemCatResult();
        result.setData(getItemCatList(0L));
        return result;
    }
    private List<?> getItemCatList(Long parentId){
        int count = 0;
        List list = new ArrayList();
        List<TbItemCat> tbItemCats = tbItemCatMapper.findTbItemCatByParentId(parentId);
        for (TbItemCat itemCat: tbItemCats){
            ItemCat item = new ItemCat();
            item.setUrl("/products/"+itemCat.getId()+".html");
            if (itemCat.getIsParent()){
                if (itemCat.getParentId() == 0){
                    item.setName("<a href='/products/"+itemCat.getId()+".html'>"+itemCat.getName()+"</a>");

                }else{
                    item.setName("<a href='/products/"+itemCat.getId()+".html'>"+itemCat.getName()+"</a>");
                }
                item.setItem(getItemCatList(itemCat.getId()));
                list.add(item);
                count++;
                if (parentId == 0 && count >=14){
                    break;
                }
            }else {
                list.add("/products/"+itemCat.getId()+".html|"+itemCat.getName());
            }
        }
        return list;
    }
}
