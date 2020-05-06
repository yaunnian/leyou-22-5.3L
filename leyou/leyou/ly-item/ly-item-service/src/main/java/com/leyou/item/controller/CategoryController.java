package com.leyou.item.controller;

import com.leyou.item.service.CategoryService;
import com.leyou.pojo.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
/**
 * 分类管理
 * */
@RestController
@RequestMapping("category")
public class CategoryController {
    @Autowired
    private CategoryService service;
    /**
     * 分类管理数据查询
     * */
    @GetMapping("list")
    public ResponseEntity<List<Category>> queryByParentId(@RequestParam("pid")Long id){
        List<Category> categories = service.queryByParentId(id);
        if(categories!=null && categories.size()>0){
            return ResponseEntity.ok(categories);
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }
    /**
     * 查询所有分类信息
     * */
    @GetMapping("bid/{bid}")
    public ResponseEntity<List<Category>> queryByBreanId(@PathVariable("bid") Long id){
        List<Category> categories = service.queryByBreanId(id);
        if (categories!=null && categories.size()>0){
            return ResponseEntity.ok(categories);
        }else {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
    }
    /**
     * 根据商品分类id查询名称
     */
    @GetMapping("names")
    public ResponseEntity<List<String>> queryNamesByIds(@RequestParam("ids") List<Long> ids){
        List<String> list=this.service.queryNamesByIn(ids);
        if (list!=null || list.size()>0){
            return ResponseEntity.ok(list);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
