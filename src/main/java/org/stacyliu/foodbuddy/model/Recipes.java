package org.stacyliu.foodbuddy.model;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Recipes {
    // Singleton
    private static final Recipes INSTANCE = new Recipes();

    // Static block here to load data during class initialization
    static {
        INSTANCE.loadFromFile();
    }
    private static final String FILE_PATH = "src/main/resources/org/stacyliu/foodbuddy/Data/Recipes.json";
    private List<Recipe> recipeList;

    public List<Recipe> getRecipeList() {
        return recipeList;
    }

    private Recipes() {
        this.recipeList = new ArrayList<>();
    }

    // Static method to get the single instance
    public static Recipes getInstance() {
        return INSTANCE;
    }

    public void setRecipeList(List<Recipe> recipeList) {
        this.recipeList = recipeList;
    }

    /**
     * Get a sorted list of recipes based on the weight score of the main ingredients, highest weight score first
     * Weight Score:
     * 1. If the food is in the first third of the food stock and has 3 or fewer days left, add 3 points
     * 2. If the food is in the first third of the food stock and has more than 3 days left, add 2 points
     * 3. If the food is in the last two thirds of the food stock, add 1 point
     * 4. If the food has more than 5 quantity, add 1 point
     * @param foodStock
     * @return a sorted list of recipes based on the weight score of the main ingredients
     */
    public List<Recipe> getSortedRecipesBasedOnWeight(List<Food> foodStock) {
        return recipeList.stream().sorted(Comparator.comparingInt(recipe -> -getRecipeScore(recipe, foodStock))).toList();
    }

    private int getRecipeScore(Recipe recipe, List<Food> foodStock) {
        int weightScore = 0;
        for (String ingredient : recipe.getIngredients().split(",")) {
            for (int i = 0; i < foodStock.size(); i++) {
                Food food = foodStock.get(i);
                if (ingredient.trim().contains(food.getName())) {
                    if (i < foodStock.size()/3) {
                        if (food.getDaysLeft() <= 3) {
                            weightScore += 3;
                        } else {
                            weightScore += 2;
                        }
                    } else {
                        weightScore += 1;
                    }
                    if (food.getQuantity() > 5) {
                        weightScore += 1;
                    }
                }
            }
        }
        return weightScore;
    }

    private void loadFromFile() {
        ObjectMapper mapper = new ObjectMapper();

        try {
            recipeList = mapper.readValue(new File(FILE_PATH), mapper.getTypeFactory().constructCollectionType(List.class, Recipe.class));
        } catch (IOException e) {
            System.out.println("File does not exist or is corrupted. Initializing to empty list.");
            recipeList = new ArrayList<>(); // Initialize to empty list if there's an error
        }
    }
}
