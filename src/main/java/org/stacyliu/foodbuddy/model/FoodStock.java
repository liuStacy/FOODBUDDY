package org.stacyliu.foodbuddy.model;

import java.io.File;
import java.io.IOException;
import java.util.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class FoodStock {
    private static final String FILE_PATH = "src/main/resources/org/stacyliu/foodbuddy/Data/FoodStock.json";
    // Eager Initialization
    private static final FoodStock INSTANCE = new FoodStock();

    // Static block here to load data during class initialization
    static {
        INSTANCE.loadFromFile();
    }
    private List<Food> foodList;

    private FoodStock() {
        this.foodList = new ArrayList<>();
    }

    public static FoodStock getInstance() {
        return INSTANCE;
    }

    public List<Food> getFoodList() {
        return foodList;
    }

    public void setFoodList(List<Food> foodList) {
        this.foodList = foodList;
    }

    public void addFood(Food newFood) {
        boolean foodMerged = false;
        for (int i = 0; i < foodList.size(); i++) {
            Food existingFood = foodList.get(i);

            if (existingFood.equals(newFood)) {
                existingFood.setQuantity(existingFood.getQuantity() + newFood.getQuantity());
                foodMerged = true;
                break;
            }
        }

        if (!foodMerged) {
            foodList.add(newFood);
        }
        sortFoodByExpiryDate();
        saveToFile();
    }

    // Sort foodList by expiryDate from nearest to furthest.
    // Foods with the same name are grouped together.
    public void sortFoodByExpiryDate() {
        // Sort foodList by expiryDate from nearest to furthest
        foodList.sort(Comparator.comparing(Food::getExpiryDate));

        // AI Aid algorithm to group foods with the same name together
        // Temporary map to track last inserted index for each food name
        Map<String, Integer> nameToLastIndex = new HashMap<>();

        List<Food> sortedList = new ArrayList<>();

        for (Food food : foodList) {
            String name = food.getName();
            if (!nameToLastIndex.containsKey(name)) {
                nameToLastIndex.put(name, sortedList.size());
                sortedList.add(food);
            } else {
                int index = nameToLastIndex.get(name);
                // Insert after the last index for the same name
                sortedList.add(index + 1, food);
                nameToLastIndex.put(name, index + 1);
            }
        }

        // Update the original foodList with the sorted results
        foodList = sortedList;
    }

    public void saveToFile() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule()); // Register the module to handle LocalDate

        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(FILE_PATH), foodList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadFromFile() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule()); // Register the module to handle LocalDate

        try {
            foodList = mapper.readValue(new File(FILE_PATH), mapper.getTypeFactory().constructCollectionType(List.class, Food.class));
        } catch (IOException e) {
            System.out.println("File does not exist or is corrupted. Initializing to empty list.");
            foodList = new ArrayList<>(); // Initialize to empty list if there's an error
        }
    }

    public void removeFood(Food foodToRemove) {
        // Removes the first occurrence of the food that matches
        foodList.removeIf(existingFood -> existingFood.equals(foodToRemove));
        saveToFile();
    }
}
