package com.leyou.controller;

import com.leyou.service.FileService;
import com.leyou.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class PageController {
    @Autowired
    private FileService fileService;
    @Autowired
    private PageService pageService;
    @GetMapping("item/{id}.html")
    public String toPage(@PathVariable("id") Long spuId, Model model){
       // System.out.println("spuid:"+spuId);
        model.addAllAttributes(pageService.loadData(spuId));
        if (!fileService.exists(spuId)){
            System.out.println("11111");
            fileService.syncCreateHtml(spuId);
        }
        return "item";
    }

}
