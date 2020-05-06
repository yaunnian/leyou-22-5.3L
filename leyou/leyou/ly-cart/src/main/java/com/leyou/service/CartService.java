package com.leyou.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.leyou.common.utils.JsonUtils;
import com.leyou.entity.UserInfo1;
import com.leyou.filter.LoginInterceptor;
import com.leyou.pojo.Cart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartService {
    @Autowired
    private StringRedisTemplate redisTemplate;
    static final String KEY_PREFIX = "ly:cart:uid";
    @Transactional
    public void addCart(Cart cart) {
        try {
            UserInfo1 loginUser = LoginInterceptor.getLoginUser();
            BoundHashOperations<String, Object, Object> ops = redisTemplate.boundHashOps(KEY_PREFIX + loginUser.getId());
            Object skuObj = ops.get(cart.getSkuId().toString());
            if (null != skuObj){
                Cart storeCart= JsonUtils.nativeRead(skuObj.toString(), new TypeReference<Cart>() {
                });
                storeCart.setNum(storeCart.getNum()+cart.getNum());
                ops.put(cart.getSkuId().toString(),JsonUtils.serialize(cart));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public List<Cart> queryCarts() {
        try {
            UserInfo1 loginUser = LoginInterceptor.getLoginUser();
            BoundHashOperations<String, Object, Object> ops = redisTemplate.boundHashOps(KEY_PREFIX + loginUser.getId());
            List<Object> skusObj = ops.values();
            List<Cart> carts = new ArrayList<>();
            if (null != skusObj){
                skusObj.forEach(skuObj->
                    carts.add(JsonUtils.nativeRead(skuObj.toString(),
                            new TypeReference<Cart>() {
                            }
                    )));
                return carts;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    @Transactional
    public void updateIncrementCart(Cart cart) {
        UserInfo1 loginUser = LoginInterceptor.getLoginUser();

        BoundHashOperations<String, Object, Object> ops = redisTemplate.boundHashOps(KEY_PREFIX + loginUser.getId());
        String skuJson= ops.get(cart.getSkuId().toString()).toString();
        Cart storeCart = JsonUtils.nativeRead(skuJson, new TypeReference<Cart>() {
        });
        storeCart.setNum(storeCart.getNum()+1);
        ops.put(storeCart.getSkuId().toString(),JsonUtils.serialize(storeCart));
    }
    @Transactional
    public void deleteCart(Long skuId) {
        UserInfo1 loginUser = LoginInterceptor.getLoginUser();
        BoundHashOperations<String, Object, Object> ops =
                redisTemplate.boundHashOps(KEY_PREFIX + loginUser.getId());
        ops.delete(skuId.toString());
    }
}
