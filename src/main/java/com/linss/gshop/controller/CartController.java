package com.linss.gshop.controller;

import com.linss.gshop.entity.Cart;
import com.linss.gshop.entity.CartItem;
import com.linss.gshop.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping("/getCart")
    public List<Cart> getCart() {
        return cartService.getCart();
    }

    @GetMapping("/getCartById/{id}")
    public Cart getCartById(@PathVariable Integer id) {
        return cartService.getCartById(id);
    }

    @GetMapping("/getCartByUserId/{userId}")
    public Cart getCartByUserId(@PathVariable Integer userId) {
        return cartService.getCartByUserId(userId);
    }

    @PostMapping("/addCartItem")
    public int addCartItem(@RequestBody CartItem cartItem) {
        return cartService.addCartItem(cartItem);
    }

    @PutMapping("/updateCartItem")
    public int updateCartItem(@RequestBody CartItem cartItem) {
        return cartService.updateCartItem(cartItem);
    }

    @DeleteMapping("/deleteCartItem/{itemId}")
    public int deleteCartItem(@PathVariable Integer itemId) {
        return cartService.deleteCartItem(itemId);
    }

    @PostMapping("/addCartByUserId/{userId}")
    public int addCartByUserId(@PathVariable Integer userId) {
        return cartService.addCartByUserId(userId);
    }


}