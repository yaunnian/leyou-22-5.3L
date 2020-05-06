package com.leyou.item.mapper;

import com.leyou.pojo.Category;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.additional.idlist.SelectByIdListMapper;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface CategoryMapper extends Mapper<Category>, SelectByIdListMapper<Category,Long> {

    @Select("select t.* from tb_category t left join tb_category_brand cb on t.id=cb.category_id  where cb.brand_id = #{bid}")
    List<Category> queryByBreanId(@Param("bid") Long bid);



}