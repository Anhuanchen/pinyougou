package com.pinyougou.cart.controller;

import PageResult.InsertResult;
import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.common.utils.IdWorker;
import com.pinyougou.order.service.OrderService;
import com.pinyougou.pay.service.WeixinPayService;
import com.pinyougou.pojo.TbPayLog;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/pay")
public class PayController {
    @Reference
    private WeixinPayService weixinPayService;
    @Reference
    private OrderService orderService;

    /**
     * 生成二维码
     * @return
     */
    @RequestMapping("/createNative")
    public Map createNative(){
        //获取当前用户
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        //从redis中查询
        TbPayLog payLog = orderService.searchPayLogFromRedis(userId);
        if(payLog!=null){
            return weixinPayService.createNative(payLog.getOutTradeNo(),payLog.getTotalFee()+"");
        }else{
            return new HashMap();
        }

    }

    /**
     * 查询支付状态
     * @param out_trade_no
     * @return
     */
    @RequestMapping("/queryPayStatus")
    public InsertResult queryPayStatus(String out_trade_no){
        InsertResult result = null;
        int x=0;
        while(true){
         //调用查询接口
            Map map = weixinPayService.queryPayStatus(out_trade_no);
            if(map==null){
                result = new InsertResult(false,"支付出错");
                break;
            }
            if (map.get("trade_state").equals("SUCCESS")){
                result=new InsertResult(true,"支付成功");
                //修改订单状态
                orderService.updateOrderStatus(out_trade_no,(String)map.get("transaction_id"));
                break;
            }
            try{
                Thread.sleep(3000);
            }catch (Exception e){
                e.printStackTrace();
            }
            x++;
            if(x>=100){
                result=new InsertResult(false,"二维码超时");
                break;
            }
        }

        return result;
    }

}
