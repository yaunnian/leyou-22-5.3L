package com.leyou.service;

import com.leyou.pojo.Spu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.*;

@Service
public class FileService {
    @Autowired
    private PageService pageService;
    @Autowired
    private TemplateEngine templateEngine;
    @Value("${ly.thymeleaf.destPath}")
    private String destPath;

    public boolean exists(Long id){
        return this.createPath(id).exists();
    }
    //判断文件是否存在
    private File createPath(Long id) {
        File file = new File(destPath);
        if (!file.exists()){
            file.mkdirs();
        }
        return new File(file,id+".html");
    }
    //G:/Nginx/nginx-1.14.2/html/item
    public void syncCreateHtml(Long id){
        Context context=new Context();
        context.setVariables(pageService.loadData(id));
        File filePath=new File(destPath,id+".html");
        try {
            PrintWriter writer=new PrintWriter(filePath,"utf-8");
            templateEngine.process("item",context,writer);

        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void deleteHtml(Long id){
        File file=new File(destPath,id+".html");
        file.deleteOnExit();
    }

}
