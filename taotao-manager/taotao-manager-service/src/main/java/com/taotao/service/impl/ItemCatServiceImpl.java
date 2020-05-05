package com.taotao.service.impl;


import com.taotao.mapper.TbItemCatMapper;
import com.taotao.pojo.ItemCat;
import com.taotao.pojo.ItemCatResult;
import com.taotao.pojo.TbItemCat;
import com.taotao.pojo.ZtreeResult;
import com.taotao.service.ItemCatService;
import com.taotao.service.JedisClient;
import com.taotao.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ItemCatServiceImpl implements ItemCatService {
    @Autowired
    private TbItemCatMapper tbItemCatMapper;
    @Autowired
    private JedisClient jedisClient;
    @Value("ITEMCAT")
    private String ITEMCAT;
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
        String json = jedisClient.get(ITEMCAT);
        if (StringUtils.isNotBlank(json)){
            List lists = JsonUtils.jsonToPojo(json,List.class);
            result.setData(lists);
            System.out.println("使用缓存");
            return result;
        }
        List list = getItemCatList(0L);
        result.setData(list);
        jedisClient.set(ITEMCAT,JsonUtils.objectToJson(list));
        System.out.println("使用数据库");
        return result;
    }
    private List getItemCatList(Long parentId){
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
