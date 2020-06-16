package seedu.canvas.component.canvas.unit;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import seedu.canvas.component.canvas.Direction;

public abstract class ResizeHandle extends Circle {

    protected Point2D mouseLocation = null;
    protected UnitPoint previousHandleLocation = null;
    protected boolean isResizing = false;

    protected Direction location;

    public ResizeHandle(Direction location) {
        super(4);

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
