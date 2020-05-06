package com.leyou.api;

import com.leyou.pojo.Category;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface CategoryApi {
    @GetMapping("category/names")
    public List<String> queryNamesByIds(@RequestParam("ids") List<Long> ids);

    @GetMapping("category/bid/{bid}")
    public List<Category> queryByBreanId(@PathVariable("bid") Long id);

    @GetMapping("category/list")
    public List<Category> queryByParentId(@RequestParam("pid")Long id);
}
