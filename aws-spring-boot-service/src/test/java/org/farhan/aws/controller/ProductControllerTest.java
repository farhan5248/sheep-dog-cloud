package org.farhan.aws.controller;

import org.farhan.aws.model.Product;
import org.farhan.aws.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class ProductControllerTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    private Product product1;
    private Product product2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        product1 = new Product(1L, "Product 1", "Description 1", 10.0, 100);
        product2 = new Product(2L, "Product 2", "Description 2", 20.0, 200);
    }

    @Test
    void getAllProducts_ShouldReturnAllProducts() {
        // Arrange
        List<Product> products = Arrays.asList(product1, product2);
        when(productService.getAllProducts()).thenReturn(products);

        // Act
        ResponseEntity<List<Product>> response = productController.getAllProducts();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        verify(productService, times(1)).getAllProducts();
    }

    @Test
    void getProductById_WithExistingId_ShouldReturnProduct() {
        // Arrange
        when(productService.getProductById(1L)).thenReturn(Optional.of(product1));

        // Act
        ResponseEntity<Product> response = productController.getProductById(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Product 1", response.getBody().getName());
        verify(productService, times(1)).getProductById(1L);
    }

    @Test
    void getProductById_WithNonExistingId_ShouldReturnNotFound() {
        // Arrange
        when(productService.getProductById(3L)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Product> response = productController.getProductById(3L);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(productService, times(1)).getProductById(3L);
    }

    @Test
    void createProduct_ShouldReturnCreatedProduct() {
        // Arrange
        Product newProduct = new Product(null, "New Product", "New Description", 30.0, 300);
        Product savedProduct = new Product(3L, "New Product", "New Description", 30.0, 300);
        when(productService.saveProduct(newProduct)).thenReturn(savedProduct);

        // Act
        ResponseEntity<Product> response = productController.createProduct(newProduct);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(3L, response.getBody().getId());
        verify(productService, times(1)).saveProduct(newProduct);
    }
}
