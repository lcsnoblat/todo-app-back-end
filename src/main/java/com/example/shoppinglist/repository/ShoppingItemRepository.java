package com.example.shoppinglist.repository;

import com.example.shoppinglist.entity.ShoppingItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ShoppingItemRepository extends JpaRepository<ShoppingItem, Long> {

    List<ShoppingItem> findByCategory(String category);

    List<ShoppingItem> findByNameContainingIgnoreCase(String name);

    @Query("SELECT s FROM ShoppingItem s WHERE s.price * s.quantity >= :minCost")
    List<ShoppingItem> findItemsWithMinimumCost(@Param("minCost") BigDecimal minCost);

    @Query("SELECT DISTINCT s.category FROM ShoppingItem s ORDER BY s.category")
    List<String> findAllCategories();

    @Query("SELECT SUM(s.price * s.quantity) FROM ShoppingItem s")
    BigDecimal getTotalShoppingCost();
}