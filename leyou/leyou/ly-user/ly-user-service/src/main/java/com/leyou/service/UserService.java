package com.leyou.service;

import com.leyou.common.utils.NumberUtils;
import com.leyou.mapper.UserMapper;
import com.leyou.utils.CodecUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import pojo.User;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private StringRedisTemplate template;
    static final String KEY_PREFIX = "user:code:phone:";

    public Boolean check(String data, Integer type) {
        User user = new User();
        switch (type) {
            case 1:
                user.setUsername(data);
                break;
            case 2:
                user.setPhone(data);
                break;
        }
        return userMapper.selectCount(user) == 0;
    }

    public Boolean sendVerifyCode(String phone) {
        //产生验证码
        String s = NumberUtils.generateCode(5);
        //将验证码放到redis中
        template.opsForValue().set(KEY_PREFIX + phone, s, 5, TimeUnit.MINUTES);
        return true;
    }

    public Boolean createUser(User user, String code) {
        String s = template.opsForValue().get(KEY_PREFIX + user.getPhone());
        if (null == s) {
            return false;
        }
        if (!code.equals(s)) {
            return false;
        }
        User user1 = new User();
        user1.setUsername(user.getUsername());
        Boolean boo = userMapper.selectCount(user1) == 0;
        if (boo) {
            String salt = CodecUtils.generateSalt();
            String newpassword = CodecUtils.md5Hex(user.getPassword(), salt);
            user.setPassword(newpassword);
            user.setCreated(new Date());
            user.setSalt(salt);
            Boolean boo1 = userMapper.insertSelective(user) == 1;
            if (boo1) {
                this.template.delete(KEY_PREFIX + user.getPhone());
            }
            return boo1;
        }
        return boo;
    }

    public User queryUser(String username, String password) {
        User user = new User();
        user.setUsername(username);
        User selectOne = userMapper.selectOne(user);
        if (selectOne == null) {
            return null;
        }
        String md5Hex = CodecUtils.md5Hex(password, selectOne.getSalt());
        if (!user.getPassword().equals(md5Hex)) {
            return null;
        }
        return user;
    }
}
