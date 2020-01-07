package main;

import binarize.Img;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class Controller {
    @FXML
    private ImageView imageView;

    private Img img;

    public void onFileChooseRequest(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        File imgFile = fileChooser.showOpenDialog(new Stage());
        img = new Img(imgFile);
        this.onOriginalImageRequest(event);
    }

    private void showImageNotLoadedDialog() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Informacja");
        alert.setHeaderText("Najpierw wczytaj obraz");
        alert.setContentText("Plik -> Wczytaj obraz");
        alert.showAndWait();
    }

    public void setOnCloseRequest(ActionEvent event) {
        Platform.exit();
    }

    public void onOriginalImageRequest(ActionEvent event) {
        if (img == null) {
            showImageNotLoadedDialog();
            return;
        }

        imageView.setImage(img.getInputImage());
    }

    public void onGrayscaleImageRequest(ActionEvent event) {
        if (img == null) {
            showImageNotLoadedDialog();
            return;
        }

        imageView.setImage(img.getBinarizedImage());
    }
}
