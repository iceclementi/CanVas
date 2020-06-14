package seedu.canvas.component.canvas.utility;

import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class LineColourPicker extends ColourPicker {

    private static LineColourPicker pickedColour = null;

    public LineColourPicker(ColourButton colourButton, Color colour) {
        super(colourButton, colour);

        initialiseStyle();
    }

    private void initialiseStyle() {
        setStroke(colour);
        setStrokeWidth(3);
        setFill(Color.WHITE);
    }

    @Override
    protected void onRelease(MouseEvent mouseEvent) {
        pick();
    }

    private void pick() {
        if (pickedColour != null) {
            pickedColour.isPicked = false;
            pickedColour.reset();
        }

        pickedColour = this;

        isPicked = true;
        setEffect(new DropShadow(BlurType.TWO_PASS_BOX, Color.ORANGE, 5, 2, 0, 0));

        colourButton.pickLineColour(colour);
    }
}
