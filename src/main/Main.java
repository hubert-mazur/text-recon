package main;

import binarize.Img;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;


public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {

        Img im = new Img();
        //creating a Group object
        ImageView iv = new ImageView(im.getBinarizedImage());

        Group group = new Group(iv);

        //Creating a Scene by passing the group object, height and width
        Scene scene = new Scene(group ,600, 300);

        //Setting the title to Stage.
        primaryStage.setTitle("Sample Application");
        //Adding the scene to Stage
        primaryStage.setScene(scene);

        //Displaying the contents of the stage
        primaryStage.show();

    }
    public static void main(String[] args){
        launch(args);
    }
}

/* Useful links:
https://www.tutorialspoint.com/javafx/index.htm -- tutorial for JavaFX
https://www.jetbrains.com/help/idea/javafx.html -- setting up environment in IntelliJ
https://docs.oracle.com/javase/8/javafx/api/toc.htm -- documentation for of JavaFX

 */
