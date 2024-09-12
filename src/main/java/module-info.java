module org.stacyliu.foodbuddy {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.stacyliu.foodbuddy to javafx.fxml;
    exports org.stacyliu.foodbuddy;
}