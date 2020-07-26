package seedu.canvas.component.canvas.draw;

import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import seedu.canvas.component.canvas.DragData;
import seedu.canvas.component.canvas.Gesture;
import seedu.canvas.component.canvas.TheCanvas;
import seedu.canvas.component.canvas.CanvasHandle;

public class DrawingMoveHandle extends CanvasHandle {

    private TheCanvas canvas = TheCanvas.getInstance();
    private DragData drawingDragData = new DragData();
    private Point2D previousAnchorPoint = null;
    private Gesture gesture = Gesture.MOVE;

    private DrawingWrapper wrapper;

    public DrawingMoveHandle(DrawingWrapper wrapper) {
        super(null);
        this.wrapper = wrapper;

        initialiseStyle();
        initialiseEvents();
    }

    private void initialiseStyle() {
        centerXProperty().bind(wrapper.xProperty().add(wrapper.widthProperty().divide(2)));
        centerYProperty().bind(wrapper.yProperty().add(wrapper.heightProperty().divide(2)));

        setCursor(Cursor.MOVE);
    }

    private void initialiseEvents() {
        addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent -> {
            if (mouseEvent.isPrimaryButtonDown()) {
                addMousePressEvent(wrapper.getDrawing(), mouseEvent);

                mouseEvent.consume();
            }
        });

        setOnMouseReleased(mouseEvent -> {
            addMouseReleaseEvent(wrapper.getDrawing());

            mouseEvent.consume();
        });

        setOnMouseDragged(mouseEvent -> {
            addMouseDragEvent(wrapper.getDrawing(), mouseEvent);

            mouseEvent.consume();
        });
    }
}
