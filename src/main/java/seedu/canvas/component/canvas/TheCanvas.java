package seedu.canvas.component.canvas;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class TheCanvas extends Canvas {

    private static TheCanvas canvas = null;

    private GraphicsContext gc;

    private TheCanvas() {
        super(600, 600);

        gc = getGraphicsContext2D();
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

    private void initialiseModelModeEvents() {
        setOnMouseClicked(this::onClickModelMode);
    }

    private void onClickModelMode(MouseEvent mouseEvent) {
        double x = mouseEvent.getX();
        double y = mouseEvent.getY();

        gc.setStroke(Color.MIDNIGHTBLUE);
        gc.setLineWidth(2);

        gc.strokeRect(x, y, 50, 20);
    }

    private void resetEvents() {
        setOnMouseClicked(null);
    }
}
