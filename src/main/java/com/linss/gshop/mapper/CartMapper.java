package com.linss.gshop.mapper;

import com.linss.gshop.entity.Cart;
import com.linss.gshop.entity.CartItem;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CartMapper {


    List<Cart> getCart();

    Cart getCartById(Integer id);

    Cart getCartByUserId(Integer userId);

    int addCartItem(CartItem cartItem);

    int updateCartItem(CartItem cartItem);

    int deleteCartItem(Integer itemId);

    int addCart(Cart cart);
}