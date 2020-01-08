package binarize;

import histogram.Histogram;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

public class Img {
    private WritableImage binaryImg;
    private WritableImage grayscaledImg;
    private Image inputImg;
    private int height;
    private int width;
    private PixelReader pxInputImgReader;
    private PixelReader pxGrayImgReader;
    private PixelWriter pxGrayImgWriter;
    private PixelWriter pxBinaryImgWriter;
    private int defaultSegmentSize = 3;

    public Img(File imgFile) {
        inputImg = new Image(imgFile.toURI().toString());
        width = (int) inputImg.getWidth(); // be aware of it
        height = (int) inputImg.getHeight();

        System.out.println("width: " + width + " height: " + height); // OK
        binaryImg = new WritableImage(width, height);
        grayscaledImg = new WritableImage(width, height);
        pxInputImgReader = inputImg.getPixelReader();
        pxGrayImgWriter = grayscaledImg.getPixelWriter();
        pxGrayImgReader = grayscaledImg.getPixelReader();
        pxBinaryImgWriter = binaryImg.getPixelWriter();

        greyscale();
        binarize();
    }

    private void greyscale() {
        double averagePixelValue;
        Color color;
        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++) {
                color = pxInputImgReader.getColor(i, j);
                averagePixelValue = ((color.getRed() + color.getGreen() + color.getBlue()) / 3);
                pxGrayImgWriter.setColor(i, j, new Color(averagePixelValue, averagePixelValue, averagePixelValue, 1));
//                System.out.println(pxReader.getColor(i, j).getRed());
            }
    }

    public Image getInputImage() {
        return inputImg;
    }

    public Image getBinarizedImage() {
        return binaryImg;
    }

    public Image getGrayscaledImage() {
        return grayscaledImg;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    private void binarize() {


        double param;
        for (int i = 1; i < this.getWidth() - 1; i++) {
            for (int j = 1; j < this.getHeight() - 1; j++) {
                adaptiveOtsu(i, j);
            }
//            System.out.println("i = " + i);
        }
    }

    private void adaptiveOtsu(int x, int y) {
        Histogram histogram = new Histogram(0.0, 1.0 / 255, 1);
        ArrayList<Double> classVariances = new ArrayList<Double>();

        for (int i = x - defaultSegmentSize / 2; i < x + defaultSegmentSize / 2; i++)
            for (int j = y - defaultSegmentSize / 2; j < y + defaultSegmentSize / 2; j++) {

//                x = (i >= this.getWidth() ? this.width - (-i + (x + defaultSegmentSize / 2)) : i);
//                y = (j >= this.getHeight() ? this.height - (-j + (y + defaultSegmentSize / 2)) : j);
//
//                x = (i < 0 ? Math.abs(i) : x);
//                y = (j < 0 ? Math.abs(j) : y);
                System.out.println("x = " + x + " y = " + y);
                histogram.insert(pxGrayImgReader.getColor(i, j).getRed());
            }

        double bgWeight, bgMean, bgVariance, bgSum;
        double fgWeight, fgMean, fgVariance, fgSum;
        double sum = histogram.sum();

        for (int i = 0; i < histogram.getLength(); i++) {

            bgWeight = bgMean = bgVariance = bgSum = 0;
            fgWeight = fgMean = fgVariance = fgSum = 0;

            for (int b = 0; b < i; b++) {
                bgWeight += histogram.getCount(b);
                bgMean += b * bgWeight;
                bgSum += histogram.getCount(b);
            }

            bgWeight /= sum;
            bgMean /= bgSum;

            for (int f = i; f < histogram.getLength(); f++) {
                fgWeight += histogram.getCount(f);
                fgMean += f * fgWeight;
                fgSum += histogram.getCount(f);
            }

            fgWeight /= sum;
            fgMean /= fgSum;

            for (int f = i; f < histogram.getLength(); f++)
                fgVariance += (Math.pow(f - fgMean, 2) * histogram.getCount(f));

            fgVariance /= fgSum;

            classVariances.add(bgWeight * bgVariance + fgWeight * fgVariance);
        }

        int element = classVariances.indexOf(Collections.min(classVariances));
        double binarizationParam = histogram.getBeginOf(element);

//        System.out.println(binarizationParam);

        if (pxGrayImgReader.getColor(x, y).getRed() < binarizationParam)
            pxBinaryImgWriter.setColor(x, y, new Color(1, 1, 1, 1));
        else
            pxBinaryImgWriter.setColor(x, y, new Color(0, 0, 0, 1));

    }
}
