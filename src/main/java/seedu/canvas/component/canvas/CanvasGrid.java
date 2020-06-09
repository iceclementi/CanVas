package seedu.canvas.component.canvas;

import seedu.canvas.component.canvas.unit.RectangleUnit;

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

    public static GridPoint getTargetGridPoint() {
        return targetGridPoint;
    }

    public static void setTargetGridPoint(GridPoint targetGridPoint) {
        CanvasGrid.targetGridPoint = targetGridPoint;
    }

    public static void selectRectangleAnchorPoints(RectangleUnit rectangleUnit,
            int pointX, int pointY, int widthUnit, int heightUnit) {
        for (GridPoint gridPoint : rectangleUnit.getAnchorPoints()) {
            if (gridPoint == null) {
                break;
            }
            gridPoint.unanchor();
        }

        rectangleUnit.setAnchorPoints(
                gridPoints.get(pointX).get(pointY),
                gridPoints.get(pointX + widthUnit).get(pointY),
                gridPoints.get(pointX).get(pointY + heightUnit),
                gridPoints.get(pointX + widthUnit).get(pointY + heightUnit
        ));

        for (GridPoint gridPoint : rectangleUnit.getAnchorPoints()) {
            gridPoint.anchor();
        }
    }
}
