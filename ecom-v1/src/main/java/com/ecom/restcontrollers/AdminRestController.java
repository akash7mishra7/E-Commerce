package com.ecom.restcontrollers;

import com.ecom.model.Category;
import com.ecom.model.Product;
import com.ecom.model.ProductOrder;
import com.ecom.model.UserDtls;
import com.ecom.service.CategoryService;
import com.ecom.service.OrderService;
import com.ecom.service.ProductService;
import com.ecom.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')") // ✅ protect all endpoints
public class AdminRestController {

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private CategoryService categoryService;

    // -------------------- PRODUCT MANAGEMENT --------------------

    @PostMapping("/products")
    public ResponseEntity<Product> addProduct(@RequestBody Product product) {
        return ResponseEntity.ok(productService.saveProduct(product));
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Integer id, @RequestBody Product product) {
        Product existing = productService.getProductById(id);
        if (existing == null) return ResponseEntity.notFound().build();
        Product updated = productService.updateProduct(product, null); // handle MultipartFile if needed
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Integer id) {
        Boolean deleted = productService.deleteProduct(id);
        if (!deleted) return ResponseEntity.notFound().build();
        return ResponseEntity.ok("Product deleted successfully");
    }

    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    // -------------------- USER MANAGEMENT --------------------

    @GetMapping("/users")
    public ResponseEntity<List<UserDtls>> getAllUsers(@RequestParam(required = false) String role) {
        List<UserDtls> users = userService.getUsers(role);
        return ResponseEntity.ok(users);
    }

    @PutMapping("/users/{id}/enable")
    public ResponseEntity<?> enableUser(@PathVariable Integer id) {
        Boolean updated = userService.updateAccountStatus(id, true);
        if (!updated) return ResponseEntity.notFound().build();
        return ResponseEntity.ok("User enabled");
    }

    @PutMapping("/users/{id}/disable")
    public ResponseEntity<?> disableUser(@PathVariable Integer id) {
        Boolean updated = userService.updateAccountStatus(id, false);
        if (!updated) return ResponseEntity.notFound().build();
        return ResponseEntity.ok("User disabled");
    }

    // -------------------- ORDER MANAGEMENT --------------------

    @GetMapping("/orders/all")
    public ResponseEntity<List<ProductOrder>> allOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @PutMapping("/orders/{orderId}/status")
    public ResponseEntity<?> updateStatus(@PathVariable Integer orderId, @RequestParam String status) {
        orderService.updateOrderStatus(orderId, status);
        return ResponseEntity.ok("Order status updated");
    }

    // -------------------- CATEGORY MANAGEMENT --------------------

    @PostMapping("/categories")
    public ResponseEntity<?> saveCategory(@RequestBody Category category) {
        if (categoryService.existCategory(category.getName())) {
            return ResponseEntity.badRequest().body("Category with this name already exists");
        }
        return ResponseEntity.ok(categoryService.saveCategory(category));
    }

    @GetMapping("/categories")
    public ResponseEntity<List<Category>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategory());
    }

    @GetMapping("/categories/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable int id) {
        Category category = categoryService.getCategoryById(id);
        if (category == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(category);
    }

    @DeleteMapping("/categories/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable int id) {
        Boolean deleted = categoryService.deleteCategory(id);
        if (!deleted) return ResponseEntity.notFound().build();
        return ResponseEntity.ok("Category deleted successfully");
    }

    @GetMapping("/categories/active")
    public ResponseEntity<List<Category>> getAllActiveCategories() {
        return ResponseEntity.ok(categoryService.getAllActiveCategory());
    }

    @GetMapping("/categories/page")
    public ResponseEntity<Page<Category>> getCategoriesPaginated(
            @RequestParam Integer pageNo, @RequestParam Integer pageSize) {
        return ResponseEntity.ok(categoryService.getAllCategorPagination(pageNo, pageSize));
    }
}

