package com.leyou.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.leyou.client.CategoryClient;
import com.leyou.client.GoodsClient;
import com.leyou.client.SpecClient;
import com.leyou.common.utils.JsonUtils;
import com.leyou.pojo.*;
import com.leyou.reponsitory.GoodsRepository;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class IndexService {
    @Autowired
    private CategoryClient categoryClient;

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private SpecClient specClient;
    @Autowired
    private GoodsRepository goodsRepository;
    public Goods buildGoods(SpuBo spuBo){
        Goods goods = new Goods();

        //对应拷贝赋值相同的属性
        BeanUtils.copyProperties(spuBo, goods);
        //all 所有需要被搜索的信息，包含标题，分类

        List<String> names = categoryClient.queryNamesByIds(Arrays.asList(spuBo.getCid1(), spuBo.getCid2(), spuBo.getCid3()));

        //跨服务查询分类信息，并展示
        String all = spuBo.getTitle() + " " + StringUtils.join(names, " ");
        goods.setAll(all);
        //price
        //skus

        //要处理sku首先要查询到所有的sku,由于sku只需要id，title，price，image，所以这里不能把整个sku纳入
        List<Sku> skus = this.goodsClient.querySkuBySpuId(spuBo.getId());

        List<Map<String, Object>> skuMapList = new ArrayList<>();

        List<Long> prices = new ArrayList<>();

        skus.forEach(sku -> {
            prices.add(sku.getPrice());
            Map<String, Object> skuMap = new HashMap<>();
            skuMap.put("id", sku.getId());
            skuMap.put("title", sku.getTitle());
            skuMap.put("price", sku.getPrice());
            //如果不为空，多个图片是以逗号分隔，所以切开取第一个
            skuMap.put("image", StringUtils.isBlank(sku.getImages()) ? "" : sku.getImages().split(",")[0]);

            skuMapList.add(skuMap);
        });
        goods.setSkus(JsonUtils.serialize(skuMapList));

        goods.setPrice(prices);

        //specs
        Map<String, Object> specs = getSpecs(spuBo);
        //把最终获取到的spec数据赋值给goods
        goods.setSpecs(specs);

        return goods;

    }

    private Map<String, Object> getSpecs(SpuBo spuBo) {
        //key规格参数的名称，值为对应的规格参数的值（取决于商品本身）
        Map<String, Object> specs = new HashMap<>();//操作系统：android

        //1,获取到所有的可搜索的规格参数
        List<Param> searchingParams = this.specClient.querySpecParam(null, spuBo.getCid3(), true,null);

        //2,循环可搜索的规格参数集合，判断通用还是特有，通用从通用规格中取值，特有从特有规格中取值

        SpuDetail spuDetail = this.goodsClient.querySpuDetailBySpuId(spuBo.getId());

        //由于要取值，为了方便我们把通用规格和特有规格都转换为Map
        //通用规格键值对集合map
        Map<Long, Object> genericMap = JsonUtils.nativeRead(spuDetail.getGenericSpec(), new TypeReference<Map<Long, Object>>() {
        });
        //特有规格键值对集合map
        Map<Long, List<String>> specialMap = JsonUtils.nativeRead(spuDetail.getSpecialSpec(), new TypeReference<Map<Long, List<String>>>() {
        });

        //3,通用和特有的值来自于spuDetail
        searchingParams.forEach(specParam -> {
            Long id = specParam.getId();//对应取值，规格参数的id就是通用规格和特有规格保存时map的key，当key一致可以直接取值
            String name = specParam.getName();//具体的key的值

            //通用参数
            Object value = null;
            if (specParam.getGeneric()) {
                //通用参数
                value = genericMap.get(id);

                if (null != value && specParam.getNumeric()) {
                    //数值类型可能需要加分段,以及单位
                    value = this.chooseSegment(value.toString(), specParam);
                }
            } else {//特有参数
                value = specialMap.get(id);

            }
            if (null == value) {
                value = "其他";
            }
            specs.put(name, value);
        });
        return  specs;

    }

    private Object chooseSegment(String value, Param p) {
        double val = NumberUtils.toDouble(value);
        String result = "其它";
        // 保存数值段
        for (String segment : p.getSegments().split(",")) {//segment:1000-2000
            String[] segs = segment.split("-");
            // 获取数值范围
            double begin = NumberUtils.toDouble(segs[0]);
            double end = Double.MAX_VALUE;
            if(segs.length == 2){
                end = NumberUtils.toDouble(segs[1]);
            }
            // 判断是否在范围内
            if(val >= begin && val < end){
                if(segs.length == 1){
                    result = segs[0] + p.getUnit() + "以上";
                }else if(begin == 0){
                    result = segs[1] + p.getUnit() + "以下";
                }else{
                    result = segment + p.getUnit();//添加单位
                }
                break;
            }
        }
        return result;
    }

    public void createIndex(Long id) {
        Spu spu = goodsClient.querySpuById(id);

        SpuBo spuBo=new SpuBo();

        BeanUtils.copyProperties(spu,spuBo);

        Goods goods=buildGoods(spuBo);

        this.goodsRepository.save(goods);
    }

    public void deleteIndex(Long id) {
        this.goodsRepository.deleteById(id);
    }
}
