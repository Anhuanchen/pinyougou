package cn.itcast.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
@RestController
public class SmsProducer {
    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;
    @RequestMapping("/sendSms")
    public void SendMessage(){
        Map map = new HashMap<String,String>();
        map.put("PhoneNumber","15829476835");
        map.put("TemplateCode","SMS_171117853");
        map.put("TemplateParam","");
        map.put("SignName","云山");
        jmsMessagingTemplate.convertAndSend("sms",map);
    }

}
