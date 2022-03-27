package com.codegym.controller;

import com.codegym.model.Category;
import com.codegym.model.Product;
import com.codegym.service.ICategoryService;
import com.codegym.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Controller
public class CategoryController {
    @Autowired
    ICategoryService categoryService;
    @Autowired
    IProductService productService;

    @GetMapping("/categories/list")
    public ModelAndView ShowList(@RequestParam(name = "name") Optional<String> name, @PageableDefault(value = 10) Pageable pageable) {
        Page<Category> categories;
        categories = categoryService.findAll(pageable);
        if (name.isPresent()) {
            categories = categoryService.findAllByNameContaining(name.get(), pageable);
        }
        ModelAndView modelAndView = new ModelAndView("/category/list");
        modelAndView.addObject("categories", categories);
        return modelAndView;
    }

    @GetMapping("/categories/viewByCategory/{id}")
    public ModelAndView ShowProductByCategoryId(@PathVariable Long id, Pageable pageable) {
        Page<Product> products;
        products = productService.getProductWithName(id, pageable);
        ModelAndView modelAndView = new ModelAndView("/product/listProductByCategory");
        modelAndView.addObject("products", products);
        return modelAndView;
    }

    @GetMapping("/categories/create")
    public ModelAndView ShowDeleteCategory() {
        ModelAndView modelAndView = new ModelAndView("/category/create");
        modelAndView.addObject("category", new Category());
        return modelAndView;
    }

    @PostMapping("/categories/create")
    public ModelAndView CreateCategory(@ModelAttribute Category category) {
        categoryService.save(category);
        return new ModelAndView("redirect:/categories/list");
    }

    @GetMapping("/categories/delete/{id}")
    public ModelAndView ShowDeleteCategory(@PathVariable Long id) {
        Optional<Category> category = categoryService.findById(id);
        if (!category.isPresent()) {
            return new ModelAndView("/error-404");
        }
        ModelAndView modelAndView = new ModelAndView("/category/delete");
        modelAndView.addObject("category", category.get());
        return modelAndView;
    }

    @PostMapping("/categories/delete/{id}")
    public ModelAndView DeleteCategory(@PathVariable Long id) {
        Optional<Category> category = categoryService.findById(id);
        if (!category.isPresent()) {
            return new ModelAndView("/error-404");
        }
        categoryService.deleteCategory(id);
        return new ModelAndView("redirect:/categories/list");
    }

    @GetMapping("categories/edit/{id}")
    public ModelAndView ShowEdiCategory(@PathVariable Long id) {
        Optional<Category> category = categoryService.findById(id);
        if (!category.isPresent()) {
            return new ModelAndView("/error-404");
        }
        ModelAndView modelAndView = new ModelAndView("/category/edit");
        modelAndView.addObject("category", category.get());
        return modelAndView;
    }

    @PostMapping("/categories/edit/{id}")
    public ModelAndView EditCategory(@ModelAttribute Category category) {
        categoryService.save(category);
        return new ModelAndView("redirect:/categories/list");
    }
}
