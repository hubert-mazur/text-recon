package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    static Controller controller;

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("main.fxml"));
        Parent root = loader.load();

        //Setting the title to Stage.
        primaryStage.setTitle("Rozpoznawanie tekstu");

        //Adding the scene to Stage
        primaryStage.setScene(new Scene(root, 1200, 650));

        controller = loader.getController();
        controller.setStage(primaryStage);

        //Displaying the contents of the stage
        primaryStage.show();
    }
}

/* Useful links:
https://www.tutorialspoint.com/javafx/index.htm -- tutorial for JavaFX
https://www.jetbrains.com/help/idea/javafx.html -- setting up environment in IntelliJ
https://docs.oracle.com/javase/8/javafx/api/toc.htm -- documentation for of JavaFX

 */
