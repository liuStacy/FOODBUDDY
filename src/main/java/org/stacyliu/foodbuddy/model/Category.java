package org.stacyliu.foodbuddy.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Category {
    private String category;
    private List<Category> subcategories;
    private Integer daysToExpire;
    // Getters and Setters
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<Category> getSubcategories() {
        return subcategories;
    }

    public void setSubcategories(List<Category> subcategories) {
        this.subcategories = subcategories;
    }

    public Integer getDaysToExpire() {
        return daysToExpire;
    }

    public void setDaysToExpire(Integer daysToExpire) {
        this.daysToExpire = daysToExpire;
    }

    public Integer getDaysToExpire(String category) {
        if (this.category.equals(category)) {
            return this.daysToExpire;
        }

        if (subcategories != null) {
            for (Category subcategory : subcategories) {
                Integer days = subcategory.getDaysToExpire(category);
                if (days != null) {
                    return days;
                }
            }
        }

        return null;
    }

    public static Category loadCategoryJsonData() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        try {
            return mapper.readValue(new File("src/main/resources/org/stacyliu/foodbuddy/Data/FoodCategory.json"), Category.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
