package com.codegym.repository;

import com.codegym.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IProductRepository extends PagingAndSortingRepository<Product, Long> {
    Page<Product> findByNameContaining(String name, Pageable pageable);

    @Query(value = "select product.*, category.name from product join category on product.category_id = category.id where category_id = ?1", nativeQuery = true)
    Page<Product> getProductWithNameSQL(Long id, Pageable pageable);
}
