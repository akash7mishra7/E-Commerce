package com.ecom.service;

import java.util.List;

import com.ecom.model.Cart;

public interface CartService {

	public Cart saveCart(Integer productId, Integer userId);

	public List<Cart> getCartsByUser(Integer userId);
	
	public Integer getCountCart(Integer userId);

	void updateQuantity(String action, Integer cartId, Integer userId);
	
    void removeCartItem(Integer cartId, Integer userId);

}
