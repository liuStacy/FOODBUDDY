package org.stacyliu.foodbuddy.Servive;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import javafx.scene.control.*;
import org.stacyliu.foodbuddy.model.Category;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

public class AddFoodTabService {
    public void addFoodTabSetup(TreeView<String> categoryList, TextField categoryTextField,
                                TextField foodTextField, TextField countTextField, DatePicker expiryDatePicker, Slider countSlider) {

        categoryListSetup(categoryList, categoryTextField, foodTextField, countTextField, expiryDatePicker);

        sliderSetup(countSlider, countTextField);
    }

    private void sliderSetup(Slider countSlider, TextField countTextField) {
        // Slider listener to update text field
        countSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            countTextField.setText(String.valueOf(newVal.intValue()));
        });
    }

    private void categoryListSetup(TreeView<String> categoryList, TextField categoryTextField, TextField foodTextField, TextField countTextField, DatePicker expiryDatePicker) {
        // Add Food tab
        // CategoryList data from JSON
        Category rootCategory = Category.loadCategoryJsonData();
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

    // Build a tree structure from the Category object
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
