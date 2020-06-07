package seedu.canvas.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import seedu.canvas.component.canvas.DrawButton;
import seedu.canvas.component.canvas.ModelButton;
import seedu.canvas.component.canvas.TheCanvas;
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
    private ScrollPane canvasScrollPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initialiseTitle();
        initialiseToolBox();
        initialiseCanvas();
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

    private void initialiseCanvas() {
        canvasScrollPane.setContent(TheCanvas.getInstance());
    }
}
