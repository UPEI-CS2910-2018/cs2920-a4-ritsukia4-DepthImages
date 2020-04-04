import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Complete this class as part of the assignment
 */
public class DepthImageProcessor implements ImageProcessor{
    private double currentThreshold;
    private double minDistance;
    private double maxDistance;
    private int width;
    private int height;
    private double [][] imageArray;
    private int[][] colorImg;
    private Blobs[][] blobs;
    private Blobs[][] newBlobs;
    private ArrayList<Blob> blobArray = new ArrayList<>();

    @Override
    public void processFile(String fileString) {

        try {
            File file = new File("A4-VideoColour-starter/" + fileString);
            Scanner fileScanner;
            fileScanner = new Scanner(file);

            width = fileScanner.nextInt();
            height = fileScanner.nextInt();

            imageArray = new double[height][width];
            //if(colorImg == null)
                colorImg = new int[height][width];

            if(blobs == null)
                blobs = new Blobs[height][width];

            blobArray.clear();

            for(int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    imageArray[i][j] = fileScanner.nextDouble();
                }
            }

        } catch (FileNotFoundException e) {
            System.out.println("No such file found");
            e.getStackTrace();
        }

        setColorImg();
    }

    /**
     * Helper method to create an array of colorImg
     */
    private void setColorImg()
    {
        boolean [][] visited = new boolean[height][width];
        int[][] newColorImg = new int[height][width];
        newBlobs = new Blobs[height][width];

        for(boolean[] a : visited)
            Arrays.fill(a, false);

        for(int i = 0; i < height; i++)
        {
            for(int j = 0; j < width; j++)
            {
                if(imageArray[i][j] > maxDistance || imageArray[i][j] < minDistance) {
                    visited[i][j] = true;
                    newBlobs[i][j] = null;
                    colorImg[i][j] = 0;
                }
                else if (!visited[i][j]) {
                    BFS(newColorImg, visited, i, j);
                    //colorImg[i][j] = 1000000000;
                }
            }
        }

        for(int i = 0; i < height; i++) {
            for(int j = 0; j < width; j++) {
                if(newBlobs[i][j] != null)
                    colorImg[i][j] = newBlobs[i][j].getColor();
            }
        }

        blobs = newBlobs;
        //colorImg = newColorImg;
    }

    /**
     * Helper method to use breath first search to find a blob area being assigned as the same color
     *
     * @param visited an array representing which pixels has been checked so far
     * @param r a row at which bfs starts
     * @param c a column bfs starts
     */
    private void BFS(int[][] newColorImg ,boolean[][] visited, int r, int c)
    {
        Queue<Point> searchQueue = new ArrayDeque<>();
        searchQueue.add(new Point(r, c));
        int rowSum = 0;
        int colSum = 0;
        int counter = 0;

        Blobs blob = new Blobs();

        while(!searchQueue.isEmpty())
        {
            counter++;
            Point p = searchQueue.poll();
            int row = p.x;
            int col = p.y;

            rowSum += row;
            colSum += col;

            newBlobs[row][col] = blob;

            //left
            if(isValidMove(row - 1, col)) {
                if (!visited[row - 1][col] && isWithinThreshold(row, col, row - 1, col)) {
                    visited[row - 1][col] = true;
                    searchQueue.add(new Point(row - 1, col));
                }
            }
            //right
            if(isValidMove(row + 1, col)) {
                if (!visited[row + 1][col] && isWithinThreshold(row, col, row + 1, col)) {
                    visited[row + 1][col] = true;
                    searchQueue.add(new Point(row + 1, col));
                }
            }
            //up
            if(isValidMove(row, col - 1)) {
                if (!visited[row][col - 1] && isWithinThreshold(row, col, row, col - 1)) {
                    visited[row][col - 1] = true;
                    searchQueue.add(new Point(row, col - 1));
                }
            }
            //down
            if(isValidMove(row, col + 1)) {
                if (!visited[row][col + 1] && isWithinThreshold(row, col, row, col + 1)) {
                    visited[row][col + 1] = true;
                    searchQueue.add(new Point(row, col + 1));
                }
            }
            //upper left
            if(isValidMove(row - 1, col - 1)) {
                if (!visited[row - 1][col - 1] && isWithinThreshold(row, col, row - 1, col - 1)) {
                    visited[row - 1][col - 1] = true;
                    searchQueue.add(new Point(row - 1, col - 1));
                }
            }
            //upper right
            if(isValidMove(row - 1, col + 1)) {
                if (!visited[row - 1][col + 1] && isWithinThreshold(row, col, row - 1, col + 1)) {
                    visited[row - 1][col + 1] = true;
                    searchQueue.add(new Point(row - 1, col + 1));
                }
            }
            //lower left
            if(isValidMove(row + 1, col - 1)) {
                if (!visited[row + 1][col - 1] && isWithinThreshold(row, col, row + 1, col - 1)) {
                    visited[row + 1][col - 1] = true;
                    searchQueue.add(new Point(row + 1, col - 1));
                }
            }
            //lowe right
            if(isValidMove(row + 1, col + 1)) {
                if (!visited[row + 1][col + 1] && isWithinThreshold(row, col, row + 1, col + 1)) {
                    visited[row + 1][col + 1] = true;
                    searchQueue.add(new Point(row + 1, col + 1));
                }
            }
        }


        //Create a new blob object
        int rowAve = rowSum / counter;
        int colAve = colSum / counter;
        blob.setCentroid(rowAve, colAve);

        if(blobs[rowAve][colAve] != null && blobs[rowAve][colAve].getColor() != 0) {
            blob.setLabel(blobs[rowAve][colAve].getLabel());
            blob.setColor(blobs[rowAve][colAve].getColor());
            blobs[rowAve][colAve].setColor(0);
        }

        blobArray.add(blob);

    }
    /**
     * Helper method to check the given row and col are valid
     *
     * @param row
     * @param col
     * @return true if it is a valid pixel
     */
    private boolean isValidMove(int row, int col)
    {
        return row >= 0 && row < height && col >= 0 && col < width;
    }

    /**
     * Helper method to check if the distance of two pixels are within the current threshold
     *
     * @param r1 the current row
     * @param c1 the current column
     * @param r2 a neighboring row
     * @param c2 a neighboring column
     * @return true if the distance of the two pixels are within the threshold
     */
    private boolean isWithinThreshold(int r1, int c1, int r2, int c2)
    {
        return Math.abs(imageArray[r1][c1] - imageArray[r2][c2]) < currentThreshold;
    }

    @Override
    public double[][] getRawImg() {
        return imageArray;
    }

    @Override
    public int[][] getColorImg() {
        return colorImg;
    }

    @Override
    public ArrayList<Blob> getBlobs() {
        return blobArray;
    }

    @Override
    public void setThreshold(double thres) {
        currentThreshold = thres;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public void setMinDist(double minDist) {
        minDistance = minDist;
    }

    @Override
    public void setMaxDist(double maxDist) {
        maxDistance =  maxDist;
    }
}
