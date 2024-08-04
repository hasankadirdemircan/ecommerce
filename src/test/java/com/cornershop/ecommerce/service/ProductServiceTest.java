package com.cornershop.ecommerce.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.cornershop.ecommerce.model.Product;
import com.cornershop.ecommerce.repository.ProductRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;

public class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createProduct_successful() {
        MockMultipartFile firstFile = new MockMultipartFile("data", "filename.txt", "text/plain", "some xml".getBytes());
        Product product = new Product();
        product.setActive(true);
        product.setUnitsInStock(5L);
        product.setName("macbook");
        product.setPrice(5000D);
        product.setCategoryId(1L);
        product.setImage("uploads/macbook.txt");


        Product savedProduct = new Product();
        savedProduct.setId(1L);
        savedProduct.setActive(true);
        savedProduct.setUnitsInStock(5L);
        savedProduct.setName("macbook");
        savedProduct.setPrice(5000D);
        savedProduct.setCategoryId(1L);
        savedProduct.setImage("uploads/macbook.txt");

        when(productRepository.save(product)).thenReturn(savedProduct);

        Product response = productService.createProduct(firstFile, product);
        assertEquals(response.getName(), savedProduct.getName());
        assertEquals(response.getId(), savedProduct.getId());
        assertEquals(response.getImage(), savedProduct.getImage());
        verify(productRepository, times(1)).save(product);
        verify(productRepository, times(0)).findById(product.getId());
    }

    @Test
    void createProductUpdate_successful() {
        MockMultipartFile file = null;
        Product product = new Product();
        product.setId(1L);
        product.setActive(true);
        product.setUnitsInStock(5L);
        product.setName("macbook");
        product.setPrice(50002D);
        product.setCategoryId(1L);
        product.setImage("uploads/macbook.txt");

        Product savedProduct = new Product();
        savedProduct.setId(1L);
        savedProduct.setActive(true);
        savedProduct.setUnitsInStock(5L);
        savedProduct.setName("macbook");
        savedProduct.setPrice(5000D);
        savedProduct.setCategoryId(1L);
        savedProduct.setImage("uploads/macbook.txt");

        when(productRepository.findById(product.getId())).thenReturn(Optional.of(savedProduct));
        when(productRepository.save(product)).thenReturn(product);

        Product response = productService.createProduct(file, product);

        assertEquals(product.getImage(), response.getImage());
        assertEquals(product.getPrice(), response.getPrice());
        verify(productRepository, times(1)).findById(product.getId());
        verify(productRepository, times(1)).save(product);
    }

    //TODO:  createProduct methodundaki "else" kısmı için new ProductNotFoundException hatası fırlatacak şekilde fail case testi yazın.
    @Test
    void createProductUpdate_fail() {

    }

}
