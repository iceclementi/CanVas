package seedu.canvas.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import seedu.canvas.component.canvas.CanvasMode;
import seedu.canvas.component.canvas.utility.ColourButton;
import seedu.canvas.component.canvas.utility.DrawButton;
import seedu.canvas.component.canvas.utility.ModelButton;
import seedu.canvas.component.canvas.TheCanvas;
import seedu.canvas.component.canvas.utility.PointButton;
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
    private HBox accessoryBox;
    @FXML
    private VBox colourPopupBox;

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
        toolBox.getChildren().addAll(
            new PointButton(FilePath.CANVAS_POINT_BUTTON_IMAGE_PATH),
            new ModelButton(FilePath.CANVAS_MODEL_BUTTON_IMAGE_PATH),
            new DrawButton(FilePath.CANVAS_DRAW_BUTTON_IMAGE_PATH)
        );
    }

    private void initialiseAccessoryBox() {
        accessoryBox.getChildren().addAll(
            new ColourButton(FilePath.CANVAS_COLOUR_BUTTON_IMAGE_PATH, colourPopupBox)
        );
    }

    private void initialiseCanvas() {
        canvas.initialise();
        canvasScrollPane.setContent(canvas);
        canvas.showGridLines();
        canvas.changeMode(CanvasMode.POINT);
    }
}
