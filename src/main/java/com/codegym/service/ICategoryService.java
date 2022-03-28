package com.codegym.service;

import com.codegym.model.Category;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ICategoryService extends IGeneralService<Category> {
    Page<Category> findAll(Pageable pageable);
    Page<Category> findAllByNameContaining(String name, Pageable pageable);
    void deleteCategory(Long id);
}
