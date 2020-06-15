package seedu.canvas.component.canvas.utility.format;

import javafx.geometry.Pos;
import javafx.scene.layout.VBox;

public class ShadeGroup extends VBox {

    public ShadeGroup(PaletteColour ... paletteColours) {

        initialiseStyle();
        getChildren().addAll(paletteColours);
    }

    private void initialiseStyle() {
        setAlignment(Pos.CENTER);
        setSpacing(5);
    }
}
