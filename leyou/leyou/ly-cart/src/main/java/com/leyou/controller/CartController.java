package com.leyou.controller;

import com.leyou.pojo.Cart;
import com.leyou.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CartController {
    @Autowired
    private CartService cartService;
    @PostMapping
    public ResponseEntity<Void> addCart(@RequestBody Cart cart){
        this.cartService.addCart(cart);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<Cart>> queryCarts(){
        List<Cart> carts=this.cartService.queryCarts();
        if (carts != null && 0 != carts.size()){
            return ResponseEntity.ok(carts);
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    @PutMapping("increment")
    public ResponseEntity<Void> updateIncrementCart(@RequestBody Cart cart){
        this.cartService.updateIncrementCart(cart);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @DeleteMapping("{skuId}")
    public ResponseEntity<Void> deleteCart(@PathVariable("skuId")Long skuId){
        this.cartService.deleteCart(skuId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
