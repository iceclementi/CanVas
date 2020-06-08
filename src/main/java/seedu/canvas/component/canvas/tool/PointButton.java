package seedu.canvas.component.canvas.tool;

import javafx.scene.input.MouseEvent;
import seedu.canvas.component.canvas.CanvasMode;
import seedu.canvas.component.canvas.TheCanvas;

public class PointButton extends ToolButton {

    /**
     * Constructor for the point tool button.
     *
     * @param backgroundPath
     *  The path to the background of the point tool button
     */
    public PointButton(String backgroundPath) {
        super(backgroundPath);

        selectButton(this);
        TheCanvas.getInstance().changeMode(CanvasMode.POINT);
        initialiseEvents();
    }

    private void initialiseEvents() {
        setOnMouseReleased(this::onClick);
    }

    private void onClick(MouseEvent mouseEvent) {
        selectButton(this);
        TheCanvas.getInstance().changeMode(CanvasMode.POINT);
    }
}
