package com.example.shoppinglist.controller;

import com.example.shoppinglist.entity.ShoppingItem;
import com.example.shoppinglist.repository.ShoppingItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-test.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ShoppingItemControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ShoppingItemRepository repository;

    private String baseUrl;
    private ShoppingItem testItem;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/api/items";
        repository.deleteAll();
        testItem = new ShoppingItem("Test Item", new BigDecimal("10.99"), 2, "Test Category");
        testItem = repository.save(testItem);
    }

    @Test
    void getAllItems_ShouldReturnAllItems() {
        ResponseEntity<ShoppingItem[]> response = restTemplate.getForEntity(baseUrl, ShoppingItem[].class);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody()[0].getName()).isEqualTo("Test Item");
        assertThat(response.getBody()[0].getPrice()).isEqualTo(new BigDecimal("10.99"));
        assertThat(response.getBody()[0].getQuantity()).isEqualTo(2);
    }

    @Test
    void getItemById_WhenExists_ShouldReturnItem() {
        ResponseEntity<ShoppingItem> response = restTemplate.getForEntity(baseUrl + "/" + testItem.getId(), ShoppingItem.class);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody().getName()).isEqualTo("Test Item");
        assertThat(response.getBody().getPrice()).isEqualTo(new BigDecimal("10.99"));
    }

    @Test
    void getItemById_WhenNotExists_ShouldReturn404() {
        ResponseEntity<String> response = restTemplate.getForEntity(baseUrl + "/999", String.class);

        assertThat(response.getStatusCode().is4xxClientError()).isTrue();
    }

    @Test
    void createItem_WithValidData_ShouldReturnCreated() {
        ShoppingItem newItem = new ShoppingItem("New Item", new BigDecimal("15.50"), 1, "New Category");

        ResponseEntity<ShoppingItem> response = restTemplate.postForEntity(baseUrl, newItem, ShoppingItem.class);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody().getName()).isEqualTo("New Item");
        assertThat(response.getBody().getPrice()).isEqualTo(new BigDecimal("15.50"));
    }

    @Test
    void deleteItem_WhenExists_ShouldDelete() {
        restTemplate.delete(baseUrl + "/" + testItem.getId());

        ResponseEntity<String> response = restTemplate.getForEntity(baseUrl + "/" + testItem.getId(), String.class);
        assertThat(response.getStatusCode().is4xxClientError()).isTrue();
    }

    @Test
    void getTotalCost_ShouldReturnCorrectTotal() {
        ResponseEntity<BigDecimal> response = restTemplate.getForEntity(baseUrl + "/total-cost", BigDecimal.class);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isGreaterThan(BigDecimal.ZERO);
    }
}