package seedu.canvas.component.canvas;

public class CanvasGrid {

    private static GridPoint targetGridPoint = null;

    /**
     * Constructor for the canvas grid.
     */
    public CanvasGrid() {
    }

    public static GridPoint getTargetGridPoint() {
        return targetGridPoint;
    }

    public static void setTargetGridPoint(GridPoint targetGridPoint) {
        CanvasGrid.targetGridPoint = targetGridPoint;
    }

    public void initialise() {
        TheCanvas canvas = TheCanvas.getInstance();

        double width = 600;
        double height = 600;

        double offset = 20d;
        for (double i = 0d; i <= width; i += offset) {
            for (double j = 0d; j <= height; j += offset) {
                canvas.getChildren().add(new GridPoint(i, j));
            }
        }
    }

}
