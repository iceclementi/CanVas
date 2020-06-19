package seedu.canvas.component.canvas;

import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class GridPoint extends Circle {

    /**
     * Constructor for a grid point on the canvas grid.
     *
     * @param x
     *  The x coordinate of the grid point on the grid
     * @param y
     *  The y coordinate of the grid point on the grid
     */
    public GridPoint(double x, double y) {
        super(x, y, 5.0d);

        initialiseStyle();
        initialiseEvents();
    }

    private void initialiseStyle() {
        setFill(Color.web("#749dd9", 0.5));
    }

    private void initialiseEvents() {
        setOnMouseEntered(this::onHover);
        setOnMouseExited(this::onUnhover);
    }

    private void onHover(MouseEvent mouseEvent) {
        setFill(Color.web("#73d98d", 0.8));
        CanvasGrid.setTargetGridPoint(this);
    }

    private void onUnhover(MouseEvent mouseEvent) {
        setFill(Color.web("#749dd9", 0.5));
        CanvasGrid.setTargetGridPoint(null);
    }
}
