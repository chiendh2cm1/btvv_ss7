package com.codegym.model;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class ProductForm {
    private Long id;
    @NotEmpty
    @Size(min = 5, max = 20, message = "5 đến 10 ki tu")
    private String name;
    @NotNull(message = "khong de null")
    private double price;
    @NotEmpty(message = "khong de ")
    private String description;
    private MultipartFile image;
    private MultipartFile sound;
    private Category category;

    public ProductForm() {
    }

    public ProductForm(Long id, String name, double price, String description, MultipartFile image, MultipartFile sound, Category category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.image = image;
        this.sound = sound;
        this.category = category;
    }

    public ProductForm(String name, double price, String description, MultipartFile image, MultipartFile sound, Category category) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.image = image;
        this.sound = sound;
        this.category = category;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) {
        this.image = image;
    }

    public MultipartFile getSound() {
        return sound;
    }

    public void setSound(MultipartFile sound) {
        this.sound = sound;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
