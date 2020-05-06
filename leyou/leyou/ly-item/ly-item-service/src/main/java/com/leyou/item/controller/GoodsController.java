package com.leyou.item.controller;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.service.GoddsService;
import com.leyou.pojo.Sku;
import com.leyou.pojo.Spu;
import com.leyou.pojo.SpuBo;
import com.leyou.pojo.SpuDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class GoodsController {

    @Autowired
    private GoddsService goddsService;
    @GetMapping("spu/page")
    public ResponseEntity<PageResult<SpuBo>> querySpuByPage(
            @RequestParam(value = "page",defaultValue = "1")Integer page,
            @RequestParam(value = "rows",defaultValue = "5")Integer rows,
            @RequestParam(value = "key",required = false) String key,
            @RequestParam(value = "saleable",required = false)Boolean saleable

    ){
        PageResult<SpuBo> spuBoPageResult=this.goddsService.querySpuByPage(page,rows,key,saleable);

        if (spuBoPageResult!=null && spuBoPageResult.getItems().size()>0){
            return ResponseEntity.ok(spuBoPageResult);
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    @PostMapping("goods")
    public ResponseEntity<Void> saveGoods(@RequestBody SpuBo spuBo){
            this.goddsService.saveGoods(spuBo);
            return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @GetMapping("spu/detail/{spuId}")
    public ResponseEntity<SpuDetail> querySpuDetailBySpuId(@PathVariable("spuId")Long spuId){
        SpuDetail spuDetail=goddsService.querySpuDetailBySpuId(spuId);
        if (spuDetail != null ){
            return ResponseEntity.ok(spuDetail);
        }else {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
    }

    @GetMapping("/sku/list")
    public ResponseEntity<List<Sku>> querySkuBySpuId(@RequestParam("id")Long id){
        List<Sku> skus=goddsService.querySkuBySpuId(id);
        if (skus != null && skus.size()>0){
            return ResponseEntity.ok(skus);
        }else {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
    }

    @PutMapping("goods")
    public ResponseEntity<Void> updateGoods(@RequestBody SpuBo spuBo){
        this.goddsService.updateGoods(spuBo);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @GetMapping("spu/{id}")
    public ResponseEntity<Spu> querySpuById(@PathVariable("id")Long id){
        Spu spu=goddsService.querySpuById(id);
        if (spu != null){
            return ResponseEntity.ok(spu);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
