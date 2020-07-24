package seedu.canvas.component.canvas.text;

import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import seedu.canvas.component.canvas.TheCanvas;
import seedu.canvas.component.canvas.CanvasHandle;

public class TextBoxMoveHandle extends CanvasHandle {

    private Point2D previousAnchorPoint = null;

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

                // wrapper.getTextBox().requestFocus();
                requestFocus();
                mouseEvent.consume();
            }
        });

        setOnMouseReleased(mouseEvent -> {
            mouseLocation = null;
            wrapper.getTextBox().focusSingle();
        });

        setOnMouseDragged(mouseEvent -> {
            double deltaX = TheCanvas.getInstance().toScale(mouseEvent.getSceneX() - mouseLocation.getX());
            double deltaY = TheCanvas.getInstance().toScale(mouseEvent.getSceneY() - mouseLocation.getY());

            double newX = previousAnchorPoint.getX() + deltaX;
            double newY = previousAnchorPoint.getY() + deltaY;

            wrapper.getTextBox().move(newX, newY);

            mouseEvent.consume();
        });
    }
}
