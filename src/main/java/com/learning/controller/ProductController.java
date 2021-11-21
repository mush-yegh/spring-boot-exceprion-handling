package com.learning.controller;

import com.learning.model.Product;
import com.learning.service.ProductService;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/product")
public class ProductController {
  private final ProductService productService;

  public ProductController(ProductService productService) {
    this.productService = productService;
  }

  @GetMapping
  public ResponseEntity<List<Product>> getAll() {
    return new ResponseEntity<>(productService.getAll(), HttpStatus.OK);
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> getProductById(@PathVariable final Integer id) {
    final Optional<Product> optionalProduct = productService.findById(id);
    if (optionalProduct.isPresent()) {
      final Product product = optionalProduct.get();
      return new ResponseEntity<>(product, HttpStatus.OK);
    }
    String errorMessage = String.format("Product with id %d not found.", id);
    return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
  }

  @PostMapping
  public ResponseEntity<HttpStatus> save(
      @RequestBody final Product product, UriComponentsBuilder ucBuilder) {
    if (productService.getAll().contains(product)) {
      return new ResponseEntity<>(HttpStatus.CONFLICT);
    }
    productService.add(product);
    final HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setLocation(
        ucBuilder.path("/product/{id}").buildAndExpand(product.getId()).toUri());
    return new ResponseEntity<>(httpHeaders, HttpStatus.CREATED);
  }

  @PutMapping
  public ResponseEntity<HttpStatus> update(@RequestBody final Product product) {
    Optional<Product> optionalProduct = productService.findById(product.getId());
    if (optionalProduct.isPresent()) {
      productService.update(product);
      return new ResponseEntity<>(HttpStatus.OK);
    }
    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<HttpStatus> delete(@RequestParam final Integer id) {
    Optional<Product> optionalProduct = productService.findById(id);
    if (optionalProduct.isPresent()) {
      productService.delete(optionalProduct.get());
      return new ResponseEntity<>(HttpStatus.OK);
    }
    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
  }
}
