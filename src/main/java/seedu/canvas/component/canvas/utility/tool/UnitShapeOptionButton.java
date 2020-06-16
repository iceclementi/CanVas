package seedu.canvas.component.canvas.utility.tool;

import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import seedu.canvas.component.canvas.CanvasMode;
import seedu.canvas.component.canvas.TheCanvas;
import seedu.canvas.component.canvas.unit.UnitShape;
import seedu.canvas.storage.FilePath;
import seedu.canvas.util.ComponentUtil;

public class UnitShapeOptionButton extends ToolButton {

    private TheCanvas canvas = TheCanvas.getInstance();

    private UnitShape shape = null;
    private HBox unitShapePopupBox;
    private boolean isShowPopup = false;

    /**
     * Constructor for the model tool button.
     *
     * @param backgroundPath
     *  The path to the background of the model tool button
     */
    public UnitShapeOptionButton(String backgroundPath) {
        super(backgroundPath);

        initialiseEvents();
    }

    public void initialisePopup(HBox unitShapePopupBox) {
        this.unitShapePopupBox = unitShapePopupBox;

        ComponentUtil.setStyleClass(unitShapePopupBox, FilePath.CANVAS_STYLE_PATH, "popup");

        // Popup box position
        unitShapePopupBox.setTranslateX(175);
        unitShapePopupBox.setTranslateY(35);

        unitShapePopupBox.getChildren().addAll(
            new UnitShapeButton(this, UnitShape.MODEL, FilePath.CANVAS_MODEL_BUTTON_IMAGE_PATH),
            new UnitShapeButton(this, UnitShape.LINE, FilePath.CANVAS_LINE_BUTTON_IMAGE_PATH)
        );
    }

    public void changeShape(UnitShape shape) {
        this.shape = shape;
        canvas.changeUnitShape(shape);

        switch (shape) {
        case MODEL:
            ComponentUtil.setBackground(this, FilePath.CANVAS_MODEL_OPTION_BUTTON_IMAGE_PATH);
            break;
        case LINE:
            ComponentUtil.setBackground(this, FilePath.CANVAS_LINE_OPTION_BUTTON_IMAGE_PATH);
            break;
        default:
            System.out.println("UnitShapeOptionButton: Invalid shape!!");
            break;
        }
    }

    private void initialiseEvents() {
        setOnMouseReleased(this::onClick);

        focusedProperty().addListener(observable -> {
            if (getScene().getFocusOwner() instanceof UnitShapeButton) {
                requestFocus();
                return;
            }

            if (!isFocused()) {
                hidePopup();
            }
        });
    }

    private void onClick(MouseEvent mouseEvent) {
        selectButton(this);

        if (isShowPopup) {
            hidePopup();
        } else {
            showPopup();
        }

        TheCanvas.getInstance().changeMode(CanvasMode.SHAPE);
        TheCanvas.getInstance().setCursor(Cursor.CROSSHAIR);
    }

    private void showPopup() {
        unitShapePopupBox.setVisible(true);
        isShowPopup = true;
    }

    private void hidePopup() {
        unitShapePopupBox.setVisible(false);
        isShowPopup = false;
    }
}
