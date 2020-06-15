package seedu.canvas.component.canvas.utility.format;

import javafx.scene.control.Button;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.input.MouseEvent;
import seedu.canvas.storage.FilePath;
import seedu.canvas.util.ComponentUtil;

public class ColourTargetButton extends Button {

    private boolean isSelected = false;
    private static ColourTargetButton selectedTarget = null;

    private Palette palette;
    private ColourTarget target;
    private String backgroundPath;

    /**
     * Constructor for a colour target button.
     *
     * @param backgroundPath
     *  The path to the background image of the button
     */
    public ColourTargetButton(Palette palette, ColourTarget target, String backgroundPath) {
        super();
        this.palette = palette;
        this.target = target;
        this.backgroundPath = backgroundPath;

        initialiseStyle();
        initialiseEvents();
    }

    private void initialiseStyle() {
        ComponentUtil.setBackground(this, backgroundPath);
        ComponentUtil.setStyleClass(this, FilePath.CANVAS_STYLE_PATH, "colour-target-button");

        if (target == ColourTarget.LINE) {
            selectButton();
        }
    }

    private void initialiseEvents() {
        setOnMouseEntered(this::onHover);
        setOnMouseExited(this::onUnhover);
        setOnMousePressed(this::onPress);
        setOnMouseReleased(this::onRelease);
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

    private void onRelease(MouseEvent mouseEvent) {
        selectButton();
    }

    private void selectButton() {
        if (selectedTarget != null) {
            selectedTarget.isSelected = false;
            selectedTarget.reset();
        }

        isSelected = true;
        setEffect(new ColorAdjust(0, 0, -0.1, 0));

        selectedTarget = this;
        palette.changeTarget(target);
    }

    private void reset() {
        setEffect(null);
    }
}
