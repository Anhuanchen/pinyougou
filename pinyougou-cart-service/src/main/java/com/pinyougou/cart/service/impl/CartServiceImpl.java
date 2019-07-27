package com.pinyougou.cart.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.cart.service.CartService;
import com.pinyougou.mapper.TbItemMapper;
import com.pinyougou.mapper.TbOrderItemMapper;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.pojo.TbOrderItem;
import com.pinyougou.pojogroup.Cart;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private TbItemMapper tbItemMapper;
    @Autowired
    private TbOrderItemMapper tbOrderItemMapper;


    @Override
    public List<Cart> addGoodsToCartList(List<Cart> cartList, Long itemId, Integer num) {
        //根据sku  id查询sku商品信息
        TbItem item = tbItemMapper.selectByPrimaryKey(itemId);

        if(item==null){
            throw new RuntimeException("商品不存在");
        }

        if(!item.getStatus().equals("1")){
            throw new RuntimeException("商品状态无效");
        }

        //获取商家id
        String sellerId = item.getSellerId();
        Cart cart = searchCartBySellerId(cartList, sellerId);

        //如果购物车列表中不存在该商家的购物车
        if(cart==null){
            //新建一个购物车对象
            cart= new Cart();
            cart.setSellerId(sellerId);
            cart.setSellerName(item.getSeller());
            TbOrderItem tbOrderItem = CreateOrderItem(item,num);
            List orderItemList= new ArrayList();
            orderItemList.add(tbOrderItem);
            cart.setOrderItemList(orderItemList);
            cartList.add(cart);//将购物车对象添加到
        }else{
            //如果购物车列表中存在该商家的购物车
            //就遍历明细，判断明细中是否存在该商品
            TbOrderItem tbOrderItem = searchOrderItemByItemId(cart.getOrderItemList(), itemId);
            if (tbOrderItem==null){
                //没有，新增购物车明细
                tbOrderItem = CreateOrderItem(item, num);
                cart.getOrderItemList().add(tbOrderItem);
            }else{
                //已经有了，增加数量，修改金额
                tbOrderItem.setNum(tbOrderItem.getNum()+num);
                tbOrderItem.setPrice(new BigDecimal(tbOrderItem.getNum()*tbOrderItem.getPrice().doubleValue()));
                //如果操作后小于等于0，则移除明细
                if(tbOrderItem.getNum()<=0){
                    cart.getOrderItemList().remove(tbOrderItem);
                }
                //如果移除后cart的明细数量为0，则移除该cart
                if(cart.getOrderItemList().size()==0){
                    cartList.remove(cart);
                }
            }

        }
        return cartList;
    }

    /**
     * 根据商品明细id查询
     * @param orderItemList
     * @param itemId
     * @return
     */
    private TbOrderItem searchOrderItemByItemId(List<TbOrderItem> orderItemList,Long itemId){
        for (TbOrderItem tbOrderItem : orderItemList) {
            if(tbOrderItem.getItemId().longValue()==itemId.longValue()){
                return tbOrderItem;
            }
        }
        return null;
    }

    /**
     * 根据商家ID查询购物车对象
     * @param cartList
     * @param sellerId
     * @return
     */
    private Cart searchCartBySellerId(List<Cart> cartList,String sellerId){
        for (Cart cart : cartList) {
            if(cart.getSellerId().equals(sellerId)){
                return cart;
            }
        }
        return null;
    }

    /**
     * 创建订单明细
     * @param tbItem
     * @param num
     * @return
     */
    private TbOrderItem CreateOrderItem(TbItem tbItem,Integer num){
        if(num<0){
            throw new RuntimeException("非法数量");
        }
        TbOrderItem tbOrderItem = new TbOrderItem();
        tbOrderItem.setNum(num);
        tbOrderItem.setGoodsId(tbItem.getGoodsId());
        tbOrderItem.setItemId(tbItem.getId());
        tbOrderItem.setPicPath(tbItem.getImage());
        tbOrderItem.setPrice(tbItem.getPrice());
        tbOrderItem.setSellerId(tbItem.getSellerId());
        tbOrderItem.setTitle(tbItem.getTitle());
        tbOrderItem.setTotalFee(new BigDecimal(tbItem.getPrice().doubleValue()*num));
        return tbOrderItem;
    }
}
