package seedu.canvas.component.canvas.utility.tool;

import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import seedu.canvas.component.canvas.CanvasMode;
import seedu.canvas.component.canvas.TheCanvas;

public class ModelButton extends ToolButton {

    /**
     * Constructor for the model tool button.
     *
     * @param backgroundPath
     *  The path to the background of the model tool button
     */
    public ModelButton(String backgroundPath) {
        super(backgroundPath);

        initialiseEvents();
    }

    private void initialiseEvents() {
        setOnMouseReleased(this::onClick);
    }

    private void onClick(MouseEvent mouseEvent) {
        selectButton(this);
        TheCanvas.getInstance().changeMode(CanvasMode.MODEL);
        TheCanvas.getInstance().setCursor(Cursor.CROSSHAIR);
    }
}
