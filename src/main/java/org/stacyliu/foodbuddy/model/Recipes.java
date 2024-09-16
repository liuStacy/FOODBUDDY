package org.stacyliu.foodbuddy.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Recipes {

    private List<Recipe> recipeList;

    public List<Recipe> getRecipeList() {
        return recipeList;
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
        // treeMap can naturally sort the recipes based on key (weightScore)
        // Key: weightScore, Value: Recipe
        Map<Integer, Recipe> treeMap = new TreeMap<>();
        for (Recipe recipe : recipeList) {
            int weightScore = 0;
            for (String ingredient : recipe.getMainIngredients().split(",")) {
                for (int i = 0; i < foodStock.size(); i++) {
                    Food food = foodStock.get(i);
                    if (food.getName().equals(ingredient)) {
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
            treeMap.put(weightScore, recipe);
        }
        List<Recipe> sortedRecipes = new ArrayList<>(treeMap.values());
        // Reverse the list to get the highest weight score first
        return sortedRecipes.reversed();
    }
}
