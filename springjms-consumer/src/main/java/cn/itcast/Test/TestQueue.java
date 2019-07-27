package cn.itcast.Test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext-jms-consumer-topic.xml")
public class TestQueue {

    @Test
    public void TestQueue(){
        try{
            System.in.read();
        }catch (Exception e){
            System.out.println("失败");
        }
    }

    @Test
    public void TestTopic(){
        try{
            System.in.read();
        }catch (Exception e){
            System.out.println("失败");
        }

    }

}
