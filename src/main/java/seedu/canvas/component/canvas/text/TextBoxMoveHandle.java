package seedu.canvas.component.canvas.text;

import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import seedu.canvas.component.canvas.DragData;
import seedu.canvas.component.canvas.Gesture;
import seedu.canvas.component.canvas.TheCanvas;
import seedu.canvas.component.canvas.CanvasHandle;

public class TextBoxMoveHandle extends CanvasHandle {

    private DragData textBoxDragData = new DragData();
    private Point2D previousAnchorPoint = null;
    private Gesture gesture = Gesture.MOVE;

    private TextBoxWrapper wrapper;

    public TextBoxMoveHandle(TextBoxWrapper wrapper) {
        super(null);
        this.wrapper = wrapper;

        initialiseStyle();
        initialiseEvents();
    }

    private void initialiseStyle() {
        centerXProperty().bind(wrapper.xProperty().add(wrapper.widthProperty().divide(2)));
        centerYProperty().bind(wrapper.yProperty().add(wrapper.heightProperty().divide(2)));

        setCursor(Cursor.MOVE);

        unfocus();
    }

    private void initialiseEvents() {
        addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent -> {
            if (mouseEvent.isPrimaryButtonDown()) {
                mouseLocation = new Point2D(mouseEvent.getSceneX(), mouseEvent.getSceneY());
                previousAnchorPoint = new Point2D(wrapper.getX(), wrapper.getY());
                wrapper.getTextBox().interactSingle();

                requestFocus();

                if (mouseEvent.isControlDown()) {
                    textBoxDragData.getCopiedCanvasNodes().add(wrapper.getTextBox());
                    gesture = Gesture.COPY;
                }

                mouseEvent.consume();
            }
        });

        setOnMouseReleased(mouseEvent -> {
            mouseLocation = null;
            gesture = Gesture.MOVE;

            if (textBoxDragData.getRecentCanvasNode() != null) {
                textBoxDragData.getRecentCanvasNode().focusSingle();
                textBoxDragData.reset();
            } else {
                wrapper.getTextBox().focusSingle();
            }

            mouseEvent.consume();
        });

        setOnMouseDragged(mouseEvent -> {
            if (gesture == Gesture.MOVE) {
                double deltaX = TheCanvas.getInstance().toScale(mouseEvent.getSceneX() - mouseLocation.getX());
                double deltaY = TheCanvas.getInstance().toScale(mouseEvent.getSceneY() - mouseLocation.getY());

                double newX = previousAnchorPoint.getX() + deltaX;
                double newY = previousAnchorPoint.getY() + deltaY;

                wrapper.getTextBox().move(newX, newY);
            }

            if (mouseEvent.isControlDown() && gesture == Gesture.COPY) {
                wrapper.getTextBox().dragCopy(mouseEvent.getX(), mouseEvent.getY(), textBoxDragData);
            }

            mouseEvent.consume();
        });
    }
}
