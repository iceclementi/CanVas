package seedu.canvas.component.canvas.utility.format.text;

import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import seedu.canvas.component.canvas.text.TextBox;

import java.util.ArrayList;

public class TextPaletteColour extends Circle {

    private static ArrayList<TextPaletteColour> paletteColours = new ArrayList<>();
    private static TextPaletteColour pickedColour = null;

    private boolean isPicked = false;

    private Color colour;

    public TextPaletteColour(Color colour) {
        super(12);
        this.colour = colour;

        paletteColours.add(this);

        initialiseStyle();
        initialiseEvents();
    }

    public static void pick(Color colour) {
        for (TextPaletteColour paletteColour : paletteColours) {
            if (paletteColour.colour == colour) {
                paletteColour.pick();
                return;
            }
        }
    }

    private void pick() {
        if (pickedColour != null) {
            pickedColour.isPicked = false;
            pickedColour.reset();
        }

        pickedColour = this;

        isPicked = true;

        TextBox textBox = TextFormatBox.getTextBox();

        if (textBox != null) {
            textBox.applyTextFill(getColourCode());
        }
    }

    private String getColourCode() {
        return colour.toString().replace("0x", "#");
    }

    private void initialiseStyle() {
        if (colour == Color.WHITE) {
            setStroke(Color.LIGHTGRAY);
        } else if (colour == Color.TRANSPARENT) {
            setStroke(Color.LIGHTGRAY);
            getStrokeDashArray().addAll(4d, 3d);
        } else {
            setStroke(Color.TRANSPARENT);
        }

        setStrokeWidth(1);

        if (colour == Color.TRANSPARENT) {
            setFill(Color.WHITE);
        } else {
            setFill(colour);
        }
    }

    private void initialiseEvents() {
        setOnMouseEntered(this::onHover);
        setOnMouseExited(this::onUnhover);
        setOnMouseReleased(this::onRelease);
    }

    private void onHover(MouseEvent mouseEvent) {
        if (!isPicked) {
            setEffect(new DropShadow(BlurType.TWO_PASS_BOX, Color.GOLD, 2, 1, 0, 0));
        }
    }

    private void onUnhover(MouseEvent mouseEvent) {
        if (!isPicked) {
            reset();
        }
    }

    private void onRelease(MouseEvent mouseEvent) {
        pick();
        setEffect(new DropShadow(BlurType.TWO_PASS_BOX, Color.GOLD, 3, 2, 0, 0));
    }

    private void reset() {
        setEffect(null);
    }
}

