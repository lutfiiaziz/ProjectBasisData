module filtering_app {
    requires javafx.controls;
    requires javafx.fxml;

    opens filtering_app to javafx.fxml;
    exports filtering_app;
    requires java.sql;
}
