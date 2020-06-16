package seedu.canvas.component.canvas.utility.format;

import javafx.scene.control.ContentDisplay;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import seedu.canvas.component.canvas.TheCanvas;
import seedu.canvas.storage.FilePath;
import seedu.canvas.util.ComponentUtil;

public class ColourButton extends AccessoryButton {

    private TheCanvas canvas = TheCanvas.getInstance();
    private Circle previewColour = new Circle(16);

    private VBox colourPopupBox;

    /**
     * Constructor for the colour accessory button.
     *
     * @param backgroundPath
     *  The path to the background image of the colour accessory button
     */
    public ColourButton(String backgroundPath) {
        super(backgroundPath);

        initialiseStyle();
        initialiseEvents();
    }

    public void initialisePopup(VBox colourPopupBox, VBox colourTargetBox, HBox paletteBox) {
        this.colourPopupBox = colourPopupBox;

        ComponentUtil.setStyleClass(colourPopupBox, FilePath.CANVAS_STYLE_PATH, "popup");

        // Popup box position
        colourPopupBox.setTranslateX(285);
        colourPopupBox.setTranslateY(125);

        Palette palette = new Palette(this, colourTargetBox, paletteBox);
        palette.initialise();
    }

    public void pickLineColour(Color colour) {
        previewColour.setStroke(colour);
        canvas.setLineColour(colour);
    }

    public void pickFillColour(Color colour) {
        previewColour.setFill(colour);
        canvas.setFillColour(colour);
    }

    private void initialiseStyle() {
        previewColour.setStroke(TheCanvas.DEFAULT_LINE_COLOUR);
        previewColour.setStrokeWidth(5);
        previewColour.setFill(TheCanvas.DEFAULT_FILL_COLOUR);

        setGraphic(previewColour);
        setText(" ");
        setContentDisplay(ContentDisplay.TOP);
    }

    private void initialiseEvents() {
        setOnMouseClicked(this::onClick);

        focusedProperty().addListener(observable -> {
            if (getScene().getFocusOwner() instanceof ColourTargetButton) {
                requestFocus();
                return;
            }

            if (!isFocused()) {
                hidePopup();
            }
        });
    }

    private void onClick(MouseEvent mouseEvent) {
        if (isSelected) {
            hidePopup();
        } else {
            showPopup();
        }
    }

    private void showPopup() {
        selectButton(this);
        colourPopupBox.setVisible(true);
    }

    private void hidePopup() {
        selectButton(null);
        colourPopupBox.setVisible(false);
    }
}
