package org.stacyliu.foodbuddy;

import com.google.gson.Gson;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import org.stacyliu.foodbuddy.model.Category;

import java.io.FileReader;
import java.time.LocalDate;

public class FoodBuddyController {
    @FXML
    private TreeTableColumn<?, ?> stockTable;

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
    private ListView<?> runningLowList;

    @FXML
    private ListView<?> tryNewList;

    @FXML
    public void initialize() {
        addFoodTabSetup();
    }

    private void addFoodTabSetup() {
        categoryListSetup();
        sliderSetup();
    }

    private void sliderSetup() {
        // Slider listener to update text field
        countTextField.setText(String.valueOf((int)countSlider.getValue()));
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
        try (FileReader reader = new FileReader(filePath)) {
            Gson gson = new Gson();
            return gson.fromJson(reader, Category.class);
        } catch (Exception e) {
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