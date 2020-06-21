package seedu.canvas.component.canvas.unit;

import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import seedu.canvas.component.canvas.CanvasGrid;
import seedu.canvas.component.canvas.DragData;
import seedu.canvas.component.canvas.Gesture;
import seedu.canvas.component.canvas.TheCanvas;

public class LineMoveHandle extends CanvasHandle {

    private TheCanvas canvas = TheCanvas.getInstance();

    private LineUnit unit;
    private Gesture gesture = Gesture.MOVE;
    private DragData unitDragData = new DragData();

    public LineMoveHandle(LineUnit unit) {
        super(null);
        this.unit = unit;

        initialiseStyle();
        initialiseEvents();
    }

    private void initialiseStyle() {
        focus();

        centerXProperty().bind(unit.startXProperty().add(unit.endXProperty()).divide(2));
        centerYProperty().bind(unit.startYProperty().add(unit.endYProperty()).divide(2));

        setCursor(Cursor.MOVE);
    }

    private void initialiseEvents() {
        setOnMousePressed(mouseEvent -> {
            mouseLocation = new Point2D(mouseEvent.getSceneX(), mouseEvent.getSceneY());
            previousHandleLocation = new UnitPoint(unit.getUnitStartX(), unit.getUnitStartY());

            if (mouseEvent.isControlDown()) {
                unitDragData.getCopiedUnits().add(unit);
                gesture = Gesture.COPY;
            }

            unit.interact();
            CanvasGrid.showGridPoints();
        });

        setOnMouseReleased(mouseEvent -> {
            mouseLocation = null;

            unitDragData.reset();
            gesture = Gesture.MOVE;

            unit.focus();
            CanvasGrid.hideGridPoints();
        });

        setOnMouseDragged(mouseEvent -> {
            if (gesture == Gesture.MOVE) {
                int deltaX = CanvasGrid.toUnit(canvas.toScale(mouseEvent.getSceneX() - mouseLocation.getX()));
                int deltaY = CanvasGrid.toUnit(canvas.toScale(mouseEvent.getSceneY() - mouseLocation.getY()));

                int newUnitStartX = previousHandleLocation.getUnitX() + deltaX;
                int newUnitStartY = previousHandleLocation.getUnitY() + deltaY;

                unit.move(newUnitStartX, newUnitStartY);
            }

            if (mouseEvent.isControlDown() && gesture == Gesture.COPY) {
                int mouseUnitX = CanvasGrid.toUnit(mouseEvent.getX());
                int mouseUnitY = CanvasGrid.toUnit(mouseEvent.getY());

                unit.dragCopy(mouseUnitX, mouseUnitY, unitDragData);
            }
        });
    }
}
