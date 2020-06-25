package seedu.canvas.component.canvas.text;

import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;
import seedu.canvas.component.canvas.CanvasMode;
import seedu.canvas.component.canvas.Gesture;
import seedu.canvas.component.canvas.TheCanvas;

public class TextBoxEventManager {

    private TheCanvas canvas = TheCanvas.getInstance();
    private TextBox textBox = null;

    private Point2D previousMouseLocation = null;
    private Point2D previousAnchorPoint = null;

    private Gesture gesture = Gesture.MOVE;

    public TextBoxEventManager() {
    }

    public EventHandler<MouseEvent> getOnMousePressed() {
        return onMousePressed;
    }

    public EventHandler<MouseEvent> getOnMouseDragged() {
        return onMouseDragged;
    }

    public EventHandler<MouseEvent> getOnMouseReleased() {
        return onMouseReleased;
    }

    private EventHandler<MouseEvent> onMousePressed = mouseEvent -> {
        if (mouseEvent.isPrimaryButtonDown()) {

            if (canvas.getCanvasMode() == CanvasMode.SHAPE) {
                canvas.changeMode(CanvasMode.POINT);
            }

            if (canvas.getCanvasMode() != CanvasMode.POINT) {
                return;
            }

            textBox = (TextBox) mouseEvent.getSource();
            textBox.interact();

            previousAnchorPoint = new Point2D(textBox.getLayoutX(), textBox.getLayoutY());
            previousMouseLocation = new Point2D(mouseEvent.getSceneX(), mouseEvent.getSceneY());
        }
    };

    private EventHandler<MouseEvent> onMouseDragged = mouseEvent -> {
        if (canvas.getCanvasMode() != CanvasMode.POINT || !mouseEvent.isPrimaryButtonDown()) {
            return;
        }

        if (previousMouseLocation == null) {
            return;
        }

        textBox = (TextBox) mouseEvent.getSource();


        if (gesture == Gesture.MOVE) {
            double deltaX = canvas.toScale(mouseEvent.getSceneX() - previousMouseLocation.getX());
            double deltaY = canvas.toScale(mouseEvent.getSceneY() - previousMouseLocation.getY());

            double newStartX = previousAnchorPoint.getX() + deltaX;
            double newStartY = previousAnchorPoint.getY() + deltaY;

            textBox.move(newStartX, newStartY);
        }

        mouseEvent.consume();
    };

    private EventHandler<MouseEvent> onMouseReleased = mouseEvent -> {
        gesture = Gesture.MOVE;

        if (textBox != null) {
            textBox.focus();
        }

        textBox = null;
    };
}
