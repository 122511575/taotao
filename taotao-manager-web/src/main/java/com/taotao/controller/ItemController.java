package com.taotao.controller;

import com.taotao.pojo.*;
import com.taotao.service.ItemService;
import com.taotao.utils.FtpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.*;
import java.io.IOException;
import java.util.Date;
import java.util.List;


@Controller
@RequestMapping("/item")
public class ItemController {
    @Autowired
    private ItemService itemService;
    @RequestMapping("/{itemId}")
    @ResponseBody
    public TbItem findTbItem(@PathVariable Long itemId){
        TbItem result = itemService.findTbItemById(itemId);
        return result;
    }

    @RequestMapping("/showItemPage")
    @ResponseBody
    public LayuiResult showItemPage(Integer page,Integer limit){
    LayuiResult result = itemService.findTbItemByPage(page,limit);
    System.out.println(result);
    return result;
    }

    @RequestMapping("/itemDelete")
    @ResponseBody
    public TaotaoResult itemDelete(@RequestBody List<TbItem> tbItem){
        Date date = new Date();
        TaotaoResult result = itemService.updateItem(tbItem,2,date);
        return result;
    }

    @RequestMapping("/commodityUpperShelves")
    @ResponseBody
    public TaotaoResult commodityUpperShelves(@RequestBody List<TbItem> tbItem){
        Date date = new Date();
        TaotaoResult result = itemService.updateItem(tbItem,1,date);
        return result;
    }

    @RequestMapping("/commodityLowerShelves")
    @ResponseBody
    public TaotaoResult commodityLowerShelves(@RequestBody List<TbItem> tbItem){
        Date date = new Date();
        TaotaoResult result = itemService.updateItem(tbItem,0,date);
        return result;
    }

    @RequestMapping("/searchItem")
    @ResponseBody
    public LayuiResult searchItem(Integer page,Integer limit,String title,Integer priceMin,Integer priceMax,Long cId){
        LayuiResult result = itemService.getLikeItem(page,limit,title,priceMin,priceMax,cId);
        return result;
    }

    @RequestMapping("/fileUpload")
    @ResponseBody
    public PictureResult fileUpload(MultipartFile file){
        String filename = file.getOriginalFilename();
        try {
            byte[] bytes = file.getBytes();
            PictureResult result = itemService.addPicture(filename,bytes);
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping("/addItem")
    @ResponseBody
    public TaotaoResult addItem(TbItem tbItem,String itemDesc){
        TaotaoResult result = itemService.addItem(tbItem,itemDesc);
        return result;
    }

}
