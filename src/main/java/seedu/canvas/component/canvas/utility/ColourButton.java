package seedu.canvas.component.canvas.utility;

import javafx.scene.control.ContentDisplay;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import seedu.canvas.component.canvas.TheCanvas;

public class ColourButton extends AccessoryButton {

    private TheCanvas canvas = TheCanvas.getInstance();
    private Circle colourCircle = new Circle(16);

    private VBox colourPopupBox;

    /**
     * Constructor for the colour accessory button.
     *
     * @param backgroundPath
     *  The path to the background image of the colour accessory button
     * @param colourPopupBox
     *  The popup box for the colour selection
     */
    public ColourButton(String backgroundPath, VBox colourPopupBox) {
        super(backgroundPath);
        this.colourPopupBox = colourPopupBox;

        initialiseStyle();
        initialiseEvents();
    }

    private void initialiseStyle() {
        colourCircle.setStroke(TheCanvas.DEFAULT_LINE_COLOUR);
        colourCircle.setStrokeWidth(5);
        colourCircle.setFill(TheCanvas.DEFAULT_FILL_COLOUR);

        setGraphic(colourCircle);
        setText(" ");
        setContentDisplay(ContentDisplay.TOP);

        // Popup box position
        colourPopupBox.setTranslateX(280);
        colourPopupBox.setTranslateY(130);
    }

    private void initialiseEvents() {
        setOnMouseClicked(this::onClick);
    }

    private void onClick(MouseEvent mouseEvent) {
        if (isSelected) {
            selectButton(null);
            colourPopupBox.setVisible(false);
        } else {
            selectButton(this);
            colourPopupBox.setVisible(true);
        }
    }
}
