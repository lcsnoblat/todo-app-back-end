package com.example.shoppinglist.config;

import com.example.shoppinglist.entity.ShoppingItem;
import com.example.shoppinglist.repository.ShoppingItemRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataLoader.class);
    private final ShoppingItemRepository repository;
    private final ObjectMapper objectMapper;

    @Autowired
    public DataLoader(ShoppingItemRepository repository, ObjectMapper objectMapper) {
        this.repository = repository;
        this.objectMapper = objectMapper;
    }

    @Override
    public void run(String... args) throws Exception {
        if (repository.count() == 0) {
            loadSampleData();
        } else {
            logger.info("Database already contains data, skipping sample data loading");
        }
    }

    private void loadSampleData() {
        try (InputStream inputStream = new ClassPathResource("sample-data.json").getInputStream()) {
            List<ShoppingItem> items = objectMapper.readValue(inputStream, new TypeReference<List<ShoppingItem>>() {});
            repository.saveAll(items);
            logger.info("Loaded {} sample shopping items", items.size());
        } catch (IOException e) {
            logger.error("Failed to load sample data", e);
        }
    }
}