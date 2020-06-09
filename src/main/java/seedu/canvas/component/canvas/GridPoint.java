package seedu.canvas.component.canvas;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class GridPoint extends Circle {

    private BooleanProperty isAnchored = new SimpleBooleanProperty(false);

    /**
     * Constructor for a grid point on the canvas grid.
     *
     * @param x
     *  The x coordinate of the grid point on the grid
     * @param y
     *  The y coordinate of the grid point on the grid
     */
    public GridPoint(double x, double y) {
        super(x, y, 8.0d);

        initialiseStyle();
        initialiseEvents();
    }

    public void anchor() {
        isAnchored.set(true);
    }

    public void unanchor() {
        isAnchored.set(false);
    }

    private void initialiseStyle() {
        setFill(Color.web("#749dd9", 0.5));
    }

    private void initialiseEvents() {
        setOnMouseEntered(this::onHover);
        setOnMouseExited(this::onUnhover);

        isAnchored.addListener(value -> {
            if (isAnchored.get()) {
                setFill(Color.web("#73d98d", 0.8));
            } else {
                setFill(Color.web("#749dd9", 0.5));
            }
        });
    }

    private void onHover(MouseEvent mouseEvent) {
        isAnchored.set(true);
        CanvasGrid.setTargetGridPoint(this);
    }

    private void onUnhover(MouseEvent mouseEvent) {
        isAnchored.set(false);
        CanvasGrid.setTargetGridPoint(null);
    }
}
