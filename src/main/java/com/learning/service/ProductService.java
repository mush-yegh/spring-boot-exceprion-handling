package com.learning.service;

import com.learning.model.Product;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class ProductService {
  private static final Logger LOGGER = LoggerFactory.getLogger(ProductService.class);
  private static final AtomicInteger COUNTER = new AtomicInteger();
  private static final List<Product> PRODUCTS;

  static {
    PRODUCTS = populateProducts();
  }

  public List<Product> getAll() {
    return PRODUCTS;
  }

  public Optional<Product> findById(final Integer id) {
    if (id < 1) {
      throw new IllegalArgumentException("Invalid product id: " + id);
    }
    for (final Product product : PRODUCTS) {
      if (product.getId() == id) {
        LOGGER.info("Product with id {}, successfully fetched", id);
        return Optional.of(product);
      }
    }
    LOGGER.warn("Product with id {} not found", id);
    return Optional.empty();
  }

  public void add(final Product product) {
    if (product == null) {
      throw new IllegalArgumentException("Invalid Product data");
    }
    product.setId(COUNTER.incrementAndGet());
    PRODUCTS.add(product);
    LOGGER.info("Product with id {} successfully added", product.getId());
  }

  public void update(final Product product) {
    final int index = PRODUCTS.indexOf(product);
    if (product == null || index == -1) {
      throw new IllegalArgumentException("Invalid Product data");
    }
    PRODUCTS.set(index, product);
    LOGGER.info("Product with id {} successfully updated.", product.getId());
  }

  public void delete(final Product product) {
    final int index = PRODUCTS.indexOf(product);
    if (product == null || index == -1) {
      throw new IllegalArgumentException("Invalid Product data");
    }
    PRODUCTS.remove(index);
    LOGGER.info("Product with id {} successfully deleted.", product.getId());
  }

  private static List<Product> populateProducts() {
    final List<Product> products = new ArrayList<>();
    products.add(new Product(COUNTER.incrementAndGet(), "Banana", 1.23));
    products.add(new Product(COUNTER.incrementAndGet(), "Cherry", 2.34));
    products.add(new Product(COUNTER.incrementAndGet(), "Pear", 3.45));
    return products;
  }
}
