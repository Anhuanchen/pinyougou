package com.pinyougou.cart.controller;

import PageResult.InsertResult;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.pinyougou.cart.service.CartService;
import com.pinyougou.common.utils.CookieUtil;
import com.pinyougou.pojogroup.Cart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;


@RestController
@RequestMapping("/cart")
public class CartController {

    @Reference(timeout = 6000)
    private CartService cartService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HttpServletResponse response;

    /**
     * 购物车列表
     * @return
     */
    @RequestMapping("/findCartList")
    public List<Cart> findCartList(){
        //当前登陆人账号
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println("当前登录用户："+name);
        String cartListString = CookieUtil.getCookieValue(request, "cartList", "UTF-8");
        if (cartListString==null||cartListString.equals("")){
            cartListString="[]";
        }
        List<Cart> cart_cookie = JSON.parseArray(cartListString, Cart.class);

        if("anonymousUser".equals(name)){
            //未登录，从cookie中提取购物车
            System.out.println("从cookie中提取");

            return cart_cookie;
        }else{//已登录，从redis中读取数据
            System.out.println("从redis中提取");
            List<Cart> cartListFromRedis = cartService.findCartListFromRedis(name);
            if(cart_cookie.size()>0){
                //如果cookie里有数据，就合并
                List<Cart> cartList = cartService.mergeCartList(cart_cookie, cartListFromRedis);
                //合并后清除cookie里的数据
                CookieUtil.deleteCookie(request,response,"cartList");
                //将数据存入redis
                cartService.saveCartListToRedis(name,cartList);
                System.out.println("执行了合并购物车逻辑");
                return cartList;
            }
            return cartListFromRedis;
        }
    }

    /**
     * 添加商品到购物车
     * @param itemId
     * @param num
     * @return
     */
    @RequestMapping("/addGoodsToCartList")
    @CrossOrigin(origins = "http://localhost:9105",allowCredentials = "true")
    public InsertResult addGoodsToCartList(Long itemId,Integer num){
        //允许的跨域请求
//        response.setHeader("Access-Control-Allow-Origin","http://localhost:9105");
//        response.setHeader("Access-Control-Allow-Credentials", "true");//允许cookie
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println("当前登录用户："+name);
        try{
            List<Cart> cartList = findCartList();
            cartList = cartService.addGoodsToCartList(cartList, itemId, num);
            if (name.equals("anonymousUser")){
                //未登录，存入cookie
                CookieUtil.setCookie(request,response,"cartList",JSON.toJSONString(cartList),3600*24,"UTF-8");
                System.out.println("向cookie存存储数据");
            }else {
                //已登录，保存到redis
                cartService.saveCartListToRedis(name,cartList);
                System.out.println("向Redis中存储数据");
            }
            return new InsertResult(true,"添加成功");
        }catch (Exception e){
            e.printStackTrace();
            return new InsertResult(false,"添加失败");
        }
    }
}
