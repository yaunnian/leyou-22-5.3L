package com.leyou.search.test;

import com.leyou.LySearchService;
import com.leyou.client.GoodsClient;
import com.leyou.common.pojo.PageResult;
import com.leyou.pojo.Goods;
import com.leyou.pojo.SpuBo;
import com.leyou.reponsitory.GoodsRepository;
import com.leyou.service.IndexService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LySearchService.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IndexCreateTest {

    @Autowired
    private ElasticsearchTemplate esTemplate;
    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private IndexService indexService;

    @Autowired
    private GoodsRepository goodsRepository;

    @Test
    public void createIndexesAndPutMapping() {
        //创建索引
        esTemplate.createIndex(Goods.class);
        // 配置映射
        esTemplate.putMapping(Goods.class);
    }
        @Test
        public void loadData(){
            int page = 1;
            while (true){
                //分页查询所有的spuBo
                PageResult<SpuBo> spuBoPageResult = goodsClient.querySpuByPage(page,50,null, null);
                //如果没查到说明查完了，直接停
                if (null==spuBoPageResult){
                    break;
                }
                page++;
                //从查询结果中获取到所有的spuBo信息
                List<SpuBo> spuBos = spuBoPageResult.getItems();
                List<Goods> goodsList = new ArrayList<>();
                spuBos.forEach(spuBo -> {
                    //把spuBo转换为goods
                    Goods goods = indexService.buildGoods(spuBo);
                    goodsList.add(goods);
                });
                //批量保存goods到索引库
                goodsRepository.saveAll(goodsList);

            }

        }
    }

