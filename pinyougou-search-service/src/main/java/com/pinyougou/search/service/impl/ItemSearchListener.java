package com.pinyougou.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.search.service.ItemSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.util.List;
import java.util.Map;
@Component
public class ItemSearchListener implements MessageListener {

    @Autowired
    private  ItemSearchService itemSearchService;

    @Override
    public void onMessage(Message message) {
        System.out.println("监听接收到消息");
        try{
            TextMessage textMessage = (TextMessage)message;
            String text = textMessage.getText();
            List<TbItem> list = JSON.parseArray(text, TbItem.class);
            for (TbItem item : list) {
                System.out.println(item.getId()+"  "+item.getTitle());
                Map specMap = JSON.parseObject(item.getSpec());
                item.setSpecMap(specMap);
            }
            itemSearchService.importList(list);
            System.out.println("成功导入到索引库");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
