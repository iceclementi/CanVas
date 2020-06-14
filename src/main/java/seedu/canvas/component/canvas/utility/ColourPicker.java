package seedu.canvas.component.canvas.utility;

import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public abstract class ColourPicker extends Circle {

    protected boolean isPicked = false;

    protected ColourButton colourButton;
    protected Color colour;

    public ColourPicker(ColourButton colourButton, Color colour) {
        super(14);
        this.colourButton = colourButton;
        this.colour = colour;

        initialiseEvents();
    }

    private void initialiseEvents() {
        setOnMouseEntered(this::onHover);
        setOnMouseExited(this::onUnhover);
        setOnMouseReleased(this::onRelease);
    }

    private void onHover(MouseEvent mouseEvent) {
        if (!isPicked) {
            setEffect(new DropShadow(3, Color.ORANGE));
        }
    }

    private void onUnhover(MouseEvent mouseEvent) {
        if (!isPicked) {
            reset();
        }
    }

    protected abstract void onRelease(MouseEvent mouseEvent);

    protected void reset() {
        setEffect(null);
    }
}
