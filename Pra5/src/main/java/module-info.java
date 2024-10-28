module com.example.p {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.google.gson;
    requires org.junit.jupiter.api;
    requires org.junit.jupiter.params;

    opens com.example.pra5.bank to com.google.gson;
    exports com.example.pra5;
    opens com.example.pra5 to com.google.gson, javafx.fxml;
}
