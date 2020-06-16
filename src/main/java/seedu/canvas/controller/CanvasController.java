package seedu.canvas.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import seedu.canvas.component.canvas.CanvasMode;
import seedu.canvas.component.canvas.utility.format.ColourButton;
import seedu.canvas.component.canvas.utility.tool.DrawButton;
import seedu.canvas.component.canvas.utility.tool.UnitShapeOptionButton;
import seedu.canvas.component.canvas.TheCanvas;
import seedu.canvas.component.canvas.utility.tool.PointButton;
import seedu.canvas.storage.FilePath;
import seedu.canvas.util.ComponentUtil;

import java.net.URL;
import java.util.ResourceBundle;

public class CanvasController implements Initializable {

    @FXML
    private Label titleLabel;

    @FXML
    private HBox toolBox;
    @FXML
    private HBox unitShapePopupBox;

    @FXML
    private HBox accessoryBox;
    @FXML
    private VBox colourPopupBox;
    @FXML
    private VBox colourTargetBox;
    @FXML
    private HBox paletteBox;

    @FXML
    private ScrollPane canvasScrollPane;

    private TheCanvas canvas = TheCanvas.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initialiseTitle();
        initialiseToolBox();
        initialiseAccessoryBox();
        initialiseCanvas();
    }

    private void initialiseTitle() {
        ComponentUtil.setStyleClass(titleLabel, FilePath.CANVAS_STYLE_PATH, "title");
    }

    private void initialiseToolBox() {
        UnitShapeOptionButton unitShapeOptionButton =
                new UnitShapeOptionButton(FilePath.CANVAS_MODEL_OPTION_BUTTON_IMAGE_PATH);

        toolBox.getChildren().addAll(
            new PointButton(FilePath.CANVAS_POINT_BUTTON_IMAGE_PATH),
            unitShapeOptionButton,
            new DrawButton(FilePath.CANVAS_DRAW_BUTTON_IMAGE_PATH)
        );

        unitShapeOptionButton.initialisePopup(unitShapePopupBox);

        toolBox.setOnMousePressed(mouseEvent -> {
            toolBox.requestFocus();
        });
    }

    private void initialiseAccessoryBox() {
        ColourButton colourButton = new ColourButton(FilePath.CANVAS_COLOUR_BUTTON_IMAGE_PATH);

        accessoryBox.getChildren().addAll(
            colourButton
        );

        colourButton.initialisePopup(colourPopupBox, colourTargetBox, paletteBox);

        accessoryBox.setOnMousePressed(mouseEvent -> {
            accessoryBox.requestFocus();
        });
    }

    private void initialiseCanvas() {
        canvasScrollPane.setContent(canvas);
        canvas.initialise();

        canvas.showGridLines();
        canvas.changeMode(CanvasMode.POINT);
    }
}
