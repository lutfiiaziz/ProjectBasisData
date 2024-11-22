package filtering_app;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;

public class PrimaryController {
    @FXML
    private VBox checkBoxContainer;

    @FXML
    private Button showDataButton;

    @FXML
    private TableView<ObservableList<String>> tableView;

    private ObservableList<CheckBox> tableCheckBoxes = FXCollections.observableArrayList();


    String hostName = "localhost";
    String sqlInstanceName = "LAPTOP-63KNHL2K\\SQLEXPRESS";
    String sqlDB = "Chemical_DB";
    String sqlUser = "sa";
    String sqlPw = "Akufeyra09_";

    String connectURL = "jdbc:sqlserver://" + hostName + ":1433" + ";instance=" + sqlInstanceName
            + ";databaseName=" + sqlDB + ";encrypt=true;trustServerCertificate=true;";

    public void initialize() {
        // Initialize Checkboxes
        String[] tableNames = {"BrandName", "CAS", "Chemical", "Company", "Contain", "CSF", "PartOf", "PrimaryCategory", "Product", "SubCategory"};
        for (String tableName : tableNames) {
            CheckBox checkBox = new CheckBox(tableName);
            tableCheckBoxes.add(checkBox);
        }
        checkBoxContainer.getChildren().addAll(tableCheckBoxes);

        // Set Button Action
        showDataButton.setOnAction(event -> showSelectedTables());
    }

    private void showSelectedTables() {
        ObservableList<String> selectedTables = FXCollections.observableArrayList();
        for (CheckBox checkBox : tableCheckBoxes) {
            if (checkBox.isSelected()) {
                selectedTables.add(checkBox.getText());
            }
        }

        if (selectedTables.isEmpty()) {
            showAlert("No Selection", "Please select at least one table to display.");
        } else {
            fetchAndDisplayData(selectedTables);
        }
    }


    private void fetchAndDisplayData(ObservableList<String> selectedTables) {
        tableView.getColumns().clear();

        String selectedTable = selectedTables.get(0); // Display the first selected table
        String query = "SELECT TOP 50 * FROM " + selectedTable;

        try (Connection conn = DriverManager.getConnection(connectURL, sqlUser, sqlPw);
                java.sql.Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {

            // Populate TableView
            populateTable(rs);

        } catch (SQLException e) {
            showAlert("Database Error", "Failed to fetch data from the database: " + e.getMessage());
        }
    }

    private void populateTable(ResultSet rs) throws SQLException {
        ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();

        // Create table columns dynamically
        for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
            final int colIndex = i - 1;
            TableColumn<ObservableList<String>, String> column = new TableColumn<>(rs.getMetaData().getColumnName(i));
            column.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(colIndex)));
            tableView.getColumns().add(column);
        }

        // Populate rows
        while (rs.next()) {
            ObservableList<String> row = FXCollections.observableArrayList();
            for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                row.add(rs.getString(i));
            }
            data.add(row);
        }

        tableView.setItems(data);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public class CheckBoxManager {
        private final VBox checkBoxContainer;
        private final ObservableList<CheckBox> checkBoxes;

        public CheckBoxManager(String[] tableNames) {
            this.checkBoxContainer = new VBox(5); // Spacing between checkboxes
            this.checkBoxes = FXCollections.observableArrayList();

            for (String tableName : tableNames) {
                CheckBox checkBox = new CheckBox(tableName);
                checkBoxes.add(checkBox);
            }

            checkBoxContainer.getChildren().addAll(checkBoxes);
        }

        public VBox getCheckBoxContainer() {
            return checkBoxContainer;
        }

        public ObservableList<String> getSelectedTables() {
            ObservableList<String> selectedTables = FXCollections.observableArrayList();
            for (CheckBox checkBox : checkBoxes) {
                if (checkBox.isSelected()) {
                    selectedTables.add(checkBox.getText());
                }
            }
            return selectedTables;
        }
    }

}
