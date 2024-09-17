package org.stacyliu.foodbuddy;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.apache.commons.lang3.StringUtils;
import org.stacyliu.foodbuddy.Servive.AddFoodTabService;
import org.stacyliu.foodbuddy.Servive.HomeTabService;
import org.stacyliu.foodbuddy.model.*;

public class FoodBuddyController {

    private final HomeTabService homeTabService = new HomeTabService();
    private final AddFoodTabService addFoodTabService = new AddFoodTabService();
    // Home Tab - stock list
    @FXML
    private TreeTableView<Food> homeTreeTableView;

    @FXML
    private TreeTableColumn<Food, String> homeCategoryTreeTableColumn;

    @FXML
    private TreeTableColumn<Food, String> homeFoodTreeTableColumn;

    @FXML
    private TreeTableColumn<Food, Integer> homeCountTreeTableColumn;

    @FXML
    private TreeTableColumn<Food, Integer> homeDaysLeftTableColumn;

    // Home Tab - recipe list
    @FXML
    private Label recipe1Name;
    @FXML
    private Label recipe2Name;
    @FXML
    private Label recipe3Name;
    
    @FXML
    private Label recipe1Info;
    @FXML
    private Label recipe2Info;
    @FXML
    private Label recipe3Info;
    
    @FXML
    private Label recipe1Ingredients;
    @FXML
    private Label recipe2Ingredients;
    @FXML
    private Label recipe3Ingredients;
    
    @FXML
    private TextArea recipe1Instructions;
    @FXML
    private TextArea recipe2Instructions;
    @FXML
    private TextArea recipe3Instructions;

    @FXML
    private TitledPane titledPane1;
    @FXML
    private TitledPane titledPane2;
    @FXML
    private TitledPane titledPane3;

    // Add Food Tab
    @FXML
    private TreeView<String> categoryList;

    @FXML
    private TextField categoryTextField;

    @FXML
    private TextField foodTextField;

    @FXML
    private TextField countTextField;

    @FXML
    private Slider countSlider;

    @FXML
    private DatePicker expiryDatePicker;

    @FXML
    public void initialize() {

        // Home Tab setup
        homeTabService.homeTabSetup(homeCategoryTreeTableColumn, homeFoodTreeTableColumn, homeCountTreeTableColumn, homeDaysLeftTableColumn,
                homeTreeTableView, recipe1Name, recipe2Name, recipe3Name, recipe1Info, recipe2Info, recipe3Info, recipe1Ingredients,
                recipe2Ingredients, recipe3Ingredients, recipe1Instructions, recipe2Instructions, recipe3Instructions, titledPane1, titledPane2, titledPane3);

        // Add Food Tab setup
        addFoodTabService.addFoodTabSetup(categoryList, categoryTextField, foodTextField, countTextField, expiryDatePicker, countSlider);

        titledPane1.setExpanded(true);
    }

    @FXML
    private void handleTabSelectionChanged() {
        // Refresh Home Tab data when user change Tab
        if (homeTreeTableView != null) {
            homeTabService.homeTabSetup(homeCategoryTreeTableColumn, homeFoodTreeTableColumn, homeCountTreeTableColumn, homeDaysLeftTableColumn,
                    homeTreeTableView, recipe1Name, recipe2Name, recipe3Name, recipe1Info, recipe2Info, recipe3Info, recipe1Ingredients,
                    recipe2Ingredients, recipe3Ingredients, recipe1Instructions, recipe2Instructions, recipe3Instructions, titledPane1, titledPane2, titledPane3);
        }
    }

    // Add Button actions
    @FXML
    public void addAddFoodData() {
        boolean categoryExists = StringUtils.isNotBlank(categoryTextField.getText());
        boolean foodExists = StringUtils.isNotBlank(foodTextField.getText());
        boolean countExists = StringUtils.isNotBlank(countTextField.getText());
        boolean expiryExists = expiryDatePicker.getValue() != null;
        if (categoryExists && foodExists && countExists && expiryExists) {
            Food newFood = new Food(categoryTextField.getText(), foodTextField.getText(), Integer.parseInt(countTextField.getText()), expiryDatePicker.getValue());
            FoodStock.getInstance().addFood(newFood);
            clearAddFoodData();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("Missing Information");
            alert.setContentText("Please fill in all fields.");
            alert.showAndWait();
        }
    }

    // Clean Button actions
    @FXML
    public void clearAddFoodData() {
        categoryTextField.clear();
        foodTextField.clear();
        countSlider.setValue(1);
        countTextField.clear();
        expiryDatePicker.setValue(null);
    }
}