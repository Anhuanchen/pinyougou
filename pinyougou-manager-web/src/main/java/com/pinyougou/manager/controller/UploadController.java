package com.pinyougou.manager.controller;

import PageResult.InsertResult;
import com.pinyougou.common.utils.FastDFSClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@RestController
public class UploadController {

    @Value("${FILE_SERVER_URL}")
    private String file_server_url;

    @RequestMapping("/upload")
    public InsertResult upload(MultipartFile file){
        //获取文件扩展名
        String originalFilename = file.getOriginalFilename();
        String extName=originalFilename.substring(originalFilename.lastIndexOf(".")+1);

        try {
            //创建FastDFS的客户端
            FastDFSClient fastDFSClient = new FastDFSClient("classpath:config/fdfs_client.conf");
            //执行上传处理
            String path = fastDFSClient.uploadFile(file.getBytes(), extName);
            //拼接返回的url和ip地址，拼成完整的url
            String url=file_server_url+path;
            return new InsertResult(true,url);
        } catch (Exception e) {
            e.printStackTrace();
            return new InsertResult(false,"上传失败");
        }
    }
}
