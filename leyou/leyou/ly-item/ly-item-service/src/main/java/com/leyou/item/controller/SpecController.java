package com.leyou.item.controller;

import com.leyou.item.service.SpecService;
import com.leyou.pojo.Group;
import com.leyou.pojo.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("spec")
public class SpecController {
    @Autowired
    private SpecService service;

    @GetMapping("groups/{cid}")
    public ResponseEntity<List<Group>> querySpecGroups(@PathVariable("cid")Long cid){
        List<Group> groupList = service.querySpecGroups(cid);
        if (groupList!=null && groupList.size()>0){
            return ResponseEntity.ok(groupList);
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    @PostMapping("group")
    public ResponseEntity<Void> InsertGroup(@RequestBody Group  group){
        service.InsertGroup(group);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @DeleteMapping("group/{id}")
    public ResponseEntity<Void> DeleteGroup(@PathVariable("id")Long id){
        int index = service.querySpecParamCount(id);
        System.out.println(index);
        if (index != 0){
            String str="有数据不能删除";
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }else {
            service.DeleteGroup(id);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }

    }
    @PutMapping("group")
    public ResponseEntity<Void> UpdateGroup(@RequestBody Group  group){
        service.UpdateGroup(group);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @PostMapping("param")
    public ResponseEntity<Void> InsertParam(@RequestBody Param param){
        service.InsertParam(param);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @DeleteMapping("param/{id}")
    public ResponseEntity<Void> DeleteParam(@PathVariable("id")Long id){
        service.DeleteParam(id);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @PutMapping("param")
    public ResponseEntity<Void> UpdateParam(@RequestBody Param param){
        service.UpdateParam(param);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @GetMapping("params")
    public ResponseEntity<List<Param>> querySpecParam(@RequestParam(value = "gid",required = false) Long gid,
                                                      @RequestParam(value = "cid",required = false)Long cid,
                                                      @RequestParam(value = "searching",required = false)Boolean searching,
                                                      @RequestParam(value = "generic",required = false) Boolean generic
                                                      ){
        List<Param> paramList = service.querySpecParam(gid,cid,searching,generic);
        if (paramList!=null && paramList.size()>0){
            return ResponseEntity.ok(paramList);
        }
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
