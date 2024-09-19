package org.stacyliu.foodbuddy;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.apache.commons.lang3.StringUtils;
import org.stacyliu.foodbuddy.Servive.AddFoodTabService;
import org.stacyliu.foodbuddy.Servive.HomeTabService;
import org.stacyliu.foodbuddy.model.*;

import java.util.List;
import java.util.stream.Collectors;

public class FoodBuddyController {

    private final HomeTabService homeTabService = new HomeTabService();
    private final AddFoodTabService addFoodTabService = new AddFoodTabService();

    private boolean isEditMode = false;

    @FXML
    public Button cancelButton;

    @FXML
    public Button minusButton;

    @FXML
    public Button plusButton;

    @FXML
    public Button editButton;
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
    private Accordion accordion;
    @FXML
    public void initialize() {

        // Home Tab setup
        homeTabService.homeTabSetup(homeCategoryTreeTableColumn, homeFoodTreeTableColumn, homeCountTreeTableColumn, homeDaysLeftTableColumn,
                homeTreeTableView, recipe1Name, recipe2Name, recipe3Name, recipe1Info, recipe2Info, recipe3Info, recipe1Ingredients,
                recipe2Ingredients, recipe3Ingredients, recipe1Instructions, recipe2Instructions, recipe3Instructions, titledPane1, titledPane2, titledPane3);

        // Add Food Tab setup
        addFoodTabService.addFoodTabSetup(categoryList, categoryTextField, foodTextField, countTextField, expiryDatePicker, countSlider);
        accordion.setExpandedPane(titledPane1);
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

    @FXML
    public void handleCancelButtonAction(ActionEvent event) {
        // Restore to initial state
        isEditMode = false;
        editButton.setText("Edit");
        plusButton.setVisible(false);
        minusButton.setVisible(false);
        cancelButton.setVisible(false);
        FoodStock.getInstance().loadFromFile();
        homeTabService.homeTabSetup(homeCategoryTreeTableColumn, homeFoodTreeTableColumn, homeCountTreeTableColumn, homeDaysLeftTableColumn,
                homeTreeTableView, recipe1Name, recipe2Name, recipe3Name, recipe1Info, recipe2Info, recipe3Info, recipe1Ingredients,
                recipe2Ingredients, recipe3Ingredients, recipe1Instructions, recipe2Instructions, recipe3Instructions, titledPane1, titledPane2, titledPane3);
    }

    @FXML
    public void handleMinusButtonAction(ActionEvent event) {
        // Decrease quantity for selected item
        TreeItem<Food> selectedItem = homeTreeTableView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            Food food = selectedItem.getValue();
            int newQuantity = food.getQuantity() - 1;
            if (newQuantity <= 0) {
                homeTreeTableView.getRoot().getChildren().remove(selectedItem);
            } else {
                food.setQuantity(newQuantity);
                homeTreeTableView.refresh();
            }
        }
    }

    @FXML
    public void handlePlusButtonAction(ActionEvent event) {
        // Increase quantity for selected item
        TreeItem<Food> selectedItem = homeTreeTableView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            Food food = selectedItem.getValue();
            food.setQuantity(food.getQuantity() + 1); // Increase quantity
            homeTreeTableView.refresh(); // Refresh view
        }
    }

    @FXML
    public void handleEditButtonAction(ActionEvent event) {
        System.out.println("Edit Button clicked");
        if (isEditMode) {
            // Save changes
            saveChanges();

            // Switch back to Edit mode
            editButton.setText("Edit");
            plusButton.setVisible(false);
            minusButton.setVisible(false);
            cancelButton.setVisible(false);
        } else {
            // Switch to Edit mode
            editButton.setText("Save");
            plusButton.setVisible(true);
            minusButton.setVisible(true);
            cancelButton.setVisible(true);
        }
        isEditMode = !isEditMode;
        homeTreeTableView.setEditable(isEditMode);
    }

    private void saveChanges() {
        // Save data to FoodStock
        List<Food> foods = homeTreeTableView.getRoot().getChildren().stream()
                .map(TreeItem::getValue).collect(Collectors.toList());
        FoodStock.getInstance().setFoodList(foods);
        FoodStock.getInstance().sortFoodByExpiryDate();
        FoodStock.getInstance().saveToFile();
        FoodStock.getInstance().loadFromFile();
        homeTabService.homeTabSetup(homeCategoryTreeTableColumn, homeFoodTreeTableColumn, homeCountTreeTableColumn, homeDaysLeftTableColumn,
                homeTreeTableView, recipe1Name, recipe2Name, recipe3Name, recipe1Info, recipe2Info, recipe3Info, recipe1Ingredients,
                recipe2Ingredients, recipe3Ingredients, recipe1Instructions, recipe2Instructions, recipe3Instructions, titledPane1, titledPane2, titledPane3);
    }
}