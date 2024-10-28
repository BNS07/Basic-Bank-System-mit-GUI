module com.oos.praktikum5 {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;


    opens bank to com.google.gson;
    opens com.oos.praktikum5 to javafx.fxml;
    exports com.oos.praktikum5;
}