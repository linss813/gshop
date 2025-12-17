package com.linss.gshop.service;

import com.linss.gshop.entity.Cart;
import com.linss.gshop.entity.CartItem;

import java.util.List;

public interface CartService {

    List<Cart> getCart();

    Cart getCartById(Integer id);

    Cart getCartByUserId(Integer userId);

    //自动为新用户添加购物车功能
    int addCartByUserId(Integer userId);

    int addCartItem(CartItem cartItem);

    int updateCartItem(CartItem cartItem);

    int deleteCartItem(Integer itemId);







}