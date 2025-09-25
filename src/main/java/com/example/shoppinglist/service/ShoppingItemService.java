package com.example.shoppinglist.service;

import com.example.shoppinglist.entity.ShoppingItem;
import com.example.shoppinglist.exception.ItemNotFoundException;
import com.example.shoppinglist.repository.ShoppingItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ShoppingItemService {

    private final ShoppingItemRepository repository;

    @Autowired
    public ShoppingItemService(ShoppingItemRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<ShoppingItem> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public ShoppingItem findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("Shopping item not found with id: " + id));
    }

    public ShoppingItem save(ShoppingItem item) {
        return repository.save(item);
    }

    public ShoppingItem update(Long id, ShoppingItem updatedItem) {
        ShoppingItem existingItem = findById(id);

        existingItem.setName(updatedItem.getName());
        existingItem.setPrice(updatedItem.getPrice());
        existingItem.setQuantity(updatedItem.getQuantity());
        existingItem.setCategory(updatedItem.getCategory());

        return repository.save(existingItem);
    }

    public void deleteById(Long id) {
        if (!repository.existsById(id)) {
            throw new ItemNotFoundException("Shopping item not found with id: " + id);
        }
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<ShoppingItem> findByCategory(String category) {
        return repository.findByCategory(category);
    }

    @Transactional(readOnly = true)
    public List<ShoppingItem> searchByName(String name) {
        return repository.findByNameContainingIgnoreCase(name);
    }

    @Transactional(readOnly = true)
    public List<ShoppingItem> findItemsWithMinimumCost(BigDecimal minCost) {
        return repository.findItemsWithMinimumCost(minCost);
    }

    @Transactional(readOnly = true)
    public List<String> getAllCategories() {
        return repository.findAllCategories();
    }

    @Transactional(readOnly = true)
    public BigDecimal getTotalShoppingCost() {
        return Optional.ofNullable(repository.getTotalShoppingCost()).orElse(BigDecimal.ZERO);
    }
}