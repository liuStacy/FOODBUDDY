package org.stacyliu.foodbuddy.Servive;

import javafx.scene.control.*;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import org.stacyliu.foodbuddy.model.Food;
import org.stacyliu.foodbuddy.model.FoodStock;
import org.stacyliu.foodbuddy.model.Recipe;
import org.stacyliu.foodbuddy.model.Recipes;

import java.time.LocalDate;
import java.util.List;

public class HomeTabService {

    // setup the home tab content
    public void homeTabSetup(TreeTableColumn<Food, String> homeCategoryTreeTableColumn, TreeTableColumn<Food, String> homeFoodTreeTableColumn, TreeTableColumn<Food, Integer> homeCountTreeTableColumn,
                                TreeTableColumn<Food, Integer> homeDaysLeftTableColumn, TreeTableView<Food> homeTreeTableView, Label recipe1Name, Label recipe2Name, Label recipe3Name,
                                Label recipe1Info, Label recipe2Info, Label recipe3Info, Label recipe1Ingredients, Label recipe2Ingredients, Label recipe3Ingredients,
                                TextArea recipe1Instructions, TextArea recipe2Instructions, TextArea recipe3Instructions, TitledPane titledPane1, TitledPane titledPane2,
                                TitledPane titledPane3) {

        loadFoodStockData(homeCategoryTreeTableColumn, homeFoodTreeTableColumn, homeCountTreeTableColumn, homeDaysLeftTableColumn, homeTreeTableView);

        homeTabRecipeSetup(recipe1Name, recipe2Name, recipe3Name, recipe1Info, recipe2Info, recipe3Info, recipe1Ingredients, recipe2Ingredients, recipe3Ingredients,
                           recipe1Instructions, recipe2Instructions, recipe3Instructions, titledPane1, titledPane2, titledPane3);
    }

    // Load data from FoodStock and display it in the TreeTableView
    private void loadFoodStockData(TreeTableColumn<Food, String> homeCategoryTreeTableColumn, TreeTableColumn<Food, String> homeFoodTreeTableColumn,
                                  TreeTableColumn<Food, Integer> homeCountTreeTableColumn, TreeTableColumn<Food, Integer> homeDaysLeftTableColumn,
                                  TreeTableView<Food> homeTreeTableView) {

        homeCategoryTreeTableColumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("category"));
        homeFoodTreeTableColumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("name"));
        homeCountTreeTableColumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("quantity"));
        // Convert expiryDate to daysLeft. AI aid algorithm.
        homeDaysLeftTableColumn.setCellValueFactory(cellData -> {
            // Convert the daysLeft property to an ObservableValue<Integer>
            return new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getValue().getDaysLeft()).asObject();
        });
        homeDaysLeftTableColumn.setCellFactory(column -> new TreeTableCell<Food, Integer>() {
            @Override
            protected void updateItem(Integer daysLeft, boolean empty) {
                super.updateItem(daysLeft, empty);

                if (empty || daysLeft == null) {
                    setText(null);
                } else {
                    setText(String.valueOf(daysLeft));
                }
            }
        });

        // Load data from FoodStock
        FoodStock foodStock = FoodStock.getInstance();
        List<Food> foodList = foodStock.getFoodList();

        // Populate the TreeTableView
        TreeItem<Food> rootItem = new TreeItem<>(new Food("Root", "", 0, LocalDate.now()));
        rootItem.setExpanded(true);

        for (Food food : foodList) {
            rootItem.getChildren().add(new TreeItem<>(food));
        }

        homeTreeTableView.setRoot(rootItem);
        homeTreeTableView.setShowRoot(false);
    }

    private void homeTabRecipeSetup(Label recipe1Name, Label recipe2Name, Label recipe3Name, Label recipe1Info, Label recipe2Info, Label recipe3Info,
                                    Label recipe1Ingredients, Label recipe2Ingredients, Label recipe3Ingredients, TextArea recipe1Instructions, TextArea recipe2Instructions,
                                    TextArea recipe3Instructions, TitledPane titledPane1, TitledPane titledPane2, TitledPane titledPane3) {
        // Load data from Recipes
        Recipes recipes = Recipes.getInstance();

        // Get the sorted recipes based on the weight score of the main ingredients
        List<Recipe> sortedRecipes = recipes.getSortedRecipesBasedOnWeight(FoodStock.getInstance().getFoodList());

        // Display the top 3 recipes
        populateRecipes(0, recipe1Name, recipe1Info, recipe1Ingredients, recipe1Instructions, titledPane1, sortedRecipes);
        populateRecipes(1, recipe2Name, recipe2Info, recipe2Ingredients, recipe2Instructions, titledPane2, sortedRecipes);
        populateRecipes(2, recipe3Name, recipe3Info, recipe3Ingredients, recipe3Instructions, titledPane3, sortedRecipes);
        titledPane1.setExpanded(true);
    }

    private void populateRecipes(int index, Label name, Label info, Label ingredients, TextArea instructions, TitledPane pane, List<Recipe> sortedRecipes) {
        name.setText(sortedRecipes.get(index).getName());
        info.setText(sortedRecipes.get(index).getTotalTime() + "  |  " + sortedRecipes.get(index).getDifficulty() + "  |  " + sortedRecipes.get(index).getLastUpdated());
        ingredients.setGraphic(getIngredientsTextFlow(sortedRecipes.get(index).getIngredients()));
        instructions.setText("Instructions:\n" + sortedRecipes.get(index).getInstructions());
        pane.setText(sortedRecipes.get(index).getName());
    }

    // Make the ingredients bold if they are in the food stock
    private TextFlow getIngredientsTextFlow(String ingredients) {
        TextFlow textFlow = new TextFlow();
        String[] ingredientList = ingredients.split(",");
        for (int i = 0; i < ingredientList.length; i++) {
            String ingredient = ingredientList[i].trim();
            Text text = new Text(ingredient);
            if (FoodStock.getInstance().getFoodList().stream().anyMatch(food -> ingredient.contains(food.getName()))) {
                text.setStyle("-fx-font-weight: bold;");
            }
            textFlow.getChildren().add(text);
            if (i < ingredientList.length - 1) {
                textFlow.getChildren().add(new Text(", "));
            }
        }
        return textFlow;
    }
}
