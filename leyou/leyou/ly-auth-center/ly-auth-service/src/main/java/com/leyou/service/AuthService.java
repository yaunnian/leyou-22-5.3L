package com.leyou.service;

import com.leyou.client.UserClient;
import com.leyou.config.JwtProperties;
import com.leyou.entity.UserInfo1;
import com.leyou.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pojo.User;

@Service
public class AuthService {
    @Autowired
    private UserClient userClient;
    @Autowired
    private JwtProperties jwtProperties;
    public String accredit(String username, String password) {
        try {
            User user = this.userClient.queryUser(username, password);
            if (user == null) {
                return null;
            }

            String token = JwtUtils.generateToken(new UserInfo1(user.getId(), user.getUsername()),
                    jwtProperties.getPrivateKey(), jwtProperties.getExpire()
            );
            return token;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
