package seedu.canvas.component.menu;

import javafx.scene.control.Button;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.input.MouseEvent;
import seedu.canvas.storage.FilePath;
import seedu.canvas.util.ComponentUtil;

public abstract class MenuButton extends Button {

    private String backgroundPath;

    /**
     * Constructor for a general menu button.
     *
     * @param backgroundPath
     *  The path to the background image of the button
     */
    public MenuButton(String backgroundPath) {
        super();
        this.backgroundPath = backgroundPath;

        initialiseStyle();
        initialiseEvents();
    }

    private void initialiseStyle() {
        ComponentUtil.setBackground(this, backgroundPath);
        ComponentUtil.setStyleClass(this, FilePath.MENU_STYLE_PATH, "custom-menu-button");
    }

    private void initialiseEvents() {
        setOnMouseEntered(this::onHover);
        setOnMouseExited(this::onUnhover);
        setOnMousePressed(this::onPress);
        setOnMouseReleased(this::onRelease);
    }

    private void onHover(MouseEvent mouseEvent) {
        setEffect(new ColorAdjust(0.05, 0.5, 0, 0));
    }

    private void onUnhover(MouseEvent mouseEvent) {
        reset();
    }

    private void onPress(MouseEvent mouseEvent) {
        setEffect(new ColorAdjust(0.05, 0.5, -0.25, 0));
    }

    private void onRelease(MouseEvent mouseEvent) {
        reset();
    }

    protected void reset() {
        setEffect(null);
    }
}
