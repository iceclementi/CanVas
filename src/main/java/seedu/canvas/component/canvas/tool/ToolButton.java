package seedu.canvas.component.canvas.tool;

import javafx.scene.control.Button;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.input.MouseEvent;
import seedu.canvas.storage.FilePath;
import seedu.canvas.util.ComponentUtil;

public abstract class ToolButton extends Button {

    protected boolean isSelected = false;
    protected static ToolButton selectedButton = null;

    private String backgroundPath;

    /**
     * Constructor for a general menu button.
     *
     * @param backgroundPath
     *  The path to the background image of the button
     */
    public ToolButton(String backgroundPath) {
        super();
        this.backgroundPath = backgroundPath;

        initialiseStyle();
        initialiseEvents();
    }

    private void initialiseStyle() {
        ComponentUtil.setBackground(this, backgroundPath);
        ComponentUtil.setStyleClass(this, FilePath.CANVAS_STYLE_PATH, "tool-button");
    }

    private void initialiseEvents() {
        setOnMouseEntered(this::onHover);
        setOnMouseExited(this::onUnhover);
        setOnMousePressed(this::onPress);
    }

    private void onHover(MouseEvent mouseEvent) {
        if (!isSelected) {
            setEffect(new ColorAdjust(0, 0, -0.05, 0));
        }
    }

    private void onUnhover(MouseEvent mouseEvent) {
        if (!isSelected) {
            reset();
        }
    }

    private void onPress(MouseEvent mouseEvent) {
        setEffect(new ColorAdjust(0, 0, -0.2, 0));
    }


    protected void reset() {
        setEffect(null);
    }

    protected void selectButton(ToolButton toSelect) {
        if (selectedButton != null) {
            selectedButton.isSelected = false;
            selectedButton.reset();
        }

        selectedButton = toSelect;
        toSelect.isSelected = true;
        toSelect.setEffect(new ColorAdjust(0, 0, -0.1, 0));
    }
}
