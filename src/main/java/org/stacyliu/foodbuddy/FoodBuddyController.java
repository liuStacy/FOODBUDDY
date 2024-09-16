package org.stacyliu.foodbuddy;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import org.apache.commons.lang3.StringUtils;
import org.stacyliu.foodbuddy.model.Category;
import org.stacyliu.foodbuddy.model.Food;
import org.stacyliu.foodbuddy.model.FoodStock;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class FoodBuddyController {
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

    @FXML
    private TextArea recipeContent1;

    @FXML
    private TextArea recipeContent2;

    @FXML
    private TextArea recipeContent3;

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
    private Button clearButton_AddFood;

    @FXML
    private Button AddButton_AddFood;

    @FXML
    private ListView<?> runningLowList;

    @FXML
    private ListView<?> tryNewList;

    @FXML
    public void initialize() {
        addHomeTabSetup();
        addFoodTabSetup();
    }

    @FXML
    private void handleTabSelectionChanged() {
        if (homeTreeTableView != null) {
            loadFoodStockData();
        }
    }

    private void addHomeTabSetup() {
        loadFoodStockData();
    }

    private void loadFoodStockData() {
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

    @FXML
    public void clearAddFoodData() {
        categoryTextField.clear();
        foodTextField.clear();
        countSlider.setValue(1);
        countTextField.clear();
        expiryDatePicker.setValue(null);
    }

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

    private void addFoodTabSetup() {
        categoryListSetup();
        sliderSetup();
    }

    private void sliderSetup() {
        // Slider listener to update text field
        countSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            countTextField.setText(String.valueOf(newVal.intValue()));
        });
    }

    private void categoryListSetup() {
        // Add Food tab
        // CategoryList data from JSON
        Category rootCategory = loadCategoryJsonData("src/main/resources/org/stacyliu/foodbuddy/Data/FoodCategory.json");
        if (rootCategory != null) {
            TreeItem<String> rootItem = buildTree(rootCategory);
            categoryList.setRoot(rootItem);
            rootItem.setExpanded(true);
        }

        // CategoryList listener to update text fields
        categoryList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && newValue.getParent() != null && newValue.getParent().getValue() != null && newValue.getChildren().isEmpty()) {
                categoryTextField.setText(newValue.getParent().getValue());
                foodTextField.setText(newValue.getValue());
                countTextField.setText("1");
                Integer daysToExpire = rootCategory.getDaysToExpire(newValue.getValue());
                if (daysToExpire != null) {
                    expiryDatePicker.setValue(LocalDate.now().plusDays(daysToExpire));
                } else {
                    expiryDatePicker.setValue(null);
                }
            } else {
                categoryTextField.clear();
                foodTextField.clear();
                expiryDatePicker.setValue(null);
            }
        });
    }

    private Category loadCategoryJsonData(String filePath) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        try {
            return mapper.readValue(new File(filePath), Category.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private TreeItem<String> buildTree(Category category) {
        TreeItem<String> rootItem = new TreeItem<>(category.getCategory());

        if (category.getSubcategories() != null && !category.getSubcategories().isEmpty()) {
            for (Category subCategory : category.getSubcategories()) {
                TreeItem<String> childItem = buildTree(subCategory);
                rootItem.getChildren().add(childItem);
            }
        }

        return rootItem;
    }
}