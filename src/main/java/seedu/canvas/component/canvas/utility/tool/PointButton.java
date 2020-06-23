package seedu.canvas.component.canvas.utility.tool;

import javafx.scene.Cursor;
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
        initialiseEvents();
    }

    private void initialiseEvents() {
        setOnMouseReleased(this::onClick);

        TheCanvas.getInstance().getCanvasModeProperty().addListener(observable -> {
            if (TheCanvas.getInstance().getCanvasMode() == CanvasMode.POINT) {
                selectButton(this);
                TheCanvas.getInstance().setCursor(Cursor.DEFAULT);
            }
        });
    }

    private void onClick(MouseEvent mouseEvent) {
        TheCanvas.getInstance().changeMode(CanvasMode.POINT);
    }
}
