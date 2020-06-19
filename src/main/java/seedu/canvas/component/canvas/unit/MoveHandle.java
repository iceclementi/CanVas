package seedu.canvas.component.canvas.unit;

import javafx.geometry.Point2D;
import seedu.canvas.component.canvas.CanvasGrid;
import seedu.canvas.component.canvas.TheCanvas;

public class MoveHandle extends CanvasHandle {

    private LineUnit unit;

    public MoveHandle(LineUnit unit) {
        super(null);
        this.unit = unit;

        initialiseStyle();
        initialiseEvents();
    }

    private void initialiseStyle() {
        focus();

        centerXProperty().bind(unit.startXProperty().add(unit.endXProperty()).divide(2));
        centerYProperty().bind(unit.startYProperty().add(unit.endYProperty()).divide(2));
    }

    private void initialiseEvents() {
        setOnMousePressed(mouseEvent -> {
            mouseLocation = new Point2D(mouseEvent.getSceneX(), mouseEvent.getSceneY());
            previousHandleLocation = new UnitPoint(unit.getUnitStartX(), unit.getUnitStartY());
            unit.interact();
        });

        setOnMouseReleased(mouseEvent -> {
            mouseLocation = null;
            unit.focus();
        });

        setOnMouseDragged(mouseEvent -> {
            double scale = TheCanvas.getInstance().getCanvasScale();

            int deltaX = CanvasGrid.toUnit((mouseEvent.getSceneX() - mouseLocation.getX()) / scale);
            int deltaY = CanvasGrid.toUnit((mouseEvent.getSceneY() - mouseLocation.getY()) / scale);

            int newUnitStartX = previousHandleLocation.getUnitX() + deltaX;
            int newUnitStartY = previousHandleLocation.getUnitY() + deltaY;

            unit.move(newUnitStartX, newUnitStartY);
        });
    }
}
