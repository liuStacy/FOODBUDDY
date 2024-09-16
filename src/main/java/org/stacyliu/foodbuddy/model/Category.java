package org.stacyliu.foodbuddy.model;

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
}
