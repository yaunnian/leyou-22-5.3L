package com.leyou.item.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.leyou.common.pojo.PageResult;
import com.leyou.item.mapper.*;
import com.leyou.pojo.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GoddsService {
    @Autowired
    private SpuMapper spuMapper;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private BrandMapper brandMapper;
    @Autowired
    private SpuDetailMapper spuDetailMapper;
    @Autowired
    private SkuMapper skuMapper;
    @Autowired
    private StcokMapper stcokMapper;
    @Autowired
    private AmqpTemplate amqpTemplate;


    public PageResult<SpuBo> querySpuByPage(Integer page, Integer rows,String key, Boolean saleable) {
        PageHelper.startPage(page,rows);

        Example example = new Example(Spu.class);

        Example.Criteria criteria = example.createCriteria();

        if (StringUtils.isNotBlank(key)){
            criteria.andLike("title","%" + key + "%");
        }

        if (saleable != null){
            criteria.andEqualTo("saleable",saleable);
        }

        Page<Spu> spuPage = (Page<Spu>) spuMapper.selectByExample(example);

       List<SpuBo> spuBos= new ArrayList<>();

        List<Spu> spus = spuPage.getResult();

        spus.forEach(spu -> {
            SpuBo spuBo = new SpuBo();

            BeanUtils.copyProperties(spu,spuBo);

            List<String> names=  this.categoryService.queryNamesByIn(Arrays.asList(spu.getCid1(),spu.getCid2(),spu.getCid3()));

            spuBo.setCname(StringUtils.join(names,"/"));

            Brand brand = this.brandMapper.selectByPrimaryKey(spuBo.getBrandId());

            spuBo.setBname(brand.getName());

            spuBos.add(spuBo);

        });
        return new PageResult<>(spuPage.getTotal(),new Long(spuPage.getPages()),
                spuBos
                );
    }

    @Transactional
    public void saveGoods(SpuBo spuBo) {
        spuBo.setSaleable(true);
        spuBo.setValid(true);
        spuBo.setCreateTime(new Date());
        spuBo.setLastUpdateTime(new Date());

        this.spuMapper.insertSelective(spuBo);

        SpuDetail spuDetail=spuBo.getSpuDetail();

        spuDetail.setSpuId(spuBo.getId());

        this.spuDetailMapper.insertSelective(spuDetail);

        List<Sku> skus=spuBo.getSkus();

        saveSkus(spuBo,skus);
        sendMessage(spuBo.getId(),"insert");
    }

    private void saveSkus(SpuBo spuBo, List<Sku> skus) {
       skus.forEach(sku -> {
           sku.setSpuId(spuBo.getId());
           sku.setCreateTime(new Date());
           sku.setLastUpdateTime(new Date());
           this.skuMapper.insertSelective(sku);

           Stock stock=new Stock();
           stock.setSkuId(sku.getId());
           stock.setStock(sku.getStock());
           this.stcokMapper.insertSelective(stock);
       });
    }

    public SpuDetail querySpuDetailBySpuId(Long spuId) {
        return this.spuDetailMapper.selectByPrimaryKey(spuId);
    }

    public List<Sku> querySkuBySpuId(Long id) {
        Sku sku=new Sku();
        sku.setSpuId(id);
        List<Sku> select = skuMapper.select(sku);
        select.forEach(skus ->{
            skus.setStock(this.stcokMapper.selectByPrimaryKey(skus.getId()).getStock());
        });
        return select;

    }

    public void updateGoods(SpuBo spuBo) {

        spuBo.setLastUpdateTime(new Date());

        spuMapper.updateByPrimaryKeySelective(spuBo);

        spuDetailMapper.updateByPrimaryKeySelective(spuBo.getSpuDetail());

        Sku sku=new Sku();
        sku.setSpuId(spuBo.getId());
        List<Sku> skus = this.skuMapper.select(sku);

        if (!CollectionUtils.isEmpty(skus)){
            List<Long> longs = skus.stream().map(Sku::getId).collect(Collectors.toList());
            this.stcokMapper.deleteByIdList(longs);
            this.skuMapper.delete(sku);
        }
            saveSkus(spuBo,spuBo.getSkus());
            sendMessage(spuBo.getId(),"update");
    }

    public Spu querySpuById(Long id) {
        return spuMapper.selectByPrimaryKey(id);
    }

    public void sendMessage(Long id,String type){

            this.amqpTemplate.convertAndSend("item." + type, id);

    }
}
