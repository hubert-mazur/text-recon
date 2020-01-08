package binarize;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.io.File;

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
    private int defaultSegmentSize;
    private int percent;

    public Img(File imgFile) {
        inputImg = new Image(imgFile.toURI().toString());
        width = (int) inputImg.getWidth();
        height = (int) inputImg.getHeight();

        binaryImg = new WritableImage(width, height);
        grayscaledImg = new WritableImage(width, height);
        pxInputImgReader = inputImg.getPixelReader();
        pxGrayImgWriter = grayscaledImg.getPixelWriter();
        pxGrayImgReader = grayscaledImg.getPixelReader();
        pxBinaryImgWriter = binaryImg.getPixelWriter();
        this.defaultSegmentSize = this.width / 8;
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

    private double grayPixelValue(int x, int y) {
        return pxGrayImgReader.getColor(x, y).getRed();
    }

    public void setPercent(int percent) {
        this.percent = percent;
    }

    private void binarize() {
        double sum;
        double[][] intImg = new double[this.width + 2 * defaultSegmentSize][this.height + 2 * defaultSegmentSize];
        int x1, x2, y1, y2;
        double count;
        for (int i = 0; i < this.width + defaultSegmentSize; i++) {
            sum = 0;
            for (int j = 0; j < this.height + defaultSegmentSize; j++) {
                if (i >= this.width || j >= this.height)
                    sum += grayPixelValue(this.width - 1, this.height - 1);
                else
                    sum += grayPixelValue(i, j);

                intImg[i + defaultSegmentSize][j + defaultSegmentSize] = (i == 0 ? sum : intImg[i - 1 + defaultSegmentSize][j + defaultSegmentSize] + sum);
            }
        }

        for (int i = 0; i < this.width; i++) {
            for (int j = 0; j < this.height; j++) {

                x1 = i - defaultSegmentSize / 2;
                x2 = i + defaultSegmentSize / 2;
                y1 = j - defaultSegmentSize / 2;
                y2 = j + defaultSegmentSize / 2;

                count = (x2 - x1) * (y2 - y1);
                sum = intImg[x2 + defaultSegmentSize][y2 + defaultSegmentSize] - intImg[x2 + defaultSegmentSize][y1 - 1 + defaultSegmentSize] - intImg[x1 - 1 + defaultSegmentSize][y2 + defaultSegmentSize] + intImg[x1 - 1 + defaultSegmentSize][y1 - 1 + defaultSegmentSize];

                if (grayPixelValue(i, j) * count <= (sum * (100 - percent) / 100.0))
                    pxBinaryImgWriter.setColor(i, j, new Color(0, 0, 0, 1));
                else
                    pxBinaryImgWriter.setColor(i, j, new Color(1, 1, 1, 1));
            }
        }
    }
}
