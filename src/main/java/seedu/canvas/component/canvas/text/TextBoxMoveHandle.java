package seedu.canvas.component.canvas.text;

import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import seedu.canvas.component.canvas.TheCanvas;
import seedu.canvas.component.canvas.unit.CanvasHandle;

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
        unfocus();

        centerXProperty().bind(wrapper.xProperty().add(wrapper.widthProperty().divide(2)));
        centerYProperty().bind(wrapper.yProperty().add(wrapper.heightProperty().divide(2)));

        setCursor(Cursor.MOVE);
    }

    private void initialiseEvents() {
        setOnMousePressed(mouseEvent -> {
            mouseLocation = new Point2D(mouseEvent.getSceneX(), mouseEvent.getSceneY());
            previousAnchorPoint = new Point2D(wrapper.getX(), wrapper.getY());
            wrapper.getTextBox().interact();
        });

        setOnMouseReleased(mouseEvent -> {
            mouseLocation = null;
            wrapper.getTextBox().focus();
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
