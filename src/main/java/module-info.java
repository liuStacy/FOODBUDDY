module org.stacyliu.foodbuddy {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.commons.lang3;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.datatype.jsr310;
    requires java.desktop;


    opens org.stacyliu.foodbuddy to javafx.fxml;
    exports org.stacyliu.foodbuddy;
    opens org.stacyliu.foodbuddy.model;
}