package com.example.shoppinglist.controller;

import com.example.shoppinglist.entity.ShoppingItem;
import com.example.shoppinglist.service.ShoppingItemService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/items")
@CrossOrigin(origins = "*")
public class ShoppingItemController {

    private final ShoppingItemService service;

    @Autowired
    public ShoppingItemController(ShoppingItemService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<ShoppingItem>> getAllItems(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) BigDecimal minCost) {

        List<ShoppingItem> items;

        if (category != null) {
            items = service.findByCategory(category);
        } else if (search != null) {
            items = service.searchByName(search);
        } else if (minCost != null) {
            items = service.findItemsWithMinimumCost(minCost);
        } else {
            items = service.findAll();
        }

        return ResponseEntity.ok(items);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShoppingItem> getItemById(@PathVariable Long id) {
        ShoppingItem item = service.findById(id);
        return ResponseEntity.ok(item);
    }

    @PostMapping
    public ResponseEntity<ShoppingItem> createItem(@Valid @RequestBody ShoppingItem item) {
        ShoppingItem savedItem = service.save(item);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedItem);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ShoppingItem> updateItem(@PathVariable Long id,
                                                  @Valid @RequestBody ShoppingItem item) {
        ShoppingItem updatedItem = service.update(id, item);
        return ResponseEntity.ok(updatedItem);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/categories")
    public ResponseEntity<List<String>> getAllCategories() {
        List<String> categories = service.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/total-cost")
    public ResponseEntity<BigDecimal> getTotalCost() {
        BigDecimal totalCost = service.getTotalShoppingCost();
        return ResponseEntity.ok(totalCost);
    }
}