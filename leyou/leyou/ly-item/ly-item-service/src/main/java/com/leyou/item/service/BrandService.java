package com.leyou.item.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.leyou.common.pojo.PageResult;
import com.leyou.item.mapper.BrandMapper;
import com.leyou.pojo.Brand;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class BrandService {
    @Autowired
    private BrandMapper mapper;
    /**
     * 品牌管理数据查询
     * */
    public PageResult<Brand> pageQuery(Integer page, Integer rows, String sortBy, Boolean desc, String key) {
        PageHelper.startPage(page,rows);

        Example example = new Example(Brand.class);

        if (StringUtils.isNotBlank(key)){
            Example.Criteria criteria=example.createCriteria();

            criteria.andLike("name","%"+key+"%");
        }
        if (StringUtils.isNotBlank(sortBy)){
           // example.setOrderByClause(sortBy+(desc ? "desc" : "asc"));
            example.setOrderByClause(sortBy+(desc ? " desc":" asc" ));
        }
        Page<Brand> brandPage= (Page<Brand>) mapper.selectByExample(example);

        return new PageResult<>(brandPage.getTotal(),new Long(brandPage.getPages()),brandPage);
    }
    /**
     * 品牌管理数据添加
     * */
    @Transactional
    public void addBrand(Brand brand, List<Long> id) {
        mapper.insertSelective(brand);

        id.forEach(ids ->{
            mapper.insertBrandCategory(brand.getId(),ids);
        });

    }

    public void updateBrand(Brand brand, List<Long> id) {
        mapper.updateByPrimaryKeySelective(brand);
        mapper.deleteBrandCategory(brand.getId());
        id.forEach(ids ->{
            mapper.insertBrandCategory(brand.getId(),ids);
        });
    }

    public List<Brand> queryBrandByCategory(Long cid) {
        return  this.mapper.queryBrandByCategory(cid);

    }
}
