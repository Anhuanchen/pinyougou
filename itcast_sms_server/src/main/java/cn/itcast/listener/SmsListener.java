package cn.itcast.listener;

import cn.itcast.sms_utils.SmsUtil;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 消息监听类
 */
@Component
public class SmsListener {

    @Autowired
    private SmsUtil smsUtil;

    @JmsListener(destination = "sms")
    public void sendSms(Map<String ,String> map){
        try {
            SendSmsResponse sendSmsResponse = smsUtil.sendSms(
                    map.get("mobile"),
                    map.get("template_code"),
                    map.get("sign_name"),
                    map.get("param")

            );
            System.out.println("Code="+sendSmsResponse.getCode());
            System.out.println("Message="+sendSmsResponse.getMessage());
            System.out.println("RequestId=" + sendSmsResponse.getRequestId());
            System.out.println("BizId=" + sendSmsResponse.getBizId());
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
