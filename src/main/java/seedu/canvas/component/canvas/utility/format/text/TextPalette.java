package seedu.canvas.component.canvas.utility.format.text;

import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class TextPalette extends HBox {

    public TextPalette() {
        super();

        initialiseStyle();
        initialisePalette();
    }

    private void initialiseStyle() {
        setAlignment(Pos.CENTER);
        setSpacing(3);
    }

    private void initialisePalette() {
        getChildren().addAll(
                generateShadeGroup(Color.RED, Color.RED, Color.DARKRED),
                generateShadeGroup(Color.GOLD, Color.ORANGE, Color.INDIANRED),
                generateShadeGroup(Color.DARKSEAGREEN, Color.FORESTGREEN, Color.DARKGREEN),
                generateShadeGroup(Color.LIGHTSTEELBLUE, Color.ROYALBLUE, Color.MIDNIGHTBLUE),
                generateShadeGroup(Color.PLUM, Color.BLUEVIOLET, Color.INDIGO),
                generateShadeGroup(Color.LIGHTGRAY, Color.GRAY, Color.BLACK),
                generateShadeGroup(Color.WHITE, Color.TRANSPARENT)
        );
    }

    private VBox generateShadeGroup(Color... colours) {
        VBox shadeGroup = new VBox();
        shadeGroup.setAlignment(Pos.CENTER);
        shadeGroup.setSpacing(3);

        for (Color colour : colours) {
            shadeGroup.getChildren().add(new TextPaletteColour(colour));
        }

        return shadeGroup;
    }
}
