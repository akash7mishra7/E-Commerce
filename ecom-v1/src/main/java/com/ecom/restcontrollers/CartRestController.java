package com.ecom.restcontrollers;

import com.ecom.config.CustomUser;
import com.ecom.model.Cart;
import com.ecom.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user/cart")
@PreAuthorize("hasRole('USER')") // All endpoints require USER role
public class CartRestController {

    @Autowired
    private CartService cartService;

    // ✅ Add item to cart
    @PostMapping("/add/{productId}")
    public ResponseEntity<Cart> addToCart(@PathVariable Integer productId,
                                          @AuthenticationPrincipal CustomUser user) {
        Cart cart = cartService.saveCart(productId, user.getId());
        return ResponseEntity.ok(cart);
    }

    // ✅ Get all carts for the logged-in user
    @GetMapping
    public ResponseEntity<List<Cart>> getUserCart(@AuthenticationPrincipal CustomUser user) {
        return ResponseEntity.ok(cartService.getCartsByUser(user.getId()));
    }

    // ✅ Get total cart item count
    @GetMapping("/count")
    public ResponseEntity<Integer> getCount(@AuthenticationPrincipal CustomUser user) {
        return ResponseEntity.ok(cartService.getCountCart(user.getId()));
    }

    // ✅ Update quantity (increase or decrease)
    @PutMapping("/update/{cartId}")
    public ResponseEntity<String> updateQuantity(@PathVariable Integer cartId,
                                                 @RequestParam String action,
                                                 @AuthenticationPrincipal CustomUser user) {
        cartService.updateQuantity(action, cartId, user.getId());
        return ResponseEntity.ok("Cart updated successfully");
    }

    // ✅ Remove cart item completely
    @DeleteMapping("/remove/{cartId}")
    public ResponseEntity<String> removeCart(@PathVariable Integer cartId,
                                             @AuthenticationPrincipal CustomUser user) {
        cartService.removeCartItem(cartId, user.getId());
        return ResponseEntity.ok("Cart item removed successfully");
    }
}

