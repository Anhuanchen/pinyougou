package cn.itcast;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class TopicConsumer {
    /**
     * 发布订阅消费者
     * @param args
     */
    public static void main(String[] args) throws Exception {
        //创建连接工厂
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory("tcp://192.168.230.1:61616");
        //创建连接
        Connection connection = activeMQConnectionFactory.createConnection();
        //启动连接
        connection.start();
        //创建session
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //创建主题对象
        Topic topic = session.createTopic("test-topic");
        //创建消费者
        MessageConsumer consumer = session.createConsumer(topic);
        //监听消息
        consumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                TextMessage TextMessage = (TextMessage) message;
                try{
                    System.out.println("收到的消息是："+TextMessage);
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
