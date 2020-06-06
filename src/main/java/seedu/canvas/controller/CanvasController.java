package seedu.canvas.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import seedu.canvas.component.canvas.DrawButton;
import seedu.canvas.component.canvas.ModelButton;
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
    private VBox canvasBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initialiseTitle();
        initialiseToolBox();
    }

    private void initialiseTitle() {
        ComponentUtil.setStyleClass(titleLabel, FilePath.CANVAS_STYLE_PATH, "title");
    }

    private void initialiseToolBox() {
        toolBox.getChildren().addAll(
            new ModelButton(FilePath.CANVAS_MODEL_BUTTON_IMAGE_PATH),
            new DrawButton(FilePath.CANVAS_DRAW_BUTTON_IMAGE_PATH)
        );
    }
}
