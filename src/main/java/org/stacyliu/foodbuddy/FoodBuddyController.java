package org.stacyliu.foodbuddy;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;

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
    private TreeView<?> categoryList;

    @FXML
    private ListView<?> runningLowList;

    @FXML
    private ListView<?> tryNewList;

    @FXML
    protected void onCategoryListClick(MouseEvent event) {
        // Handle category list item click
        Object selectedItem = categoryList.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
        }
    }

    @FXML
    protected void onRunningLowListClick(MouseEvent event) {
        // Handle running low list item click
        Object selectedItem = runningLowList.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
        }
    }

    @FXML
    protected void onTryNewListClick(MouseEvent event) {
        // Handle try new list item click
        Object selectedItem = tryNewList.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
        }
    }
}