package com.taotao.service;

import com.taotao.mapper.TbItemParamMapper;
import com.taotao.pojo.TaotaoResult;
import com.taotao.pojo.TbItemParamGroup;
import com.taotao.pojo.TbItemParamKey;
import com.taotao.service.ItemGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ItemGroupServiceImpl implements ItemGroupService {
    @Autowired
    private TbItemParamMapper tbItemParamMapper;
    @Override
    public TaotaoResult findTbItemGroupByCId(Long cId) {
        List<TbItemParamGroup> groups = tbItemParamMapper.findTbItemGroupByCId(cId);
        if (groups.size()<=0){
            return TaotaoResult.build(500,"该商品分类下没有商品规格参数");
        }
        for (TbItemParamGroup group: groups){
            List<TbItemParamKey> paramKeys = tbItemParamMapper.findTbItemParamKeyByGroupId(group.getId());
            group.setParamKeys(paramKeys);
        }
        return TaotaoResult.ok(groups);
    }

    @Override
    public TaotaoResult addGroup(Long cId, String params) {
        List<TbItemParamGroup> groups = new ArrayList<TbItemParamGroup>();
        String[] groupAndKey = params.split("clive");
        for (String str: groupAndKey){
            TbItemParamGroup group = new TbItemParamGroup();
            group.setItemCatId(cId);
            String[] key = str.split(",");
            group.setGroupName(key[0]);
            int k = tbItemParamMapper.addGroup(group);
            if (k<=0){
                return TaotaoResult.build(500,"添加商品规格参数失败");
            }
            List<TbItemParamKey> paramKeys = new ArrayList<TbItemParamKey>();
            for (int i = 1;i<key.length;i++){
                TbItemParamKey itemParamKey = new TbItemParamKey();
                itemParamKey.setParamName(key[i]);
                itemParamKey.setParamGroup(group);
                itemParamKey.setGroupId(group.getId());
                paramKeys.add(itemParamKey);
            }
            group.setParamKeys(paramKeys);
            int x = tbItemParamMapper.addGroupKey(paramKeys);
            if (x<=0){
                return TaotaoResult.build(500,"添加商品规格参数失败");
            }
        }
        return TaotaoResult.build(200,"添加商品规格参数成功");
    }
}
