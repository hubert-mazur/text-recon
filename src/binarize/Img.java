package binarize;

import histogram.Histogram;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class Img {
    private WritableImage binaryImg;
    private Image inputImg;
    private double binarizationConstant;
    private int height;
    private int width;
    private PixelReader pxReader;
    private PixelWriter pxWriter;
    private Histogram grayImageHistogram;


    public Img(File imgFile) {
        inputImg = new Image(imgFile.toURI().toString());
        width = (int) inputImg.getWidth(); // be aware of it
        height = (int) inputImg.getHeight();

        System.out.println("width: " + width + " height: " + height); // OK
        binaryImg = new WritableImage(width, height);
        pxReader = inputImg.getPixelReader();
        pxWriter = binaryImg.getPixelWriter();

        greyscale();
        binarizeOtsu();
    }

    private void greyscale() {
        double averagePixelValue;
        Color color;
        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++) {
                color = pxReader.getColor(i, j);
                averagePixelValue = ((color.getRed() + color.getGreen() + color.getBlue()) / 3);
                pxWriter.setColor(i, j, new Color(averagePixelValue, averagePixelValue, averagePixelValue, 1));
//                System.out.println(pxReader.getColor(i, j).getRed());
            }
    }

    public Image getInputImage() {
        return inputImg;
    }

    public Image getBinarizedImage() {
        return binaryImg;
    }

    private void binarizeOtsu() {
        this.grayImageHistogram = new Histogram(0.0, 1.0 / 255, 1);

        double pixelValue;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                pixelValue = pxReader.getColor(i, j).getRed();
                this.grayImageHistogram.insert(pixelValue);
            }
        }

//        this.grayImageHistogram.print();

    }

}
