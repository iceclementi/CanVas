package seedu.canvas.component.canvas.utility.tool;

import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import seedu.canvas.component.canvas.CanvasMode;
import seedu.canvas.component.canvas.TheCanvas;

public class TextButton extends ToolButton {

    public TextButton(String backgroundPath) {
        super(backgroundPath);

        initialiseEvents();
    }

    private void initialiseEvents() {
        setOnMouseReleased(this::onClick);

        TheCanvas.getInstance().getCanvasModeProperty().addListener(observable -> {
            if (TheCanvas.getInstance().getCanvasMode() == CanvasMode.TEXT) {
                selectButton(this);
                TheCanvas.getInstance().setCursor(Cursor.CROSSHAIR);
            }
        });
    }

    private void onClick(MouseEvent mouseEvent) {
        TheCanvas.getInstance().changeMode(CanvasMode.TEXT);
    }
}
