package seedu.canvas.component.canvas.utility.tool;

import javafx.scene.control.Button;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.input.MouseEvent;
import seedu.canvas.component.canvas.unit.UnitShape;
import seedu.canvas.storage.FilePath;
import seedu.canvas.util.ComponentUtil;

public class UnitShapeButton extends Button {

    private boolean isSelected = false;
    private static UnitShapeButton selectedShape = null;

    private UnitShapeOptionButton unitShapeOptionButton;
    private UnitShape shape;
    private String backgroundPath;

    public UnitShapeButton(UnitShapeOptionButton unitShapeOptionButton, UnitShape shape, String backgroundPath) {
        super();
        this.unitShapeOptionButton = unitShapeOptionButton;
        this.shape = shape;
        this.backgroundPath = backgroundPath;

        initialiseStyle();
        initialiseEvents();
    }

    private void initialiseStyle() {
        ComponentUtil.setBackground(this, backgroundPath);
        ComponentUtil.setStyleClass(this, FilePath.CANVAS_STYLE_PATH, "utility-button");

        if (shape == UnitShape.MODEL) {
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
        getParent().requestFocus();
    }

    private void selectButton() {
        if (selectedShape != null) {
            selectedShape.isSelected = false;
            selectedShape.reset();
        }

        isSelected = true;
        setEffect(new ColorAdjust(0, 0, -0.1, 0));

        selectedShape = this;
        unitShapeOptionButton.changeShape(shape);
    }

    private void reset() {
        setEffect(null);
    }
}
