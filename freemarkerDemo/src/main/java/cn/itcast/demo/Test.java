package cn.itcast.demo;


import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Test {

    public static void main(String[] args) throws IOException, TemplateException {
        //创建配置类
        Configuration configuration = new Configuration(Configuration.getVersion());
        //设置模板文件所在的路径
        configuration.setDirectoryForTemplateLoading(new File("F:/pinyougou-parent/freemarkerDemo/src/main/resources"));
        //设置字符集
        configuration.setDefaultEncoding("utf-8");
        //加载模板
        Template template = configuration.getTemplate("test.ftl");
        //创建数据类型
        Map map = new HashMap<>();
        map.put("name","李四");
        map.put("message","欢迎来到freemarker的世界");
        map.put("success",false);

        List list = new ArrayList<>();
        Map map1 = new HashMap();
        map1.put("name","苹果");
        map1.put("price","6.8");
        Map map2 = new HashMap();
        map2.put("name","香蕉");
        map2.put("price","3.8");
        Map map3 = new HashMap();
        map3.put("name","梨子");
        map3.put("price","4.5");
        list.add(map1);
        list.add(map2);
        list.add(map3);
        map.put("goodsList",list);
        map.put("today",new Date());
        map.put("point",431241234);
        //map.put("aaa","哈哈哈哈");

        //创建writer对象
        FileWriter fileWriter = new FileWriter("d:\\test.html");
        //输出
       template.process(map,fileWriter);
        //关闭流
        fileWriter.close();

    }
}
