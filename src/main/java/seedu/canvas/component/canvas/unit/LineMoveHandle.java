package seedu.canvas.component.canvas.unit;

import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import seedu.canvas.component.canvas.*;

public class LineMoveHandle extends CanvasHandle {

    private TheCanvas canvas = TheCanvas.getInstance();

    private LineUnit unit;
    private Gesture gesture = Gesture.MOVE;
    private DragData unitDragData = new DragData();
    private Point2D moveHandleLocation = null;

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
        addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent -> {
            if (mouseEvent.isPrimaryButtonDown()) {

                if (canvas.getCanvasMode() == CanvasMode.SHAPE) {
                    canvas.changeMode(CanvasMode.POINT);
                }

                mouseLocation = new Point2D(mouseEvent.getSceneX(), mouseEvent.getSceneY());
                moveHandleLocation = new Point2D(unit.getStartX(), unit.getStartY());

                if (mouseEvent.isControlDown()) {
                    unitDragData.getCopiedUnits().add(unit);
                    gesture = Gesture.COPY;
                }

                unit.interactSingle();
                CanvasGrid.showGridPoints();

                unit.requestFocus();
                mouseEvent.consume();
            }
        });

        setOnMouseReleased(mouseEvent -> {
            mouseLocation = null;

            unitDragData.reset();
            gesture = Gesture.MOVE;

            unit.focusSingle();
            CanvasGrid.hideGridPoints();
        });

        setOnMouseDragged(mouseEvent -> {
            if (gesture == Gesture.MOVE) {
                double deltaX = canvas.toScale(mouseEvent.getSceneX() - mouseLocation.getX());
                double deltaY = canvas.toScale(mouseEvent.getSceneY() - mouseLocation.getY());

                double newStartX = moveHandleLocation.getX() + deltaX;
                double newStartY = moveHandleLocation.getY() + deltaY;

                unit.move(newStartX, newStartY);
            }

            if (mouseEvent.isControlDown() && gesture == Gesture.COPY) {
                int mouseUnitX = CanvasGrid.toUnit(mouseEvent.getX());
                int mouseUnitY = CanvasGrid.toUnit(mouseEvent.getY());

                unit.dragCopy(mouseUnitX, mouseUnitY, unitDragData);
            }
        });
    }
}
