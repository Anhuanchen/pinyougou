package cn.itcast;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class TopicProducer {
    /**
     * 发布订阅模式，生产者
     * @param args
     */
    public static void main(String[] args) throws JMSException {
        //创建工厂类
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory("tcp://192.168.230.1:61616");
        //创建连接对象
        Connection connection = activeMQConnectionFactory.createConnection();
        //开启连接
        connection.start();
        //获取session
        Session session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
        //创建主题对象
        Topic topic = session.createTopic("test-topic");
        //创建消息生产者
        MessageProducer topicProducer = session.createProducer(topic);
        //生产消息
        TextMessage topicText = session.createTextMessage("欢迎来到topic");
        //发送消息
        topicProducer.send(topicText);
        //关闭资源
        topicProducer.close();
        session.close();
        connection.close();


    }
}
