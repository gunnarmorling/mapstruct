package org.mapstruct.itest.jaxb;

import java.util.List;


public class ProductEntity {

    private String name;
    private CategoryEntity category;
    private List<CategoryEntity> allCategories;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CategoryEntity getCategory() {
        return category;
    }

    public void setCategory(CategoryEntity category) {
        this.category = category;
    }

    public List<CategoryEntity> getAllCategories() {
        return allCategories;
    }

    public void setAllCategories(List<CategoryEntity> allCategories) {
        this.allCategories = allCategories;
    }
}
