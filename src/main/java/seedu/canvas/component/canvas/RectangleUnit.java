package seedu.canvas.component.canvas;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class RectangleUnit extends Rectangle {

    public RectangleUnit(double x, double y, double width, double height) {
        super(x, y, width, height);

        initialiseStyle();
    }

    private void initialiseStyle() {
        setStroke(Color.MIDNIGHTBLUE);
        setStrokeWidth(2);
        setFill(Color.TRANSPARENT);
    }
}
