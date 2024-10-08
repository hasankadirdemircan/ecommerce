package com.cornershop.ecommerce.repository;

import com.cornershop.ecommerce.model.Product;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p FROM Product p WHERE p.categoryId = :categoryId AND p.active = true")
    List<Product> findProductListByCategoryId(@Param("categoryId") Long categoryId);

    @Modifying
    @Transactional
    @Query("UPDATE Product p SET p.active = :active WHERE p.id = :id")
    void updateProductActive(@Param("active") Boolean isActive, @Param("id") Long id);

    @Query("SELECT p FROM Product p")
    List<Product> getAllProductList();

    @Query("SELECT COUNT(p) FROM Product p WHERE p.categoryId = :categoryId")
    Long getProductCountOfCategoryId(@Param("categoryId") Long categoryId);
}
