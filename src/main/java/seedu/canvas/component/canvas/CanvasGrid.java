package seedu.canvas.component.canvas;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class CanvasGrid {

    public static final double WIDTH = 600d;
    public static final double HEIGHT = 600d;
    public static final double OFFSET = 15d;
    public static final int MIN_X = 0;
    public static final int MAX_X = (int) Math.round(WIDTH / OFFSET);
    public static final int MIN_Y = 0;
    public static final int MAX_Y = (int) Math.round(HEIGHT / OFFSET);

    private static ArrayList<GridPoint> gridPoints = generateGridPoints();
    private static Canvas gridLines = generateGridLines();

    private static GridPoint targetGridPoint = null;

    /**
     * Constructor for the canvas grid.
     */
    public CanvasGrid() {
    }

    public static ArrayList<GridPoint> getGridPoints() {
        return gridPoints;
    }

    public static Canvas getGridLines() {
        return gridLines;
    }

    public static void showGridPoints() {
        gridPoints.forEach(gridPoint -> gridPoint.setVisible(true));
    }

    public static void hideGridPoints() {
        gridPoints.forEach(gridPoint -> gridPoint.setVisible(false));
    }

    public static void showGridLines() {
        gridLines.setVisible(true);
    }

    public static void hideGridLines() {
        gridLines.setVisible(false);
    }

    private static ArrayList<GridPoint> generateGridPoints() {
        ArrayList<GridPoint> gridPoints = new ArrayList<>();

        for (int row = MIN_X; row <= MAX_X; ++row) {
            for (int column = MIN_Y; column <= MAX_Y; ++column) {
                GridPoint gridPoint = new GridPoint(row * OFFSET, column * OFFSET);
                gridPoints.add(gridPoint);
            }
        }

        return gridPoints;
    }

    private static Canvas generateGridLines() {
        Canvas gridLines = new Canvas(WIDTH, HEIGHT);
        gridLines.setMouseTransparent(true);

        GraphicsContext gc = gridLines.getGraphicsContext2D();

        gc.setStroke(Color.GRAY);
        gc.setLineWidth(0.5);

        for (double i = 0d; i <= WIDTH; i += OFFSET) {
            gc.strokeLine(i, 0, i, HEIGHT);
            gc.strokeLine(0, i, WIDTH, i);
        }

        return gridLines;
    }

    /**
     * Gets the target grid point.
     *
     * @return
     *  The target grid point
     */
    public static GridPoint getTargetGridPoint() {
        return targetGridPoint;
    }

    /**
     * Sets the target grid point.
     *
     * @param targetGridPoint
     *  The targeted grid point
     */
    public static void setTargetGridPoint(GridPoint targetGridPoint) {
        CanvasGrid.targetGridPoint = targetGridPoint;
    }

    public static int toUnit(double value) {
        return (int) Math.round(value / OFFSET);
    }

    public static int clamp(int value, int minimum, int maximum) {
        return Math.min(Math.max(value, minimum), maximum);
    }
}
