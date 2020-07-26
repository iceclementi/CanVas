package seedu.canvas.component.canvas.text;

import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;
import seedu.canvas.component.canvas.CanvasMode;
import seedu.canvas.component.canvas.Gesture;
import seedu.canvas.component.canvas.TheCanvas;

public class TextBoxContentEventManager {

    private TheCanvas canvas = TheCanvas.getInstance();
    private TextBox textBox = null;
    private TextBoxContent content = null;

    private Point2D previousMouseLocation = null;
    private Point2D previousAnchorPoint = null;

    private Gesture gesture = Gesture.MOVE;

    public TextBoxContentEventManager() {
    }

    public EventHandler<MouseEvent> getOnMousePressed() {
        return onMousePressed;
    }

    // public EventHandler<MouseEvent> getOnMouseDragged() {
    //     return onMouseDragged;
    // }

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

            content = (TextBoxContent) mouseEvent.getSource();
            textBox = content.getTextBox();

            textBox.interactSingle();
            content.synchroniseTextFormatButtons();

            previousAnchorPoint = new Point2D(textBox.getCanvasStartX(), textBox.getCanvasStartY());
            previousMouseLocation = new Point2D(mouseEvent.getSceneX(), mouseEvent.getSceneY());

            content.requestFocus();
        }
    };

    // private EventHandler<MouseEvent> onMouseDragged = mouseEvent -> {
    //     if (canvas.getCanvasMode() != CanvasMode.POINT || !mouseEvent.isPrimaryButtonDown()) {
    //         return;
    //     }
    //
    //     if (previousMouseLocation == null) {
    //         return;
    //     }
    //
    //     content = (TextBoxContent) mouseEvent.getSource();
    //     textBox = content.getTextBox();
    //
    //
    //     if (gesture == Gesture.MOVE) {
    //         double deltaX = canvas.toScale(mouseEvent.getSceneX() - previousMouseLocation.getX());
    //         double deltaY = canvas.toScale(mouseEvent.getSceneY() - previousMouseLocation.getY());
    //
    //         double newStartX = previousAnchorPoint.getX() + deltaX;
    //         double newStartY = previousAnchorPoint.getY() + deltaY;
    //
    //         textBox.move(newStartX, newStartY);
    //     }
    //
    //     mouseEvent.consume();
    // };

    private EventHandler<MouseEvent> onMouseReleased = mouseEvent -> {
        if (textBox != null) {
            textBox.focusSingle();
        }

        textBox = null;
        content = null;
    };
}
