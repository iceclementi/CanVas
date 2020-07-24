package seedu.canvas.component.canvas;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import seedu.canvas.component.canvas.Direction;
import seedu.canvas.component.canvas.unit.UnitPoint;

public abstract class CanvasHandle extends Circle {

    protected Point2D mouseLocation = null;
    protected UnitPoint previousHandleLocation = null;
    protected boolean isInteracting = false;

    protected Direction location;

    public CanvasHandle(Direction location) {
        super(5);

        this.location = location;
    }

    public void interact() {
        setFill(Color.CORNFLOWERBLUE);
        setOpacity(0.8);

        setVisible(true);
        toFront();
    }

    public void focus() {
        setFill(Color.LIGHTGREEN);
        setOpacity(0.8);

        setVisible(true);
        toFront();
    }

    public void unfocus() {
        setVisible(false);
    }
}
