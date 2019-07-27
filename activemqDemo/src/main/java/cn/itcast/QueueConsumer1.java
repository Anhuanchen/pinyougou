package cn.itcast;


import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class QueueConsumer1 {
    /**
     * 消息消费者
     * @param args
     */
    public static void main(String[] args) throws Exception {
        //创建工厂
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory("tcp://192.168.230.1:61616");
        //创建连接
        Connection connection = activeMQConnectionFactory.createConnection();
        //开启连接
        connection.start();
        //获取连接
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //创建队列对象
        Queue queue = session.createQueue("test-queue");
        //创建消息消费者
        MessageConsumer consumer = session.createConsumer(queue);
        //监听消息
        consumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                TextMessage TextMessage= (TextMessage) message;
                try{
                    System.out.println("接收到的消息："+TextMessage.getText());
                }catch (Exception e){
                    System.out.println("失败");
                }
            }
        });
        //等待键盘输入
        System.in.read();
        //关闭资源
        consumer.close();
        session.close();
        connection.close();

    }
}
