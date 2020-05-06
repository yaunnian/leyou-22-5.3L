package com.leyou.item.service;

import com.leyou.item.mapper.CategoryMapper;
import com.leyou.pojo.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryService {
    @Autowired
    private CategoryMapper mapper;

    public List<Category> queryByParentId(Long id){
        Category category = new Category();
        category.setParentId(id);
        return mapper.select(category);
    }
    public List<Category> queryByBreanId(Long bid){
        return mapper.queryByBreanId(bid);

    }

    public List<String> queryNamesByIn(List<Long> cids) {

        List<String> names=new ArrayList<>();

        List<Category> categories=this.mapper.selectByIdList(cids);

        categories.forEach(category -> {
            names.add(category.getName());
        });

        return names;
    }

}
