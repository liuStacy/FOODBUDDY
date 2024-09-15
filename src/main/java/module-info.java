module org.stacyliu.foodbuddy {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;


    opens org.stacyliu.foodbuddy to javafx.fxml;
    exports org.stacyliu.foodbuddy;
    opens org.stacyliu.foodbuddy.model;
}