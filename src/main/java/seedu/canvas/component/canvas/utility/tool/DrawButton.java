package seedu.canvas.component.canvas.utility.tool;

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

        TheCanvas.getInstance().getCanvasModeProperty().addListener(observable -> {
            if (TheCanvas.getInstance().getCanvasMode() == CanvasMode.DRAW) {
                selectButton(this);
                TheCanvas.getInstance().setCursor(Cursor.DEFAULT);
            }
        });
    }

    private void onClick(MouseEvent mouseEvent) {
        TheCanvas.getInstance().changeMode(CanvasMode.DRAW);
    }
}
