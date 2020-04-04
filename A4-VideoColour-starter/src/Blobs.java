import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Blobs implements Blob {

    ArrayList<Point> blobArray;
    private int row;
    private int col;
    private int label;
    private int color;
    private final int[] palette = {8956291, 13512253, 15722670, 15896621, 12515920, 16440395, 8550440, 8563325, 15762040, 15787690};
    private static int paletteCounter = 0;

    public Blobs() {
        blobArray = new ArrayList<>();

        Random r = new Random();
        label = r.nextInt(500);
        color = palette[paletteCounter % (palette.length - 1)];
        paletteCounter++;
    }

    @Override
    public Point getCentroid() {
        return new Point(row, col);
    }

    public void setCentroid(int r, int c) {
        this.row = r;
        this.col = c;
    }

    @Override
    public int getLabel() {
        return label;
    }

    @Override
    public void setLabel(int l) {
        label = l;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
