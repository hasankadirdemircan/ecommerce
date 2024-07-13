package com.cornershop.ecommerce.repository;

import com.cornershop.ecommerce.model.Product;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p FROM Product p WHERE p.categoryId = :categoryId")
    List<Product> findProductListByCategoryId(@Param("categoryId") Long categoryId);

    @Query("UPDATE FROM Product p SET p.active = :active WHERE p.id = :id")
    Boolean updateProductActive(@Param("active") Boolean isActive, @Param("id") Long id);
}
