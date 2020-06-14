package seedu.canvas.component.canvas.utility;

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
    private Circle colourCircle = new Circle(16);

    private VBox colourPopupBox;
    private HBox lineColourBox;
    private HBox fillColourBox;

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

    public void initialisePopup(VBox colourPopupBox, HBox lineColourBox, HBox fillColourBox) {
        this.colourPopupBox = colourPopupBox;
        this.lineColourBox = lineColourBox;
        this.fillColourBox = fillColourBox;

        ComponentUtil.setStyleClass(colourPopupBox, FilePath.CANVAS_STYLE_PATH, "accessory-popup");

        // Popup box position
        colourPopupBox.setTranslateX(285);
        colourPopupBox.setTranslateY(125);

        fillPopup();
    }

    public void pickLineColour(Color colour) {
        colourCircle.setStroke(colour);
        canvas.setLineColour(colour);
    }

    public void pickFillColour(Color colour) {
        colourCircle.setFill(colour);
        canvas.setFillColour(colour);
    }

    private void initialiseStyle() {
        colourCircle.setStroke(TheCanvas.DEFAULT_LINE_COLOUR);
        colourCircle.setStrokeWidth(5);
        colourCircle.setFill(TheCanvas.DEFAULT_FILL_COLOUR);

        setGraphic(colourCircle);
        setText(" ");
        setContentDisplay(ContentDisplay.TOP);
    }

    private void fillPopup() {
        lineColourBox.getChildren().addAll(
                new LineColourPicker(this, Color.RED),
                new LineColourPicker(this, Color.GREEN),
                new LineColourPicker(this, Color.MIDNIGHTBLUE),
                new LineColourPicker(this, Color.PURPLE)
        );

        fillColourBox.getChildren().addAll(
                new FillColourPicker(this, Color.RED),
                new FillColourPicker(this, Color.GREEN),
                new FillColourPicker(this, Color.MIDNIGHTBLUE),
                new FillColourPicker(this, Color.PURPLE),
                new FillColourPicker(this, Color.TRANSPARENT)
        );
    }

    private void showPopup() {
        selectButton(this);
        colourPopupBox.setVisible(true);
    }

    private void hidePopup() {
        selectButton(null);
        colourPopupBox.setVisible(false);
    }

    private void initialiseEvents() {
        setOnMouseClicked(this::onClick);


        focusedProperty().addListener(observable -> {
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
}
