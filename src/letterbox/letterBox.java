package letterbox;

import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class letterBox {

    private ArrayList<lettersPixel> letter;
    private WritableImage separatedLetter;

    private class lettersPixel {
        private int i;
        private int j;

        public lettersPixel(int i, int j) {
            this.i = i;
            this.j = j;
        }

        public int getI() {
            return this.i;
        }

        public int getJ() {
            return this.j;
        }
    }

    public letterBox() {
        this.letter = new ArrayList<lettersPixel>();
    }

    public letterBox(int i, int j) {
        this.letter = new ArrayList<lettersPixel>();
        this.letter.add(new lettersPixel(i, j));
    }

    public void addPixel(int i, int j) {
        this.letter.add(new lettersPixel(i, j));
    }

    public boolean isNeighbour(int i, int j) {
        for (var k : letter) {
            if (k.getI() - 1 <= i && k.getI() + 1 >= i && k.getJ() - 1 <= j && k.getJ() + 1 >= j) {
                this.letter.add(new lettersPixel(i, j));
                return true;
            }
        }
        return false;
    }

    public void drawRectangle(PixelWriter px, PixelReader binImgReader, int width, int height) {
        if (this.letter.size() < 2)
            return;

        int minI = getMinI();
        int maxI = getMaxI();
        int minJ = getMinJ();
        int maxJ = getMaxJ();

        WritableImage img = new WritableImage(maxI - minI + 1, maxJ - minJ + 1);

        for (int i = minI; i <= maxI && i < width; i++) {
            for (int j = minJ; j <= maxJ && j < height; j++) {
                img.getPixelWriter().setColor(i - minI, j - minJ, binImgReader.getColor(i, j));
            }
        }

        this.separatedLetter = img;
        for (int i = minI; i <= maxI && i < width; i++) {
            px.setColor(i, minJ, new Color(1, 0, 0, 1));
            px.setColor(i, maxJ, new Color(1, 0, 0, 1));
        }

        for (int j = minJ; j <= maxJ && j < height; j++) {
            px.setColor(minI, j, new Color(1, 0, 0, 1));
            px.setColor(maxI, j, new Color(1, 0, 0, 1));
        }
    }

    public WritableImage getSeparatedLetter() {
        return separatedLetter;
    }

    private int getMinI() {
        int minI = letter.get(0).getI();
        for (var k : letter)
            if (minI > k.getI())
                minI = k.getI();
        return minI;
    }

    private int getMaxI() {
        int maxI = letter.get(0).getI();
        for (var k : letter)
            if (maxI < k.getI())
                maxI = k.getI();
        return maxI;
    }

    private int getMinJ() {
        int minJ = letter.get(0).getJ();
        for (var k : letter)
            if (minJ > k.getJ())
                minJ = k.getJ();
        return minJ;
    }

    private int getMaxJ() {
        int maxJ = letter.get(0).getJ();
        for (var k : letter)
            if (maxJ < k.getJ())
                maxJ = k.getJ();
        return maxJ;
    }
}
