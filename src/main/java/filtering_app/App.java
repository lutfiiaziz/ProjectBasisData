package filtering_app;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class App extends Application {
    private static Scene scene;

    public static void main(String[] args) throws ClassNotFoundException {
        launch();

    }

    @Override
    public void start(Stage primaryStage) throws IOException{
        scene = new Scene(loadFXML("primary"), 800, 600);
        primaryStage.setTitle("Table Selection App");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

}