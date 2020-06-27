package seedu.canvas.component.canvas;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import seedu.canvas.component.canvas.draw.DrawingCanvas;
import seedu.canvas.component.canvas.unit.UnitShape;
import seedu.canvas.storage.FilePath;
import seedu.canvas.util.ComponentUtil;

public class TheCanvas extends Pane {

    public static final Color DEFAULT_LINE_COLOUR = Color.MIDNIGHTBLUE;
    public static final Color DEFAULT_FILL_COLOUR = Color.TRANSPARENT;

    private DoubleProperty canvasScale = new SimpleDoubleProperty(1.0d);

    private CanvasMode canvasMode = CanvasMode.POINT;
    private StringProperty canvasModeProperty = new SimpleStringProperty(canvasMode.name());

    /* Unit shapes */
    private UnitShape shape = UnitShape.MODEL;

    private CanvasNode focussedNode = null;

    private Color lineColour = null;
    private Color fillColour = null;

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

        getChildren().add(CanvasGrid.getGridLines());
        CanvasGrid.getGridLines().toBack();

        getChildren().addAll(CanvasGrid.getGridPoints());

        getChildren().add(DrawingCanvas.getInstance());

        DrawingCanvas.getInstance().initialise();
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

    public StringProperty getCanvasModeProperty() {
        return canvasModeProperty;
    }

    /**
     * Changes the current canvas mode to the specified mode.
     *
     * @param canvasMode
     *  The canvas mode to be changed
     */
    public void changeMode(CanvasMode canvasMode) {
        this.canvasMode = canvasMode;
        canvasModeProperty.get();
        canvasModeProperty.set(canvasMode.name());

        if (canvasMode == CanvasMode.SHAPE) {
            showGrid();
        } else {
            hideGrid();
        }

        if (canvasMode == CanvasMode.DRAW) {
            DrawingCanvas.getInstance().startDrawing();
        } else {
            DrawingCanvas.getInstance().stopDrawing();
        }
    }

    public Color getLineColour() {
        return lineColour == null ? DEFAULT_LINE_COLOUR : lineColour;
    }

    public Color getFillColour() {
        return fillColour == null ? DEFAULT_FILL_COLOUR : fillColour;
    }

    public void setLineColour(Color lineColour) {
        this.lineColour = lineColour;

        if (focussedNode != null) {
            focussedNode.colourLine(lineColour);
        }
    }

    public void setFillColour(Color fillColour) {
        this.fillColour = fillColour;

        if (focussedNode != null) {
            focussedNode.colourFill(fillColour);
        }
    }

    public UnitShape getUnitShape() {
        return shape;
    }

    public void changeUnitShape(UnitShape shape) {
        this.shape = shape;
    }

    public void interactSingle(CanvasNode canvasNode) {
        focusNone();
        focussedNode = canvasNode;
    }

    public void focusSingle(CanvasNode canvasNode) {
        focusNone();
        focussedNode = canvasNode;
    }

    public void focusNone() {
        if (focussedNode != null) {
            focussedNode.unfocus();
            focussedNode = null;
        }
    }

    public void addNode(CanvasNode canvasNode) {
        getChildren().addAll(canvasNode.getGroup());
    }

    public void removeNode(CanvasNode canvasNode) {
        getChildren().removeAll(canvasNode.getGroup());
    }

    public double toScale(double valueToScale) {
        return valueToScale / canvasScale.get();
    }

    /**
     * Shows the grid on the canvas.
     */
    public void showGrid() {
        CanvasGrid.showGridLines();
        CanvasGrid.showGridPoints();
    }

    public void hideGrid() {
        CanvasGrid.hideGridLines();
        CanvasGrid.hideGridPoints();
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

    private void addDeleteUnitEvent() {
        sceneProperty().addListener(observable -> {
            if (getScene() == null) {
                return;
            }

            getScene().setOnKeyPressed(keyEvent -> {
                if (keyEvent.getCode() == KeyCode.DELETE || keyEvent.getCode() == KeyCode.BACK_SPACE) {
                    if (focussedNode != null) {
                        removeNode(focussedNode);
                        focussedNode = null;
                    }
                }
            });
        });
    }
}
