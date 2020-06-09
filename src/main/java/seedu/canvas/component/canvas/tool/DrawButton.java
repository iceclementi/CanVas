package seedu.canvas.component.canvas.tool;

import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import seedu.canvas.component.canvas.CanvasMode;
import seedu.canvas.component.canvas.TheCanvas;

public class DrawButton extends ToolButton {

    /**
     * Constructor for the draw tool button.
     *
     * @param backgroundPath
     *  The path to the background of the draw tool button
     */
    public DrawButton(String backgroundPath) {
        super(backgroundPath);

        initialiseEvents();
    }

    private void initialiseEvents() {
        setOnMouseReleased(this::onClick);
    }

    private void onClick(MouseEvent mouseEvent) {
        selectButton(this);
        TheCanvas.getInstance().changeMode(CanvasMode.DRAW);
        TheCanvas.getInstance().setCursor(Cursor.DEFAULT);
    }
}
