package com.leyou.api;

import com.leyou.pojo.Group;
import com.leyou.pojo.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface SpecificationApi {
    @GetMapping("spec/groups/{cid}")
    public List<Group> querySpecGroups(@PathVariable("cid")Long cid);

    @GetMapping("spec/params")
    public List<Param> querySpecParam(@RequestParam(value = "gid",required = false) Long gid,
                                                      @RequestParam(value = "cid",required = false)Long cid,
                                                      @RequestParam(value = "searching",required = false)Boolean searching,
                                                      @RequestParam(value = "generic",required = false) Boolean generic
    );
}

