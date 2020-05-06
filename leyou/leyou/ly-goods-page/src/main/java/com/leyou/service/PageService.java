package com.leyou.service;

import com.leyou.clients.GoodsClient;
import com.leyou.clients.SpecClient;

import com.leyou.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PageService {
    @Autowired
    private GoodsClient goodsClient;
    @Autowired
    private SpecClient specClient;

    public Map<String,Object> loadData(Long spuId) {
       // System.out.println(spuId);
        Map<String,Object> result = new HashMap<String,Object>();

        Spu spu = goodsClient.querySpuById(spuId);

        System.out.println("spu:"+spu);
        result.put("spu",spu);

        SpuDetail spuDetail = goodsClient.querySpuDetailBySpuId(spuId);
        //System.out.println("spuDetail:"+spuDetail);
        result.put("spuDetail",spuDetail);

        List<Sku> skus = goodsClient.querySkuBySpuId(spuId);
      //  System.out.println("skus:"+skus);
        result.put("skus",skus);

        List<Param> specailSpecParams = specClient.querySpecParam(null,spu.getCid3(), null, false);

        Map<Long,String> specParams=new HashMap<Long,String>();

        specailSpecParams.forEach(specailSpecParam->{
            specParams.put(specailSpecParam.getId(),specailSpecParam.getName());
        });
      //  System.out.println("specParams:"+specParams);
        result.put("specParams",specParams);

        List<Group> specGroups = specClient.querySpecGroups(spu.getCid3());

        result.put("specGroups",specGroups);
       // System.out.println("specGroups:"+specGroups);
        System.out.println("result:       "+result);
        return  result;
    }
}
