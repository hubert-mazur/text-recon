package binarize;

import javafx.scene.image.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.HashMap;

public class Img {
    private WritableImage binaryImg;
    private Image inputImg;
    private double binarizationConstant;
    private int height;
    private int width;
    private HashMap<Double, Integer> histogramData;
    private PixelReader pxReader;
    private PixelWriter pxWriter;

    public Img() {

        FileChooser fchooser = new FileChooser();
        File imgFile = fchooser.showOpenDialog(new Stage());
//        System.out.println(imgFile.getAbsolutePath());
        inputImg = new Image(imgFile.toURI().toString());
        width = (int) inputImg.getWidth(); // be aware of it
        height = (int) inputImg.getHeight();

        System.out.println("width: "+ width + " height: " + height); // OK
        binaryImg = new WritableImage(width, height);
        pxReader = inputImg.getPixelReader();
        pxWriter = binaryImg.getPixelWriter();

        greyscale();
//        binarizeOtsu();
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
        this.histogramData = new HashMap<Double, Integer>(256);
        double pixelValue;

        for (double i = 0;i<= 1;i+=1.0/255)
            histogramData.put(i,0);

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                pixelValue = pxReader.getColor(i, j).getRed();
                // TODO:: CREATE A CLASS REPRESENTING HISTOGRAM
            }
        }

    }



}

