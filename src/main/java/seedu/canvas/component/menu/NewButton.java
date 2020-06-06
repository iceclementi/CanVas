package seedu.canvas.component.menu;

import javafx.scene.input.MouseEvent;
import seedu.canvas.storage.FilePath;
import seedu.canvas.util.SceneUtil;

public class NewButton extends MenuButton {

    /**
     * Constructor for the new button in the menu.
     *
     * @param backgroundPath
     *  The path to the background of the new button
     */
    public NewButton(String backgroundPath) {
        super(backgroundPath);

        initialiseEvents();
    }

    private void initialiseEvents() {
        setOnMouseClicked(this::onClick);
    }

    private void onClick(MouseEvent mouseEvent) {
        SceneUtil.changeScene(this, FilePath.CANVAS_SCENE_PATH);
    }
}
