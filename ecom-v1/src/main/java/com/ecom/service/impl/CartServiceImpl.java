package com.ecom.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.ecom.model.Cart;
import com.ecom.model.Product;
import com.ecom.model.UserDtls;
import com.ecom.repository.CartRepository;
import com.ecom.repository.ProductRepository;
import com.ecom.repository.UserRepository;
import com.ecom.service.CartService;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Cart saveCart(Integer productId, Integer userId) {
        UserDtls user = userRepository.findById(userId).get();
        Product product = productRepository.findById(productId).get();

        Cart cartStatus = cartRepository.findByProductIdAndUserId(productId, userId);
        Cart cart;

        if (cartStatus == null) {
            cart = new Cart();
            cart.setProduct(product);
            cart.setUser(user);
            cart.setQuantity(1);
            cart.setTotalPrice(product.getDiscountPrice());
        } else {
            cart = cartStatus;
            cart.setQuantity(cart.getQuantity() + 1);
            cart.setTotalPrice(cart.getQuantity() * product.getDiscountPrice());
        }

        return cartRepository.save(cart);
    }

    @Override
    public List<Cart> getCartsByUser(Integer userId) {
        List<Cart> carts = cartRepository.findByUserId(userId);
        double totalOrderPrice = 0.0;

        for (Cart c : carts) {
            double totalPrice = c.getProduct().getDiscountPrice() * c.getQuantity();
            c.setTotalPrice(totalPrice);
            totalOrderPrice += totalPrice;
            c.setTotalOrderPrice(totalOrderPrice);
        }
        return carts;
    }

    @Override
    public Integer getCountCart(Integer userId) {
        return cartRepository.countByUserId(userId);
    }

    // -------------------- NEW: enforce ownership --------------------
    @Override
    public void updateQuantity(String action, Integer cartId, Integer userId) {
        Cart cart = cartRepository.findById(cartId)
                                  .orElseThrow(() -> new RuntimeException("Cart not found"));

        // Check ownership
        if (!cart.getUser().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized: Cart does not belong to user");
        }

        int quantity = cart.getQuantity();
        if ("de".equalsIgnoreCase(action)) {
            quantity--;
            if (quantity <= 0) {
                cartRepository.delete(cart);
                return;
            }
        } else {
            quantity++;
        }
        cart.setQuantity(quantity);
        cart.setTotalPrice(quantity * cart.getProduct().getDiscountPrice());
        cartRepository.save(cart);
    }

    @Override
    public void removeCartItem(Integer cartId, Integer userId) {
        Cart cart = cartRepository.findById(cartId)
                                  .orElseThrow(() -> new RuntimeException("Cart not found"));

        // Check ownership
        if (!cart.getUser().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized: Cart does not belong to user");
        }

        cartRepository.delete(cart);
    }
}

