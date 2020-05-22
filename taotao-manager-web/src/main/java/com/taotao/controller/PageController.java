package com.taotao.controller;

import com.taotao.pojo.StatisticsResult;
import com.taotao.service.ItemCatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class PageController {
    @Autowired
    private ItemCatService itemCatService;
    @RequestMapping("/")
    public String showIndex(){
        return "index";
    }

//    @RequestMapping("/itemCat/statisticsItem")
//    @ResponseBody
//    public List<StatisticsResult> statisticsItem(){
//        List<StatisticsResult> results = itemCatService.getStatisticList();
//        return results;
//    }
}
