package com.leyou.item.mapper;

import com.leyou.pojo.Brand;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface BrandMapper extends Mapper<Brand> {

    @Insert("insert into tb_category_brand values(#{id},#{ids})")
    void insertBrandCategory(@Param("id") Long id, @Param("ids") Long ids);

    @Delete("delete from tb_category_brand where brand_id=#{id}")
    void deleteBrandCategory(@Param("id") Long id);

    @Select("select t.* from tb_brand t join tb_category_brand cb on t.id=cb.brand_id where cb.category_id=#{cid}")
    List<Brand> queryBrandByCategory(@Param("cid") Long cid);
}
