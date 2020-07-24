package seedu.canvas.component.canvas.draw;

import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import seedu.canvas.component.canvas.DragData;
import seedu.canvas.component.canvas.Gesture;
import seedu.canvas.component.canvas.TheCanvas;
import seedu.canvas.component.canvas.unit.CanvasHandle;

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
                mouseLocation = new Point2D(mouseEvent.getSceneX(), mouseEvent.getSceneY());
                previousAnchorPoint = new Point2D(wrapper.getX(), wrapper.getY());
                wrapper.getDrawing().interactSingle();

                if (mouseEvent.isControlDown()) {
                    drawingDragData.getCopiedCanvasNodes().add(wrapper.getDrawing());
                    gesture = Gesture.COPY;
                }

                mouseEvent.consume();
            }
        });

        setOnMouseReleased(mouseEvent -> {
            mouseLocation = null;

            drawingDragData.reset();
            gesture = Gesture.MOVE;

            wrapper.getDrawing().focusSingle();
        });

        setOnMouseDragged(mouseEvent -> {
            if (gesture == Gesture.MOVE) {
                double deltaX = canvas.toScale(mouseEvent.getSceneX() - mouseLocation.getX());
                double deltaY = canvas.toScale(mouseEvent.getSceneY() - mouseLocation.getY());

                double newX = previousAnchorPoint.getX() + deltaX;
                double newY = previousAnchorPoint.getY() + deltaY;

                wrapper.getDrawing().move(newX, newY);
            }

            if (mouseEvent.isControlDown() && gesture == Gesture.COPY) {
                wrapper.getDrawing().dragCopy(mouseEvent.getX(), mouseEvent.getY(), drawingDragData);
            }

            mouseEvent.consume();
        });
    }
}
