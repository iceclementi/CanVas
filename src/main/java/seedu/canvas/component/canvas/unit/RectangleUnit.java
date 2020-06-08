package seedu.canvas.component.canvas.unit;

import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class RectangleUnit extends Rectangle {

    public RectangleUnit(double x, double y, double width, double height) {
        super(x, y, width, height);

        initialiseStyle();
        initialiseEvents();
    }

    private void initialiseStyle() {
        setStroke(Color.MIDNIGHTBLUE);
        setStrokeWidth(2);
        setFill(Color.TRANSPARENT);
    }

    private void initialiseEvents() {
        UnitEventManager unitEventManager = new UnitEventManager();

        addEventFilter(MouseEvent.MOUSE_PRESSED, unitEventManager.getOnMousePressed());
        addEventFilter(MouseEvent.MOUSE_DRAGGED, unitEventManager.getOnMouseDragged());
    }
}
