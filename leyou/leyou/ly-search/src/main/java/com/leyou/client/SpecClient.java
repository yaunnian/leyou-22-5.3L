package com.leyou.client;

import com.leyou.api.SpecificationApi;
import com.leyou.pojo.Group;
import com.leyou.pojo.Param;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("item-service")
public interface SpecClient extends SpecificationApi {
 /*   @GetMapping("spec/groups/{cid}")
    public List<Group> querySpecGroups(@PathVariable("cid")Long cid);

    @GetMapping("spec/params")
    public List<Param> querySpecParam(@RequestParam(value = "gid",required = false) Long gid,
                                      @RequestParam(value = "cid",required = false)Long cid,
                                      @RequestParam(value = "searching",required = false)Boolean searching,
                                      @RequestParam(value = "generic",required = false) Boolean generic
    );*/
}
