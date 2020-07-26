package seedu.canvas.component.canvas.unit;

import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import seedu.canvas.component.canvas.CanvasGrid;
import seedu.canvas.component.canvas.CanvasHandle;
import seedu.canvas.component.canvas.CanvasMode;
import seedu.canvas.component.canvas.TheCanvas;

public class LineMoveHandle extends CanvasHandle {

    private TheCanvas canvas = TheCanvas.getInstance();

    private LineUnit unit;

    public LineMoveHandle(LineUnit unit) {
        super(null);
        this.unit = unit;

        initialiseStyle();
        initialiseEvents();
    }

    private void initialiseStyle() {
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

                if (canvas.getCanvasMode() != CanvasMode.POINT) {
                    return;
                }

                addMousePressEvent(unit, mouseEvent);

                CanvasGrid.showGridPoints();
                mouseEvent.consume();
            }
        });

        setOnMouseReleased(mouseEvent -> {
            addMouseReleaseEvent(unit);

            CanvasGrid.hideGridPoints();
            mouseEvent.consume();
        });

        setOnMouseDragged(mouseEvent -> {
            addMouseDragEvent(unit, mouseEvent);

            mouseEvent.consume();
        });
    }
}
