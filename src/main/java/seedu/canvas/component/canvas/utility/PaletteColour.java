package seedu.canvas.component.canvas.utility;

import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.ArrayList;

public class PaletteColour extends Circle {

    private static ArrayList<PaletteColour> paletteColours = new ArrayList<>();
    private static PaletteColour pickedColour = null;

    private boolean isPicked = false;

    private Palette palette;
    private Color colour;

    public PaletteColour(Palette palette, Color colour) {
        super(16);
        this.palette = palette;
        this.colour = colour;

        paletteColours.add(this);

        initialiseStyle();
        initialiseEvents();
    }

    public static void pick(Color colour) {
        for (PaletteColour paletteColour : paletteColours) {
            if (paletteColour.colour == colour) {
                paletteColour.pick();
                return;
            }
        }
    }

    private void initialiseStyle() {
        if (colour == Color.WHITE) {
            setStroke(Color.LIGHTGRAY);
        } else if (colour == Color.TRANSPARENT) {
            setStroke(Color.LIGHTGRAY);
            getStrokeDashArray().addAll(7d, 3d);
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
            setEffect(new DropShadow(BlurType.TWO_PASS_BOX, Color.GOLD, 3, 1, 0, 0));
        }
    }

    private void onUnhover(MouseEvent mouseEvent) {
        if (!isPicked) {
            reset();
        }
    }

    private void onRelease(MouseEvent mouseEvent) {
        pick();
        palette.pickColour(colour);
    }

    private void pick() {
        if (pickedColour != null) {
            pickedColour.isPicked = false;
            pickedColour.reset();
        }

        pickedColour = this;

        isPicked = true;
        setEffect(new DropShadow(BlurType.TWO_PASS_BOX, Color.GOLD, 5, 2, 0, 0));
    }

    private void reset() {
        setEffect(null);
    }
}
