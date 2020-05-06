package com.leyou.item.controller;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.service.BrandService;
import com.leyou.pojo.Brand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
/**
 * 品牌管理
 * */
@RestController
@RequestMapping("brand")
public class BrandController {
    @Autowired
    private BrandService service;

    /**
     * 品牌管理数据查询
     * */
    @GetMapping("page")
    public ResponseEntity<PageResult<Brand>> pageQuery(
            @RequestParam(value = "page",defaultValue = "1") Integer page,
            @RequestParam(value = "rows",defaultValue = "10") Integer rows,
            @RequestParam(value = "sortBy",required = false) String sortBy,
            @RequestParam(value = "desc",required = false)Boolean desc,
            @RequestParam(value = "key",required = false) String key
    ){
        PageResult<Brand> brandPageResult = service.pageQuery(page, rows, sortBy, desc, key);
        if (brandPageResult!=null && null!=brandPageResult.getItems() && brandPageResult.getItems().size()>0){
            return ResponseEntity.ok(brandPageResult);
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }

    /**
     * 品牌管理数据添加
     * */
    @PostMapping
    public ResponseEntity<Void> addBrand(Brand brand, @RequestParam("cids") List<Long> id){
        service.addBrand(brand,id);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @PutMapping
    public ResponseEntity<Void> updateBrand(Brand brand, @RequestParam("cids") List<Long> id){
        service.updateBrand(brand,id);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("cid/{cid}")
    public ResponseEntity<List<Brand>> queryBrandByCategory(@PathVariable("cid")Long cid){
        List<Brand> brands = service.queryBrandByCategory(cid);
        if (brands!= null && brands.size()>0){
            return ResponseEntity.ok(brands);
        }else {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

        }
    }
}
