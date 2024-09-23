package org.stacyliu.foodbuddy.Servive;

import javafx.scene.control.ListView;
import org.stacyliu.foodbuddy.model.Category;
import org.stacyliu.foodbuddy.model.Food;
import org.stacyliu.foodbuddy.model.FoodStock;

import java.util.*;
import java.util.stream.Collectors;

public class GoShoppingTabService {

    public void goShoppingTabSetup(ListView<String> runningLowList, ListView<String> tryNewList) {
        List<Food> lowList = FoodStock.getInstance().getFoodList().stream()
                .filter(food -> food.getQuantity() < 3)
                .sorted(Comparator.comparingInt(Food::getQuantity))
                .toList();

        runningLowList.getItems().clear();
        for (Food food : lowList) {
            runningLowList.getItems().add(food.getName() + " - " + food.getQuantity() + " left");
        }

        List<String> missingFoods = getRandomMissingFoods(FoodStock.getInstance().getFoodList(), 10);
        tryNewList.getItems().clear();
        tryNewList.getItems().addAll(missingFoods);
    }


    public List<String> getAllCategoryNames(Category category) {
        List<String> categoryNames = new ArrayList<>();
        collectCategoryNames(category, categoryNames);
        return categoryNames;
    }

    private void collectCategoryNames(Category category, List<String> categoryNames) {
        if (!category.getSubcategories().isEmpty()) {
            for (Category subcategory : category.getSubcategories()) {
                collectCategoryNames(subcategory, categoryNames);
            }
        } else {
            categoryNames.add(category.getCategory());
        }
    }

    public List<String> getRandomMissingFoods(List<Food> foodList, int count) {
        Category rootCategory = Category.loadCategoryJsonData();
        Set<String> existingFoods = foodList.stream()
                .map(Food::getName)
                .collect(Collectors.toSet());

        List<String> allCategoryNames = new ArrayList<>();
        allCategoryNames.addAll(getAllCategoryNames(rootCategory));

        List<String> missingFoods = allCategoryNames.stream()
                .filter(category -> !existingFoods.contains(category))
                .collect(Collectors.toList());

        Collections.shuffle(missingFoods);
        return missingFoods.stream()
                .limit(count)
                .collect(Collectors.toList());
    }
}
