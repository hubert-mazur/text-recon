package main;

import binarize.Img;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class Controller {
    @FXML
    private ImageView imageView;
    @FXML
    private AnchorPane binarizationFactorBox;
    @FXML
    private Button subtractButton;
    @FXML
    private Button addButton;
    @FXML
    private Text percentLabel;
    @FXML
    private Button textReconButton;

    private Img img;

    void setStage(Stage stage) {
        this.stage = stage;
    }

    private Stage stage;

    public void onFileChooseRequest(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Images", "*.jpg", "*.png");
        fileChooser.getExtensionFilters().add(extFilter);
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

        subtractButton.setDisable(true);
        addButton.setDisable(true);
        textReconButton.setDisable(true);

        imageView.setImage(img.getInputImage());
    }

    public void onGrayscaleImageRequest(ActionEvent event) {
        stage.getScene().setCursor(Cursor.WAIT);

        if (img == null) {
            showImageNotLoadedDialog();
            return;
        }

        subtractButton.setDisable(true);
        addButton.setDisable(true);
        textReconButton.setDisable(true);

        imageView.setImage(img.greyscale());
        stage.getScene().setCursor(Cursor.DEFAULT);
    }

    public void onBinarizedImageRequest(ActionEvent event) {
        if (img == null) {
            showImageNotLoadedDialog();
            return;
        }

        binarizationFactorBox.setDisable(false);
        subtractButton.setDisable(false);
        addButton.setDisable(false);
        textReconButton.setDisable(false);

        stage.getScene().setCursor(Cursor.WAIT);

        imageView.setImage(img.binarize());
        img.setPercent(15);
        percentLabel.setText("15");

        stage.getScene().setCursor(Cursor.DEFAULT);
    }

    public void onSubtractPercentRequest(ActionEvent event) {
        stage.getScene().setCursor(Cursor.WAIT);

        int percent = img.getPercent();
        if (percent > -15) {
            img.setPercent(--percent);
            percentLabel.setText("" + percent);
            imageView.setImage(img.binarize());
        }

        stage.getScene().getRoot().setCursor(Cursor.DEFAULT);
    }

    public void onAddPercentButton(ActionEvent event) {
        stage.getScene().setCursor(Cursor.WAIT);

        int percent = img.getPercent();
        if (percent < 45) {
            img.setPercent(++percent);
            percentLabel.setText("" + percent);
            imageView.setImage(img.binarize());
        }

        stage.getScene().setCursor(Cursor.DEFAULT);
    }

    private void showProjectInProgress() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Informacja");
        alert.setHeaderText("Informacja");
        alert.setContentText("Nie udało nam się zaimplementować sieci neuronowej w Javie. " +
                "Algorytm poprawnie oddziela litery oraz zapisuje je do folderu GeneratedLetters. " +
                "Więcej informacji na ten temat znajduje się w dokumentacji.");
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.showAndWait();
    }

    public void onTextReconRequest(ActionEvent event) {
        if (img != null) {
            img.generateSeparatedLetters();
            this.showProjectInProgress();
        }
    }
}
