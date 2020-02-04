package Row;

import Letter.Letter;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

import java.util.ArrayList;

public class Row {
    protected int begin;
    private int end;
    private int width;
    private int height;
    public PixelWriter pxWriter;
    public PixelReader pxReader;
    public ArrayList<Letter> letters;

    public WritableImage img;

    public Row(int beginHeight, int endHeight, int width) {
        this.begin = beginHeight;
        this.end = endHeight;
        this.width = width;

        this.height = endHeight - beginHeight;

        this.img = new WritableImage(width, height);
        this.pxReader = this.img.getPixelReader();
        this.pxWriter = this.img.getPixelWriter();
        this.letters = new ArrayList<Letter>();
    }

    public void createImage(PixelReader px) {
        for (int j = 0; j < this.width; j++) {
            for (int i = this.begin; i < this.end; i++) {
                this.pxWriter.setColor(j, i - this.begin, px.getColor(j, i));
            }
        }
    }

    private boolean columnHasBlackPixels(int currentX, PixelReader px) {
        for (int i = 0; i < height; i++) {
            if (px.getColor(currentX, i).getRed() == 0)
                return true;
        }
        return false;
    }

    public void separateLetters() {
        boolean rowHadBlackPixel = false;
        boolean rowHasBlackPixel = false;
        int begin = 0;
        int end = 0;

        for (int j = 0; j < this.width; j++) {
            rowHasBlackPixel = columnHasBlackPixels(j, pxReader);
            if (rowHasBlackPixel && !rowHadBlackPixel) {
                rowHadBlackPixel = true;
                begin = j;
                System.out.println("begin letter: " + begin);
            } else if (!rowHasBlackPixel && rowHadBlackPixel) {
                end = j;
                letters.add(new Letter(begin, end, this.height));
                rowHadBlackPixel = false;
                System.out.println("end letter: " + end);
            }
        }
//        System.out.println(letters.size());
    }

}
