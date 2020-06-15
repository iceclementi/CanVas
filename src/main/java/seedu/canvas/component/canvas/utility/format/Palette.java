package seedu.canvas.component.canvas.utility.format;

import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import seedu.canvas.component.canvas.TheCanvas;
import seedu.canvas.storage.FilePath;

public class Palette {

    private ColourTarget target = ColourTarget.LINE;
    private Color lineColour = TheCanvas.DEFAULT_LINE_COLOUR;
    private Color fillColour = TheCanvas.DEFAULT_FILL_COLOUR;

    private ColourButton colourButton;
    private VBox colourTargetBox;
    private HBox paletteBox;

    public Palette(ColourButton colourButton, VBox colourTargetBox, HBox paletteBox) {
        this.colourButton = colourButton;
        this.colourTargetBox = colourTargetBox;
        this.paletteBox = paletteBox;
    }

    public void initialise() {
        initialisePaletteBox();
        initialiseTargetBox();
    }

    public void changeTarget(ColourTarget target) {
        this.target = target;

        if (target == ColourTarget.LINE) {
            PaletteColour.pick(lineColour);
        } else {
            PaletteColour.pick(fillColour);
        }
    }

    public void pickColour(Color colour) {
        if (target == ColourTarget.LINE) {
            lineColour = colour;
            colourButton.pickLineColour(colour);
        } else {
            fillColour = colour;
            colourButton.pickFillColour(colour);
        }
    }

    private void initialiseTargetBox() {
        ColourTargetButton colourLineButton =
                new ColourTargetButton(this, ColourTarget.LINE, FilePath.CANVAS_COLOUR_LINE_BUTTON_IMAGE_PATH);
        ColourTargetButton colourFillButton =
                new ColourTargetButton(this, ColourTarget.FILL, FilePath.CANVAS_COLOUR_FILL_BUTTON_IMAGE_PATH);

        colourTargetBox.getChildren().addAll(colourLineButton, colourFillButton);
    }

    private void initialisePaletteBox() {
        ShadeGroup redShades = new ShadeGroup(
                new PaletteColour(this, Color.PINK),
                new PaletteColour(this, Color.RED),
                new PaletteColour(this, Color.DARKRED)
        );

        ShadeGroup orangeShades = new ShadeGroup(
                new PaletteColour(this, Color.GOLD),
                new PaletteColour(this, Color.ORANGE),
                new PaletteColour(this, Color.INDIANRED)
        );

        ShadeGroup greenShades = new ShadeGroup(
                new PaletteColour(this, Color.DARKSEAGREEN),
                new PaletteColour(this, Color.FORESTGREEN),
                new PaletteColour(this, Color.DARKGREEN)
        );

        ShadeGroup blueShades = new ShadeGroup(
                new PaletteColour(this, Color.LIGHTSTEELBLUE),
                new PaletteColour(this, Color.ROYALBLUE),
                new PaletteColour(this, Color.MIDNIGHTBLUE)
        );

        ShadeGroup purpleShades = new ShadeGroup(
                new PaletteColour(this, Color.PLUM),
                new PaletteColour(this, Color.BLUEVIOLET),
                new PaletteColour(this, Color.INDIGO)
        );

        ShadeGroup darkShades = new ShadeGroup(
                new PaletteColour(this, Color.LIGHTGRAY),
                new PaletteColour(this, Color.GRAY),
                new PaletteColour(this, Color.BLACK)
        );

        ShadeGroup lightShades = new ShadeGroup(
                new PaletteColour(this, Color.WHITE),
                new PaletteColour(this, Color.TRANSPARENT)
        );

        paletteBox.getChildren().addAll(redShades, orangeShades, greenShades, blueShades,
                purpleShades, darkShades, lightShades);
    }
}
