package com.leyou.item.service;

import com.leyou.item.mapper.GroupMapper;
import com.leyou.item.mapper.ParamMapper;
import com.leyou.pojo.Group;
import com.leyou.pojo.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SpecService {
    @Autowired
    private GroupMapper groupMapper;
    @Autowired
    private ParamMapper paramMapper;

    public List<Group> querySpecGroups(Long cid) {
        Group group=new Group();
        group.setCid(cid);
        List<Group> groups = groupMapper.select(group);
        groups.forEach(t->{
            Param param = new Param();
            Long tId = t.getId();
            param.setGroupId(tId);
            t.setSpecParams(paramMapper.select(param));
        });
        return groups;
    }
    @Transactional
    public void InsertGroup(Group group) {
        groupMapper.insertSelective(group);
    }
    @Transactional
    public void DeleteGroup(Long id) {
        Group group=new Group();
        group.setId(id);
        groupMapper.delete(group);
    }
    @Transactional
    public void UpdateGroup(Group group) {
        groupMapper.updateByPrimaryKeySelective(group);
    }

    public List<Param> querySpecParam(Long gid,Long cid,Boolean searching,Boolean generic) {
        Param param=new Param();
        param.setGroupId(gid);
        param.setCid(cid);
        param.setSearching(searching);
        param.setGeneric(generic);
        return this.paramMapper.select(param);
    }
    @Transactional
    public void InsertParam(Param param) {
            paramMapper.insertSelective(param);
    }
    @Transactional
    public void UpdateParam(Param param) {
        paramMapper.updateByPrimaryKeySelective(param);
    }
    @Transactional
    public void DeleteParam(Long id) {
        Param param=new Param();
        param.setId(id);
        paramMapper.delete(param);
    }

    public int querySpecParamCount(Long id) {
        Param param = new Param();
        param.setGroupId(id);
        int index = paramMapper.selectCount(param);
        return index;
    }
}
