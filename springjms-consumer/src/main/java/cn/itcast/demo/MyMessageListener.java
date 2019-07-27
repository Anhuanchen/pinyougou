package cn.itcast.demo;


import org.springframework.stereotype.Component;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;


public class MyMessageListener implements MessageListener {

    public void onMessage(Message message){
        TextMessage textMessage = (TextMessage)message;
        try{
            System.out.println("接收到消息："+textMessage.getText());
        }catch (Exception e){
            System.out.println("失败");
        }

    }
}
