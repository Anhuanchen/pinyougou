package com.pinyougou.page.service.impl;

import com.pinyougou.page.ItemPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

@Service
public class PageDeleteListener implements MessageListener {

    @Autowired
    private ItemPageService itemPageService;

    @Override
    public void onMessage(Message message) {
        ObjectMessage objectMessage = (ObjectMessage)message;

        try {
            Long[] ids =(Long[]) objectMessage.getObject();
            System.out.println("ItemDeleteListener监听到的消息是"+ids);
            boolean b = itemPageService.deleteItemHtml(ids);
            System.out.println("网页删除结果："+b);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
