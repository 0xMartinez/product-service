package com.example.cassandra;

import com.example.cassandra.exception.ResourceNotFoundException;
import com.example.cassandra.repository.ProductRepository;
import com.example.cassandra.repository.model.Product;
import com.example.cassandra.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    private final static String PRODUCT_NAME = "item name";
    private final static String PRODUCT_DESCRIPTION = "item description";
    private final static BigDecimal PRODUCT_PRICE = BigDecimal.valueOf(1000);
    private final static String PRODUCT_CATEGORY = "item category";

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product testProduct;

    @BeforeEach
    void setUp() {
        testProduct = new Product();
        testProduct.setId(UUID.randomUUID());
        testProduct.setName(PRODUCT_NAME);
        testProduct.setDescription(PRODUCT_DESCRIPTION);
        testProduct.setPrice(PRODUCT_PRICE);
        testProduct.setCategory(PRODUCT_CATEGORY);
    }

    @Test
    void shouldReturnAllProducts() {
        // Given
        when(productRepository.findAll()).thenReturn(List.of(testProduct));

        // When
        List<Product> products = productService.getAllProducts();

        // Then
        assertEquals(1, products.size());
        assertEquals(PRODUCT_NAME, products.get(0).getName());

        verify(productRepository, times(1)).findAll();
    }

    @Test
    void shouldReturnProductById() {
        // Given
        when(productRepository.findById(testProduct.getId())).thenReturn(Optional.of(testProduct));

        // When
        Product foundProduct = productService.getProductById(testProduct.getId());

        // Then
        assertNotNull(foundProduct);
        assertEquals(PRODUCT_NAME, foundProduct.getName());
        assertEquals(PRODUCT_PRICE, foundProduct.getPrice());

        verify(productRepository, times(1)).findById(testProduct.getId());
    }

    @Test
    void shouldThrowExceptionWhenProductNotFound() {
        // Given
        UUID randomId = UUID.randomUUID();
        when(productRepository.findById(randomId)).thenReturn(Optional.empty());

        // When & Then
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->
                productService.getProductById(randomId));

        assertEquals("Product not found", exception.getMessage());
        verify(productRepository, times(1)).findById(randomId);
    }

    @Test
    void shouldSaveProduct() {
        // Given
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);

        // When
        Product savedProduct = productService.saveProduct(testProduct);

        // Then
        assertNotNull(savedProduct);
        assertEquals(PRODUCT_NAME, savedProduct.getName());

        verify(productRepository, times(1)).save(testProduct);
    }

    @Test
    void shouldDeleteProduct() {
        // Given
        UUID productId = testProduct.getId();
        doNothing().when(productRepository).deleteById(productId);

        // When
        productService.deleteProduct(productId);

        // Then
        verify(productRepository, times(1)).deleteById(productId);
    }
}
