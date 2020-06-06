package seedu.canvas.component.canvas;

import javafx.scene.effect.ColorAdjust;
import javafx.scene.input.MouseEvent;

public class ModelButton extends ToolButton {

    /**
     * Constructor for the help button in the menu.
     *
     * @param backgroundPath
     *  The path to the background of the help button
     */
    public ModelButton(String backgroundPath) {
        super(backgroundPath);

        initialiseEvents();
    }

    private void initialiseEvents() {
        setOnMouseReleased(this::onClick);
    }

    private void onClick(MouseEvent mouseEvent) {
        isSelected = !isSelected;

        if (isSelected) {
            setEffect(new ColorAdjust(0, 0, -0.2, 0));
            TheCanvas.getInstance().activateModelMode();
        } else {
            setEffect(null);
            TheCanvas.getInstance().deactivateMode();
        }
    }
}
