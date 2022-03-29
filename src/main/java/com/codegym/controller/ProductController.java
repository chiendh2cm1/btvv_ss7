package com.codegym.controller;

import com.codegym.model.Category;
import com.codegym.model.Product;
import com.codegym.model.ProductForm;
import com.codegym.service.ICategoryService;
import com.codegym.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

@Controller
public class ProductController {
    @Autowired
    IProductService productService;
    @Autowired
    ICategoryService categoryService;
    @Value("${file-upload}")
    String uploadPath;

    @ModelAttribute(name = "categories")
    public Iterable<Category> categories() {
        return categoryService.findAll();
    }

    @GetMapping("/products/list")
    public ModelAndView ShowList(@RequestParam(name = "name") Optional<String> name, @PageableDefault(value = 5) Pageable pageable) {
        Page<Product> products;
        ModelAndView modelAndView = new ModelAndView("/product/list");
        if (!name.isPresent()) {
            products = productService.findAll(pageable);
        } else {
            products = productService.findAllByNameContaining(name.get(), pageable);
            modelAndView.addObject("name", name.get());
        }
        modelAndView.addObject("products", products);
        return modelAndView;
    }

    @GetMapping("/products/create")
    public ModelAndView ShowDeleteProduct() {
        ModelAndView modelAndView = new ModelAndView("/product/create");
        modelAndView.addObject("product", new ProductForm());
        return modelAndView;
    }



    @PostMapping(value = "/products/create", produces = "application/x-www-urlencoded;charset=UTF-8")
    public ModelAndView CreateProduct(@Valid @ModelAttribute("product") ProductForm productForm, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()){
            return new ModelAndView("/product/create");
        }
        String fileName = productForm.getImage().getOriginalFilename();
        Long currenTime = System.currentTimeMillis();
        fileName = currenTime + fileName;

        try {
            FileCopyUtils.copy(productForm.getImage().getBytes(), new File(uploadPath + fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Product product = new Product(productForm.getName(), productForm.getPrice(), productForm.getDescription(), fileName, productForm.getCategory());
        productService.save(product);
        return new ModelAndView("redirect:/products/list");
    }

    @GetMapping("/products/delete/{id}")
    public ModelAndView ShowDeleteProduct(@PathVariable Long id) {
        Optional<Product> product = productService.findById(id);
        if (!product.isPresent()) {
            return new ModelAndView("/error-404");
        }
        ModelAndView modelAndView = new ModelAndView("/product/delete");
        modelAndView.addObject("product", product.get());
        return modelAndView;
    }

    @PostMapping("/products/delete/{id}")
    public ModelAndView DeleteProduct(@PathVariable Long id) {
        Optional<Product> product = productService.findById(id);
        if (!product.isPresent()) {
            return new ModelAndView("/error-404");
        }
        File file = new File(uploadPath + product.get().getImage());
        if (file.exists()) {
            file.delete();
        }
        productService.remove(id);
        return new ModelAndView("redirect:/products/list");
    }

    @GetMapping("products/edit/{id}")
    public ModelAndView ShowEdiProduct(@PathVariable Long id) {
        Optional<Product> product = productService.findById(id);
        if (!product.isPresent()) {
            return new ModelAndView("/error-404");
        }
        ModelAndView modelAndView = new ModelAndView("/product/edit");
        modelAndView.addObject("product", product.get());
        return modelAndView;
    }

    @PostMapping("/products/edit/{id}")
    public ModelAndView EditProduct(@PathVariable Long id, @ModelAttribute("product") ProductForm productForm) {
        Optional<Product> oldProduct = productService.findById(id);
        if (!oldProduct.isPresent()) {
            return new ModelAndView("/error-404");
        }
        Product product = oldProduct.get();
        MultipartFile multipartFile = productForm.getImage();
        if (multipartFile.getSize() != 0) {
            File file = new File(uploadPath + product.getImage());
            if (file.exists()) {
                file.delete();
            }
            String fileName = productForm.getImage().getOriginalFilename();
            Long currenTime = System.currentTimeMillis();
            fileName = currenTime + fileName;
            product.setImage(fileName);
            try {
                FileCopyUtils.copy(multipartFile.getBytes(), new File(uploadPath + fileName));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        product.setName(productForm.getName());
        product.setPrice(productForm.getPrice());
        product.setDescription(productForm.getDescription());
        product.setCategory(productForm.getCategory());
        productService.save(product);
        return new ModelAndView("redirect:/products/list");
    }

    @GetMapping("/products/view/{id}")
    public ModelAndView showViewProduct(@PathVariable Long id) {
        Optional<Product> product = productService.findById(id);
        if (!product.isPresent()) {
            return new ModelAndView("/error-404");
        }
        ModelAndView modelAndView = new ModelAndView("/product/view");
        modelAndView.addObject("product", product.get());
        return modelAndView;
    }

}
