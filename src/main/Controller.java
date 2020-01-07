package main;

import binarize.Img;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class Controller {
    @FXML
    private ImageView imageView;

    public void onFileChooseRequest(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        File imgFile = fileChooser.showOpenDialog(new Stage());
        Img img = new Img(imgFile);
        imageView.setImage(img.getBinarizedImage());
    }

    public void setOnCloseRequest(ActionEvent event) {
        Platform.exit();
    }
}
