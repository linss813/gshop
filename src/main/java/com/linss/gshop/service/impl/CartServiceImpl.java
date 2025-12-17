package com.linss.gshop.service.impl;

import com.linss.gshop.entity.Cart;
import com.linss.gshop.entity.CartItem;
import com.linss.gshop.mapper.CartMapper;
import com.linss.gshop.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartMapper cartMapper;

    @Override
    public List<Cart> getCart() {
        return cartMapper.getCart();
    }

    @Override
    public Cart getCartById(Integer id) {
        return cartMapper.getCartById(id);

    }

    @Override
    public int addCartByUserId(Integer userId) {
        //如果用户购物车不存在，则创建一个
        if (cartMapper.getCartByUserId(userId) == null) {
            // 创建购物车
            Cart cart = new Cart();
            cart.setUserId(userId);
            return cartMapper.addCart(cart);
        }
        // 如果用户购物车存在，则返回用户购物车
        Cart cart = new Cart();
        cart.setUserId(userId);
        return cartMapper.addCart(cart);
    }

    @Override
    public Cart getCartByUserId(Integer userId) {
        return cartMapper.getCartByUserId(userId);
    }

    @Override
    public int addCartItem(CartItem cartItem) {
        return cartMapper.addCartItem(cartItem);
    }

    @Override
    public int updateCartItem(CartItem cartItem) {
        return cartMapper.updateCartItem(cartItem);
    }

    @Override
    public int deleteCartItem(Integer itemId) {
        return cartMapper.deleteCartItem(itemId);
    }

}