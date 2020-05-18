package com.taotao.cart.controller;

import com.taotao.constant.RedisConstant;
import com.taotao.pojo.TaotaoResult;
import com.taotao.pojo.TbItem;
import com.taotao.service.ItemService;
import com.taotao.utils.CookieUtils;
import com.taotao.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private ItemService itemService;
    @RequestMapping("/add/{itemId}")
    public String cartSuccess(@PathVariable Long itemId, Integer num, HttpServletRequest request, HttpServletResponse response) {
        List<TbItem> cartList = getCartList(request);
        boolean hasItem = false;
        for (TbItem item: cartList){
            if (item.getId() == itemId.longValue()){
                item.setNum(item.getNum()+num);
                hasItem = true;
            }
        }
        if (!hasItem){
            TbItem tbItem = itemService.findTbItemById(itemId);
            String image = tbItem.getImage();
            if (StringUtils.isNotBlank(image)){
                String[] split = image.split("http");
                tbItem.setImage("http"+split[1]);
            }
            tbItem.setNum(num);
            cartList.add(tbItem);
        }
        CookieUtils.setCookie(request,response,RedisConstant.TT_CART,JsonUtils.objectToJson(cartList),RedisConstant.CART_EXPIRE,true);

        return "cartSuccess";
    }

    private List<TbItem> getCartList(HttpServletRequest request) {
        String json = CookieUtils.getCookieValue(request, RedisConstant.TT_CART, true);
        if (StringUtils.isNotBlank(json)) {
            List<TbItem> list = JsonUtils.jsonToList(json, TbItem.class);
            return list;
        }
        return new ArrayList<>();
    }
    @RequestMapping("/cart")
    public String showCart(Model model,HttpServletRequest request){
        List<TbItem> cartList = getCartList(request);
        model.addAttribute("cartList",cartList);
        int sum = 0;
        for (TbItem item: cartList){
            sum+=item.getNum();
        }
        model.addAttribute("sum",sum);
        return "cart";
    }
    @RequestMapping("/update/num/{itemId}/{num}")
    @ResponseBody
    public TaotaoResult updateNum(@PathVariable Long itemId,@PathVariable Integer num,
                                  HttpServletRequest request,HttpServletResponse response,Model model){
        List<TbItem> cartList = getCartList(request);
        for (TbItem tbItem: cartList){
            if (tbItem.getId() == itemId.longValue()){
                tbItem.setNum(num);
            }
        }
        int sum = 0;
        for (TbItem item: cartList){
            sum+=item.getNum();
        }
        model.addAttribute("sum",sum);
        CookieUtils.setCookie(request,response,RedisConstant.TT_CART,JsonUtils.objectToJson(cartList),RedisConstant.CART_EXPIRE,true);
        return TaotaoResult.ok();
    }
    @RequestMapping("/delete/{itemId}")
    public String deleteCartItem(@PathVariable Long itemId, HttpServletRequest request,HttpServletResponse response){
        List<TbItem> list = getCartList(request);
        for (int i = 0;i<list.size();i++){
            TbItem tbItem = list.get(i);
            if (tbItem.getId()==itemId.longValue()){
                list.remove(tbItem);
                break;
            }
        }
        CookieUtils.setCookie(request,response,RedisConstant.TT_CART,JsonUtils.objectToJson(list),RedisConstant.CART_EXPIRE,true);
        return "redirect:/cart/cart.html";
    }
}


