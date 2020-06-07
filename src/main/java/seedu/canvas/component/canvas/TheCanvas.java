package seedu.canvas.component.canvas;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import seedu.canvas.storage.FilePath;
import seedu.canvas.util.ComponentUtil;

public class TheCanvas extends Pane {

    private static TheCanvas canvas = null;

    private TheCanvas() {
        super();

        initialiseStyle();
    }

    /**
     * Gets the singleton instance of the canvas.
     *
     * @return
     *  The instance of the canvas.
     */
    public static TheCanvas getInstance() {
        if (canvas == null) {
            canvas = new TheCanvas();
        }

        return canvas;
    }

    /**
     * Activates the model mode.
     */
    public void activateModelMode() {
        resetEvents();
        initialiseModelModeEvents();
    }

    /**
     * Deactivates the current mode.
     */
    public void deactivateMode() {
        resetEvents();
    }

    private void initialiseStyle() {
        ComponentUtil.setStyleClass(this, FilePath.CANVAS_STYLE_PATH, "canvas");
    }


    private void initialiseModelModeEvents() {
        setOnMouseClicked(this::onClickModelMode);
    }

    private void onClickModelMode(MouseEvent mouseEvent) {
        double x = mouseEvent.getX();
        double y = mouseEvent.getY();

        getChildren().add(new RectangleUnit(x, y, 50, 20));
    }

    private void resetEvents() {
        setOnMouseClicked(null);
    }
}
