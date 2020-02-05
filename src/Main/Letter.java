package Main;

import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

public class Letter {
    protected int begin;
    private int end;
    private int width;
    private int height;
    public PixelWriter pxWriter;
    public PixelReader pxReader;

    public WritableImage img;

    public Letter(int beginWidth, int endWidth, int height) {
        this.begin = beginWidth;
        this.end = endWidth;
        this.width = endWidth - beginWidth;

        this.height = height;

        this.img = new WritableImage(width, height);
        this.pxReader = this.img.getPixelReader();
        this.pxWriter = this.img.getPixelWriter();
    }

    public void createImage(PixelReader px) {
        for (int i = this.begin; i < this.end; i++) {
            for (int j = 0; j < this.height; j++) {
                this.pxWriter.setColor(i - this.begin, j, px.getColor(i, j));
            }
        }
    }
}
