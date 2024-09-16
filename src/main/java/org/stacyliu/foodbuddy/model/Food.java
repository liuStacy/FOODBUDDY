package org.stacyliu.foodbuddy.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class Food {
    private String category;
    private String name;
    private int quantity;
    private LocalDate expiryDate;

    // Constructor

    public Food() {
    }

    public Food(String category, String name, int quantity, LocalDate expiryDate) {
        this.category = category;
        this.name = name;
        this.quantity = quantity;
        this.expiryDate = expiryDate;
    }

    // Getters and Setters for each field
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;  // Check if comparing the same object
        if (o == null || getClass() != o.getClass()) return false;  // Check type

        Food food = (Food) o;
        // Check category, name, and expiryDate
        return Objects.equals(category, food.category) &&
                Objects.equals(name, food.name) &&
                Objects.equals(expiryDate, food.expiryDate);
    }

    // Overriding hashCode method to ensure compatibility with equals
    @Override
    public int hashCode() {
        return Objects.hash(category, name, expiryDate);
    }

    @JsonIgnore
    public int getDaysLeft() {
        if (expiryDate == null) {
            return 0;
        }
        return (int) ChronoUnit.DAYS.between(LocalDate.now(), expiryDate);
    }
}
