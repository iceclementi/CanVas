package seedu.canvas.component.canvas;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import seedu.canvas.component.canvas.unit.RectangleUnit;
import seedu.canvas.storage.FilePath;
import seedu.canvas.util.ComponentUtil;

public class TheCanvas extends Pane {

    private DoubleProperty canvasScale = new SimpleDoubleProperty(1.0d);

    private static TheCanvas canvas = null;

    private TheCanvas() {
        super();
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

    public void initialise() {
        initialiseStyle();
        initialiseEvents();
    }

    public double getCanvasScale() {
        return canvasScale.get();
    }

    public void setCanvasScale(double canvasScale) {
        this.canvasScale.set(canvasScale);
    }

    /**
     * Sets the x and y pivot points.
     *
     * @param x
     *  The x coordinate of the pivot point
     * @param y
     *  The y coordinate of the pivot point
     */
    public void setPivot(double x, double y) {
        setTranslateX(getTranslateX() - x);
        setTranslateY(getTranslateY() - y);
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

    /**
     * Shows the grid lines on the canvas.
     */
    public void showGridLines() {
        Canvas gridLines = generateGridLines();

        getChildren().add(gridLines);
        gridLines.toBack();
    }

    private void initialiseStyle() {
        ComponentUtil.setStyleClass(this, FilePath.CANVAS_STYLE_PATH, "canvas");

        scaleXProperty().bind(canvasScale);
        scaleYProperty().bind(canvasScale);
    }

    private void initialiseEvents() {
        CanvasEventManager canvasEventManager = new CanvasEventManager();

        addEventFilter(MouseEvent.MOUSE_PRESSED, canvasEventManager.getOnMousePressed());
        addEventFilter(MouseEvent.MOUSE_DRAGGED, canvasEventManager.getOnMouseDragged());
        addEventFilter(ScrollEvent.ANY, canvasEventManager.getOnScroll());
    }

    private void initialiseModelModeEvents() {
        setOnMousePressed(this::onClickModelMode);

    }

    private void onClickModelMode(MouseEvent mouseEvent) {
        if (!mouseEvent.isPrimaryButtonDown()) {
            return;
        }

        double x = mouseEvent.getX();
        double y = mouseEvent.getY();

        getChildren().add(new RectangleUnit(x, y, 50, 20));
    }

    private void resetEvents() {
        setOnMouseClicked(null);
    }

    private Canvas generateGridLines() {
        // double width = getBoundsInLocal().getWidth();
        // double height = getBoundsInLocal().getHeight();

        double width = 600;
        double height = 600;

        Canvas gridLines = new Canvas(width, height);
        gridLines.setMouseTransparent(true);

        GraphicsContext gc = gridLines.getGraphicsContext2D();

        gc.setStroke(Color.GRAY);
        gc.setLineWidth(0.5);

        double offset = 20d;
        for (double i = 0d; i <= width; i += offset) {
            gc.strokeLine(i, 0, i, height);
            gc.strokeLine(0, i, width, i);
        }

        return gridLines;
    }
}
