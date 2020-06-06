package seedu.canvas.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import seedu.canvas.component.menu.HelpButton;
import seedu.canvas.component.menu.NewButton;
import seedu.canvas.component.menu.OpenButton;
import seedu.canvas.storage.FilePath;
import seedu.canvas.util.ComponentUtil;

import java.net.URL;
import java.util.ResourceBundle;

public class MenuController implements Initializable {

    @FXML
    private VBox menuParentBox;

    @FXML
    private VBox logoBox;
    @FXML
    private Label versionLabel;

    @FXML
    private HBox menuBox;

    private static final String VERSION = "Version 0.1";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initialiseLogo();
        initialiseMenuButtons();
    }

    private void initialiseLogo() {
        ComponentUtil.setBackground(logoBox, FilePath.CANVAS_LOGO_IMAGE_PATH);

        ComponentUtil.setStyleClass(versionLabel, FilePath.MENU_STYLE_PATH, "version");
        versionLabel.setText(VERSION);
    }

    private void initialiseMenuButtons() {
        menuBox.getChildren().addAll(
                new NewButton(FilePath.MENU_NEW_BUTTON_IMAGE_PATH),
                new OpenButton(FilePath.MENU_OPEN_BUTTON_IMAGE_PATH),
                new HelpButton(FilePath.MENU_HELP_BUTTON_IMAGE_PATH)
        );
    }
}
