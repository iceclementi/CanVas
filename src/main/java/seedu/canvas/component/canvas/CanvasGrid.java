package seedu.canvas.component.canvas;

import java.util.ArrayList;

public class CanvasGrid {

    private static final double WIDTH = 600;
    private static final double HEIGHT = 600;
    public static final double OFFSET = 20d;
    public static final int MIN_X = 0;
    public static final int MAX_X = (int) Math.round(WIDTH / OFFSET);
    public static final int MIN_Y = 0;
    public static final int MAX_Y = (int) Math.round(HEIGHT / OFFSET);

    public static ArrayList<ArrayList<GridPoint>> gridPoints = new ArrayList<>();

    private static GridPoint targetGridPoint = null;

    /**
     * Constructor for the canvas grid.
     */
    public CanvasGrid() {
    }

    /**
     * Initialises the grid points on the canvas.
     */
    public void initialise() {
        TheCanvas canvas = TheCanvas.getInstance();

        for (int row = MIN_X; row <= MAX_X; ++row) {

            ArrayList<GridPoint> gridRow = new ArrayList<>();

            for (int column = MIN_Y; column <= MAX_Y; ++column) {
                GridPoint gridPoint = new GridPoint(row * OFFSET, column * OFFSET);
                gridRow.add(gridPoint);
                canvas.getChildren().add(gridPoint);
            }

            gridPoints.add(gridRow);
        }
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
