package com.pinyougou.pay.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.wxpay.sdk.WXPayUtil;
import com.pinyougou.common.utils.HttpClient;
import com.pinyougou.pay.service.WeixinPayService;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;
import java.util.Map;

@Service
public class WeixinPayServiceImpl implements WeixinPayService {

    @Value("${appid}")
    private String appid;
    @Value("${partner}")
    private String partner;
    @Value("${partnerkey}")
    private String partnerkey;
    @Value("${notifyurl}")
    private String notifyurl;
    /**
     * 生成微信支付二维码
     * @param out_trade_no
     * @param total_fee
     * @return
     */
    @Override
    public Map createNative(String out_trade_no, String total_fee) {
        //创建参数，封装参数
        Map map = new HashMap();
        map.put("appid","wx8397f8696b538317");//公众账号ID
        map.put("mch_id","1473426802");//商户号
        map.put("nonce_str", WXPayUtil.generateNonceStr());//随机字符串
        //map.put("sign","");//签名
        map.put("body","品优购");//商品描述
        map.put("out_trade_no",out_trade_no);//商户订单号
        map.put("total_fee",total_fee);//标价金额
        map.put("spbill_create_ip","127.0.0.1");//终端IP
        map.put("notify_url","http://a31ef7db.ngrok.io/WeChatPay/WeChatPayNotify");//通知地址,回调地址
        map.put("trade_type","NATIVE");//交易类型
        try {
            //生成xml
            String xmlParam = WXPayUtil.generateSignedXml(map, "T6m9iK73b0kn9g5v426MKfHQH7X8rKwb");
            System.out.println(xmlParam);
            HttpClient httpClient = new HttpClient("https://api.mch.weixin.qq.com/pay/unifiedorder");
            httpClient.setHttps(true);
            httpClient.setXmlParam(xmlParam);
            httpClient.post();
            //获得结果
            String result = httpClient.getContent();
            System.out.println(result);
            Map<String, String> map1 = WXPayUtil.xmlToMap(result);
            HashMap<String, String> resultMap = new HashMap<>();
            map1.put("code_url",map1.get("code_url"));//支付地址
            map1.put("total_fee",total_fee);//总金额
            map1.put("out_trade_no",out_trade_no);//订单号
            return map1;
        } catch (Exception e) {
            e.printStackTrace();
            return new HashMap();
        }
    }

    /**
     * 查询支付状态
     * @param out_trade_no
     * @return
     */
    @Override
    public Map queryPayStatus(String out_trade_no) {
        HashMap param = new HashMap();
        param.put("appid","wx8397f8696b538317");
        param.put("mch_id","1473426802");
        param.put("out_trade_no",out_trade_no);
        param.put("nonce_str",WXPayUtil.generateNonceStr());
        String url="https://api.mch.weixin.qq.com/pay/orderquery";
        try {
            String xmlParam = WXPayUtil.generateSignedXml(param, "T6m9iK73b0kn9g5v426MKfHQH7X8rKwb");
            HttpClient client = new HttpClient(url);
            client.setHttps(true);
            client.setXmlParam(xmlParam);
            client.post();
            String result = client.getContent();
            Map<String, String> stringStringMap = WXPayUtil.xmlToMap(result);
            System.out.println(stringStringMap);
            return stringStringMap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
}
