package seedu.canvas.component.canvas;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import seedu.canvas.component.canvas.unit.ModelUnit;
import seedu.canvas.storage.FilePath;
import seedu.canvas.util.ComponentUtil;

import java.util.ArrayList;

public class TheCanvas extends Pane {

    public static final Color DEFAULT_LINE_COLOUR = Color.MIDNIGHTBLUE;
    public static final Color DEFAULT_FILL_COLOUR = Color.TRANSPARENT;

    private DoubleProperty canvasScale = new SimpleDoubleProperty(1.0d);
    private CanvasMode canvasMode = CanvasMode.POINT;

    private Color lineColour = null;
    private Color fillColour = null;

    private ModelUnit focussedUnit = null;
    private ArrayList<ModelUnit> modelUnits = new ArrayList<>();

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


        // // Delete unit
        // canvasScrollPane.getScene().setOnKeyPressed(keyEvent -> {
        //     if (keyEvent.getCode() == KeyCode.DELETE || keyEvent.getCode() == KeyCode.BACK_SPACE) {
        //         if (focussedUnit != null) {
        //             getChildren().removeAll(focussedUnit.getUnitGroup());
        //         }
        //     }
        // });
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

    public CanvasMode getCanvasMode() {
        return canvasMode;
    }

    /**
     * Changes the current canvas mode to the specified mode.
     *
     * @param canvasMode
     *  The canvas mode to be changed
     */
    public void changeMode(CanvasMode canvasMode) {
        this.canvasMode = canvasMode;
    }

    public Color getLineColour() {
        return lineColour == null ? DEFAULT_LINE_COLOUR : lineColour;
    }

    public Color getFillColour() {
        return fillColour == null ? DEFAULT_FILL_COLOUR : fillColour;
    }

    public void setLineColour(Color lineColour) {
        this.lineColour = lineColour;

        if (focussedUnit != null) {
            focussedUnit.setStroke(lineColour);
        }
    }

    public void setFillColour(Color fillColour) {
        this.fillColour = fillColour;

        if (focussedUnit != null) {
            focussedUnit.setFill(fillColour);
        }
    }

    public void focusUnit(ModelUnit unit) {
        if (focussedUnit != null) {
            focussedUnit.unfocus();
        }

        focussedUnit = unit;

        if (focussedUnit != null) {
            focussedUnit.focus();
        }
    }

    public void addUnit(ModelUnit unit) {
        modelUnits.add(unit);
        this.getChildren().addAll(unit.getUnitGroup());
    }

    public void removeUnit(ModelUnit unit) {
        modelUnits.remove(unit);
        this.getChildren().removeAll(unit.getUnitGroup());
    }

    public boolean isIntersectUnit(int pointX, int pointY) {
        for (ModelUnit unit : modelUnits) {
            if (unit.isIntersect(pointX, pointY)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Shows the grid lines on the canvas.
     */
    public void showGridLines() {
        Canvas gridLines = generateGridLines();

        getChildren().add(gridLines);
        gridLines.toBack();

        new CanvasGrid().initialise();
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
        addEventFilter(MouseEvent.MOUSE_RELEASED, canvasEventManager.getOnMouseReleased());
        addEventFilter(ScrollEvent.ANY, canvasEventManager.getOnScroll());

        addDeleteUnitEvent();
    }

    private Canvas generateGridLines() {
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

    private void addDeleteUnitEvent() {
        sceneProperty().addListener(observable -> {
            if (getScene() == null) {
                return;
            }

            getScene().setOnKeyPressed(keyEvent -> {
                if (keyEvent.getCode() == KeyCode.DELETE || keyEvent.getCode() == KeyCode.BACK_SPACE) {
                    if (focussedUnit != null) {
                        getChildren().removeAll(focussedUnit.getUnitGroup());
                    }
                }
            });
        });
    }
}
