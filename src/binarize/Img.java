package binarize;

import Row.Row;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.*;
import java.io.IOException;
import java.util.ArrayList;

public class Img {
    private WritableImage grayscaledImg = null;
    private WritableImage binaryImg;
    private Image inputImg;
    private int height;
    private int width;
    private PixelReader pxInputImgReader;
    private PixelReader pxGrayImgReader;
    private PixelWriter pxGrayImgWriter;
    private PixelWriter pxBinaryImgWriter;
    private ArrayList<WritableImage> readLetters;
    private int defaultSegmentSize;
    private int percent;

    public Img(File imgFile) {
        inputImg = new Image(imgFile.toURI().toString());
        width = (int) inputImg.getWidth();
        height = (int) inputImg.getHeight();

        binaryImg = new WritableImage(width, height);
        pxInputImgReader = inputImg.getPixelReader();
        pxBinaryImgWriter = binaryImg.getPixelWriter();
        this.defaultSegmentSize = this.width / 8;
        percent = 15;
    }

    public Image getInputImage() {
        return inputImg;
    }

    private double grayPixelValue(int x, int y) {
        return pxGrayImgReader.getColor(x, y).getRed();
    }

    public void setPercent(int percent) {
        this.percent = percent;
    }

    public int getPercent() {
        return percent;
    }

    public WritableImage greyscale() {
        if (this.grayscaledImg == null) {
            grayscaledImg = new WritableImage(width, height);
            pxGrayImgWriter = grayscaledImg.getPixelWriter();
            pxGrayImgReader = grayscaledImg.getPixelReader();
        }

        double averagePixelValue;
        Color color;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                color = pxInputImgReader.getColor(i, j);
                averagePixelValue = ((color.getRed() + color.getGreen() + color.getBlue()) / 3);
                pxGrayImgWriter.setColor(i, j, new Color(averagePixelValue, averagePixelValue, averagePixelValue, 1));
            }
        }

        return grayscaledImg;
    }

    public WritableImage binarize() {
        if (this.grayscaledImg == null)
            greyscale();

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

        return this.binaryImg;
    }

    public void generateSeparatedLetters() {
        this.readLetters = regionProps();
        saveImages();
    }

    private void saveImages() {
        int i = 0;
        File file;
        try {
            for (var letter : this.readLetters) {
                i++;
                file = new File("GeneratedLetters/letter_" + i + ".png");
                if (letter == null)
                    continue;
                BufferedImage bufferedImage = SwingFXUtils.fromFXImage(letter, null);
                ImageIO.write(bufferedImage, "png", file);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public ArrayList<WritableImage> regionProps() {
        ArrayList<WritableImage> lettersArrayList = new ArrayList<>();
        ArrayList<Row> rows = new ArrayList<Row>();
        PixelReader pxBinaryImgReader = this.binaryImg.getPixelReader();
        boolean rowHadBlackPixel = false;
        boolean rowHasBlackPixel;
        int begin = 0;
        int end;

        for (int j = 0; j < this.height; j++) {
            rowHasBlackPixel = rowHasBlackPixels(j, pxBinaryImgReader);
            if (rowHasBlackPixel && !rowHadBlackPixel) {
                rowHadBlackPixel = true;
                begin = j;
            } else if (!rowHasBlackPixel && rowHadBlackPixel) {
                end = j;
                rows.add(new Row(begin, end, this.width));
                rowHadBlackPixel = false;
            }
        }

        for (var row : rows) {
            row.createImage(pxBinaryImgReader);
            row.separateLetters();
            for (var letter:row.letters) {
                letter.createImage(row.pxReader);
            }
        }

        File file;
        int letterCounter = 0;

        try {
            if (Files.notExists(Paths.get("GeneratedLetters/"))) {
                new File("GeneratedLetters").mkdirs();
            }
            for (var row : rows) {
                for (var letter: row.letters) {
                    file = new File("GeneratedLetters/letter_" + letterCounter + ".png");
                    BufferedImage bufferedImageLetter = SwingFXUtils.fromFXImage(letter.img, null);
                    ImageIO.write(bufferedImageLetter, "png", file);
                    letterCounter++;
                }
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return lettersArrayList;
    }

    private boolean rowHasBlackPixels(int currentY, PixelReader px) {
        for (int i = 0; i < this.width; i++) {
            if (px.getColor(i, currentY).getRed() == 0)
                return true;
        }
        return false;
    }

}

