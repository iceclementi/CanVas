package seedu.canvas.component.canvas.unit;

import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import seedu.canvas.component.canvas.CanvasGrid;
import seedu.canvas.component.canvas.CanvasHandle;
import seedu.canvas.component.canvas.CanvasMode;
import seedu.canvas.component.canvas.TheCanvas;

public class ModelMoveHandle extends CanvasHandle {

    private TheCanvas canvas = TheCanvas.getInstance();

    private ModelUnit unit;

    public ModelMoveHandle(ModelUnit unit) {
        super(null);
        this.unit = unit;

        initialiseStyle();
        initialiseEvents();
    }

    private void initialiseStyle() {
        centerXProperty().bind(unit.xProperty().add(unit.widthProperty().divide(2)));
        centerYProperty().bind(unit.yProperty().add(unit.heightProperty().divide(2)));

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
