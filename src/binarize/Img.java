package binarize;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import letterbox.letterBox;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

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

        this.readLetters = regionProps();
        return this.binaryImg;
    }

    public ArrayList<WritableImage> regionProps() {
        System.out.println("regionProps started at: " + (new Date()));

        ArrayList<letterBox> letters = new ArrayList<letterBox>();
        ArrayList<WritableImage> lettersArrayList = new ArrayList<WritableImage>();
        PixelReader pxBinaryImgReader = this.binaryImg.getPixelReader();
        boolean isBlack;
        boolean add;

        letters.add(new letterBox());

        for (int i = 0; i < this.width; i++) {
            for (int j = 0; j < this.height; j++) {
                isBlack = (pxBinaryImgReader.getColor(i, j).getRed() == 0);
                add = true;
                if (isBlack) {
                    if (letters.size() == 0) {
                        letters.add(new letterBox(i, j));
                    } else {
                        for (var k : letters) {
                            if (k.isNeighbour(i, j)) {
                                break;
                            } else {
                                add = false;
                            }
                        }
                        if (!add) {
                            letters.add(new letterBox(i, j));
                        }
                    }
                }
            }
        }

        for (var k : letters) {
            k.drawRectangle(pxBinaryImgWriter, pxBinaryImgReader, this.width, this.height);
            lettersArrayList.add(k.getSeparatedLetter());
        }

        System.out.println("regionProps ended at: " + (new Date()));

        return lettersArrayList;
    }
}

