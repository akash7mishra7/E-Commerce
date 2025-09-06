package com.ecom.restcontrollers;

import com.ecom.config.CustomUser;
import com.ecom.model.OrderRequest;
import com.ecom.model.ProductOrder;
import com.ecom.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user/orders")
@PreAuthorize("hasRole('USER')") // All endpoints require USER role
public class OrderRestController {

    @Autowired
    private OrderService orderService;

    // 🔒 Place order
    @PostMapping("/place")
    public ResponseEntity<?> placeOrder(@RequestBody OrderRequest req,
                                        @AuthenticationPrincipal CustomUser user) throws Exception {
        orderService.saveOrder(user.getId(), req);
        return ResponseEntity.ok("Order placed successfully");
    }

    // 🔒 View my orders
    @GetMapping("/my")
    public ResponseEntity<List<ProductOrder>> myOrders(@AuthenticationPrincipal CustomUser user) {
        return ResponseEntity.ok(orderService.getOrdersByUser(user.getId()));
    }
}
