package com.example.shoppinglist.service;

import com.example.shoppinglist.entity.ShoppingItem;
import com.example.shoppinglist.exception.ItemNotFoundException;
import com.example.shoppinglist.repository.ShoppingItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShoppingItemServiceTest {

    @Mock
    private ShoppingItemRepository repository;

    @InjectMocks
    private ShoppingItemService service;

    private ShoppingItem testItem;

    @BeforeEach
    void setUp() {
        testItem = new ShoppingItem("Test Item", new BigDecimal("10.00"), 2, "Test Category");
        testItem.setId(1L);
    }

    @Test
    void findAll_ShouldReturnAllItems() {
        List<ShoppingItem> items = Arrays.asList(testItem);
        when(repository.findAll()).thenReturn(items);

        List<ShoppingItem> result = service.findAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(testItem);
        verify(repository).findAll();
    }

    @Test
    void findById_WhenItemExists_ShouldReturnItem() {
        when(repository.findById(1L)).thenReturn(Optional.of(testItem));

        ShoppingItem result = service.findById(1L);

        assertThat(result).isEqualTo(testItem);
        verify(repository).findById(1L);
    }

    @Test
    void findById_WhenItemNotExists_ShouldThrowException() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(1L))
                .isInstanceOf(ItemNotFoundException.class)
                .hasMessage("Shopping item not found with id: 1");
    }

    @Test
    void save_ShouldReturnSavedItem() {
        when(repository.save(any(ShoppingItem.class))).thenReturn(testItem);

        ShoppingItem result = service.save(testItem);

        assertThat(result).isEqualTo(testItem);
        verify(repository).save(testItem);
    }

    @Test
    void update_WhenItemExists_ShouldReturnUpdatedItem() {
        ShoppingItem updatedData = new ShoppingItem("Updated Item", new BigDecimal("15.00"), 3, "Updated Category");
        when(repository.findById(1L)).thenReturn(Optional.of(testItem));
        when(repository.save(any(ShoppingItem.class))).thenReturn(testItem);

        ShoppingItem result = service.update(1L, updatedData);

        assertThat(result.getName()).isEqualTo("Updated Item");
        verify(repository).findById(1L);
        verify(repository).save(testItem);
    }

    @Test
    void deleteById_WhenItemExists_ShouldDeleteSuccessfully() {
        when(repository.existsById(1L)).thenReturn(true);

        assertThatNoException().isThrownBy(() -> service.deleteById(1L));

        verify(repository).existsById(1L);
        verify(repository).deleteById(1L);
    }

    @Test
    void deleteById_WhenItemNotExists_ShouldThrowException() {
        when(repository.existsById(1L)).thenReturn(false);

        assertThatThrownBy(() -> service.deleteById(1L))
                .isInstanceOf(ItemNotFoundException.class)
                .hasMessage("Shopping item not found with id: 1");
    }

    @Test
    void getTotalShoppingCost_ShouldReturnTotalCost() {
        BigDecimal expectedCost = new BigDecimal("100.00");
        when(repository.getTotalShoppingCost()).thenReturn(expectedCost);

        BigDecimal result = service.getTotalShoppingCost();

        assertThat(result).isEqualTo(expectedCost);
        verify(repository).getTotalShoppingCost();
    }

    @Test
    void getTotalShoppingCost_WhenNull_ShouldReturnZero() {
        when(repository.getTotalShoppingCost()).thenReturn(null);

        BigDecimal result = service.getTotalShoppingCost();

        assertThat(result).isEqualTo(BigDecimal.ZERO);
    }
}