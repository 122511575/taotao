package com.taotao.service;

import com.taotao.constant.FTPconstant;
import com.taotao.constant.RedisConstant;
import com.taotao.mapper.TbItemDescMapper;
import com.taotao.mapper.TbItemMapper;
import com.taotao.mapper.TbItemParamMapper;
import com.taotao.pojo.*;
import com.taotao.service.ItemService;
import com.taotao.service.JedisClient;
import com.taotao.utils.FtpUtil;
import com.taotao.utils.IDUtils;
import com.taotao.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import javax.jms.*;
import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {
    @Autowired
    private TbItemMapper tbItemMapper;
    @Autowired
    private TbItemDescMapper tbItemDescMapper;
    @Autowired
    private JmsTemplate jmsTemplate;
    @Autowired
    private Destination destination;
    @Autowired
    private JedisClient jedisClient;
    @Autowired
    private TbItemParamMapper tbItemParamMapper;

    @Override
    public TbItem findTbItemById(long itemId) {
        int rand = (int) (Math.random()*800)+1;
        String json = jedisClient.get(RedisConstant.ITEM_INFO+":"+itemId);
        if (StringUtils.isNotBlank(json)){
            if (json.equals("null")){
                return null;
            }else {
                TbItem tbItem = JsonUtils.jsonToPojo(json, TbItem.class);
                jedisClient.expire(RedisConstant.ITEM_INFO+":"+itemId,RedisConstant.REDIS_TIME_OUT+rand);
                return tbItem;
            }
        }


        TbItem tbItem = tbItemMapper.findTbItemById(itemId);
        if (tbItem==null){
            jedisClient.set(RedisConstant.ITEM_INFO+":"+itemId,"null");
            jedisClient.expire(RedisConstant.ITEM_INFO+":"+itemId,RedisConstant.REDIS_TIME_OUT);
        }else {
            jedisClient.set(RedisConstant.ITEM_INFO+":"+itemId, JsonUtils.objectToJson(tbItem));
            jedisClient.expire(RedisConstant.ITEM_INFO+":"+itemId,RedisConstant.REDIS_TIME_OUT+rand);
        }
        return tbItem;
    }

    @Override
    public LayuiResult findTbItemByPage(int page, int limit) {
        LayuiResult result = new LayuiResult();
        result.setCode(0);
        result.setMsg("");
        int count = tbItemMapper.findTbItemByCount();
        result.setCount(count);
        List<TbItem> data = tbItemMapper.findTbItemByPage((page-1)*limit,limit);
        result.setData(data);
        return result;
    }

    @Override
    public TaotaoResult updateItem(List<TbItem> tbItems, int type, Date date) {
        if (tbItems.size()<=0){
            return TaotaoResult.build(500,"请先勾选，再操作",null);
        }
        List<Long> ids = new ArrayList<Long>();
        for (TbItem tbItem: tbItems) {
            ids.add(tbItem.getId());
        }
        int count = tbItemMapper.updateItemByIds(ids,type,date);
        //count>0才代表我们修改了数据库里面的数据
        if (count>0&&type==0){
            return TaotaoResult.build(200,"商品下架成功",null);
        }else if (count>0&&type==1){
            return TaotaoResult.build(200,"商品上架成功",null);
        }else if (count>0&&type==2){
            return TaotaoResult.build(200,"商品删除成功",null);
        }
        return TaotaoResult.build(500,"商品修改失败",null);

    }

    @Override
    public LayuiResult getLikeItem(Integer page, Integer limit, String title, Integer priceMin, Integer priceMax, Long cid) {
        if (priceMin==null){
            priceMin = 0;
        }
        if (priceMin <0 ){
            priceMin = 0;
        }
        if (priceMax==null){
            priceMax = 10000000;
        }
        if (priceMax>10000000){
            priceMax = 10000000;
        }
        LayuiResult result = new LayuiResult();
        result.setCode(0);
        result.setMsg("");
        int count = tbItemMapper.findTbItemByLikeCount(title,priceMin,priceMax,cid);
        result.setCount(count);
        List<TbItem> data= tbItemMapper.findTbItemLike(title,priceMin,priceMax,cid,(page-1)*limit,limit);
        result.setData(data);
        return result;
    }

    @Override
    public PictureResult addPicture(String filename, byte[] bytes) {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        String filePath = simpleDateFormat.format(date);
        String fileName = IDUtils.genImageName()+filename.substring(filename.lastIndexOf(".png"));
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        boolean b = FtpUtil.uploadFile(FTPconstant.FTP_ADDRESS, FTPconstant.FTP_PORT, FTPconstant.FTP_USERNAME, FTPconstant.FTP_PASSWORD, FTPconstant.FILI_UPLOAD_PATH, filePath, fileName, bis);
        if (b == true){
            PictureResult result = new PictureResult();
            result.setCode(0);
            result.setMsg("");
            PictureData data = new PictureData();
            data.setSrc(FTPconstant.IMAGE_BASE_URL+"/"+filePath+"/"+fileName);
            result.setData(data);
            System.out.println(data.getSrc());
            return result;
        }
        return null;
    }

    @Override
    public TaotaoResult addItem(TbItem tbItem, String itemDesc, List<Integer> paramKeyIds, List<String> paramValue) {
        final Long itemId = IDUtils.genItemId();
        Date date = new Date();
        tbItem.setId(itemId);
        tbItem.setStatus((byte)1);
        tbItem.setCreated(date);
        tbItem.setUpdated(date);

        int i = tbItemMapper.addItem(tbItem);
        if (i<=0){
            return TaotaoResult.build(500,"添加商品基本信息失败");
        }
        TbItemDesc tbItemDesc = new TbItemDesc();
        tbItemDesc.setItemId(itemId);
        tbItemDesc.setCreated(date);
        tbItemDesc.setUpdated(date);
        tbItemDesc.setItemDesc(itemDesc);
        int j = tbItemDescMapper.addItemDesc(tbItemDesc);
        if (j<=0){
            return TaotaoResult.build(500,"添加商品描述信息失败");
        }

        for (int q = 0;q<paramKeyIds.size();q++){
            TbItemParamValue tbItemParamValue = new TbItemParamValue();
            tbItemParamValue.setItemId(itemId);
            tbItemParamValue.setParamId(paramKeyIds.get(q));
            tbItemParamValue.setParamValue(paramValue.get(q));
            int w = tbItemParamMapper.addGroupValue(tbItemParamValue);
            if (w<=0){
                return TaotaoResult.build(500,"添加商品规格参数失败");

            }
        }


        jmsTemplate.send(destination, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                TextMessage textMessage = session.createTextMessage();
                textMessage.setText(itemId+"");
                return textMessage;
            }
        });

        return TaotaoResult.build(200,"添加商品成功");
    }

    @Override
    public TbItemDesc findTbItemDescByItemId(Long itemId) {
        int rand = (int) (Math.random()*800)+1;
        String json = jedisClient.get(RedisConstant.ITEM_DESC+":"+itemId);
        if (StringUtils.isNotBlank(json)){
            if (json.equals("null")){
                return null;
            }else {
                TbItemDesc itemDesc = JsonUtils.jsonToPojo(json, TbItemDesc.class);
                jedisClient.expire(RedisConstant.ITEM_DESC+":"+itemId,RedisConstant.REDIS_TIME_OUT+rand);
                return itemDesc;
            }
        }

        TbItemDesc itemDesc = tbItemDescMapper.findTbItemDescByItemId(itemId);

        if (itemDesc==null){
            jedisClient.set(RedisConstant.ITEM_DESC+":"+itemId,"null");
            jedisClient.expire(RedisConstant.ITEM_DESC+":"+itemId,RedisConstant.REDIS_TIME_OUT);
        }else {
            jedisClient.set(RedisConstant.ITEM_DESC+":"+itemId, JsonUtils.objectToJson(itemDesc));
            jedisClient.expire(RedisConstant.ITEM_DESC+":"+itemId,RedisConstant.REDIS_TIME_OUT+rand);
        }
        return itemDesc;
    }

    @Override
    public List<TbItemParamGroup> findTbItemGroupByItemId(Long itemId) {
        int rand = (int) (Math.random()*800)+1;
        String json = jedisClient.get(RedisConstant.ITEM_PARAM+":"+itemId);
        if (StringUtils.isNotBlank(json)){
            if (json.equals("null")){
                return null;
            }else {
                List<TbItemParamGroup> itemGroup = JsonUtils.jsonToPojo(json, List.class);
                jedisClient.expire(RedisConstant.ITEM_PARAM,RedisConstant.REDIS_TIME_OUT+rand);
                return itemGroup;
            }
        }
        List<TbItemParamGroup> itemGroup = tbItemParamMapper.findTbItemGroupByItemId(itemId);
        if (itemGroup==null){
            jedisClient.set(RedisConstant.ITEM_PARAM+":"+itemId,"null");
            jedisClient.expire(RedisConstant.ITEM_PARAM+":"+itemId,RedisConstant.REDIS_TIME_OUT);
        }else {
            jedisClient.set(RedisConstant.ITEM_PARAM+":"+itemId, JsonUtils.objectToJson(itemGroup));
            jedisClient.expire(RedisConstant.ITEM_PARAM+":"+itemId,RedisConstant.REDIS_TIME_OUT+rand);
        }
        return itemGroup;
    }
}
