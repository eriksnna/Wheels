module org.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires itextpdf;

    opens org.example to javafx.fxml;
    opens org.example.controllers to javafx.fxml;

    opens org.example.models to javafx.base, javafx.fxml;

    exports org.example;
}