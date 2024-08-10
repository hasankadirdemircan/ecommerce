package com.cornershop.ecommerce.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.cornershop.ecommerce.exception.CategoryDeleteException;
import com.cornershop.ecommerce.exception.ProductNotFoundException;
import com.cornershop.ecommerce.model.Product;
import com.cornershop.ecommerce.repository.ProductRepository;
import java.io.File;
import java.io.IOException;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.parameters.P;

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

    @Test
    void createProductUpdate_fail() {
        MockMultipartFile file = null;
        Product product = new Product();
        product.setId(1L);

        when(productRepository.findById(product.getId())).thenReturn(Optional.empty());

        ProductNotFoundException thrown = Assertions.assertThrows(ProductNotFoundException.class,
                () -> productService.createProduct(file, product));

        assertEquals("product not found id : 1", thrown.getMessage());
        verify(productRepository, times(1)).findById(product.getId());
        verify(productRepository, times(0)).save(product);
    }

    @Test
    void deleteProduct_success() throws IOException {
         String filePath = "uploads/test.txt";
        File  file =  new File(filePath);
        file.createNewFile();
        Long id = 1L;
        Product product = new Product();
        product.setId(id);
        product.setImage(filePath);
        when(productRepository.findById(id)).thenReturn(Optional.of(product));
        Mockito.doNothing().when(productRepository).deleteById(id);

        productService.deleteProduct(id);

        verify(productRepository, times(1)).findById(id);
        verify(productRepository, times(1)).deleteById(id);
    }
    //TODO: deleteProduct methounda ProductNotFoundException hatası atacak şekilde fail case'i test ediniz.
}
