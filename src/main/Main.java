package main;

import binarize.Img;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;


public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));

        //Setting the title to Stage.
        primaryStage.setTitle("Rozpoznawanie tekstu");

        //Adding the scene to Stage
        primaryStage.setScene(new Scene(root, 900, 650));

        //Displaying the contents of the stage
        primaryStage.show();
    }
}

/* Useful links:
https://www.tutorialspoint.com/javafx/index.htm -- tutorial for JavaFX
https://www.jetbrains.com/help/idea/javafx.html -- setting up environment in IntelliJ
https://docs.oracle.com/javase/8/javafx/api/toc.htm -- documentation for of JavaFX

 */
