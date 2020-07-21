package seedu.canvas.component.canvas.text;

import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import seedu.canvas.component.canvas.CanvasGrid;
import seedu.canvas.component.canvas.Direction;
import seedu.canvas.component.canvas.TheCanvas;
import seedu.canvas.component.canvas.unit.CanvasHandle;
import seedu.canvas.util.CanvasMath;

public class TextBoxResizeHandle extends CanvasHandle {

    private TheCanvas canvas = TheCanvas.getInstance();
    private TextBox textBox;
    private Point2D previousAnchorPoint = null;
    private Point2D oppositeAnchorPoint = null;

    private TextBoxWrapper wrapper;

    public TextBoxResizeHandle(TextBoxWrapper wrapper, Direction location) {
        super(location);
        this.wrapper = wrapper;

        textBox = wrapper.getTextBox();

        initialiseStyle();
        initialiseEvents();
    }

    private void initialiseStyle() {
        unfocus();

        switch (location) {
        case NORTHWEST:
            centerXProperty().bind(wrapper.xProperty());
            centerYProperty().bind(wrapper.yProperty());
            setCursor(Cursor.NW_RESIZE);
            break;
        case NORTHEAST:
            centerXProperty().bind(wrapper.xProperty().add(wrapper.widthProperty()));
            centerYProperty().bind(wrapper.yProperty());
            setCursor(Cursor.NE_RESIZE);
            break;
        case SOUTHWEST:
            centerXProperty().bind(wrapper.xProperty());
            centerYProperty().bind(wrapper.yProperty().add(wrapper.heightProperty()));
            setCursor(Cursor.SW_RESIZE);
            break;
        case SOUTHEAST:
            centerXProperty().bind(wrapper.xProperty().add(wrapper.widthProperty()));
            centerYProperty().bind(wrapper.yProperty().add(wrapper.heightProperty()));
            setCursor(Cursor.SE_RESIZE);
            break;
        default:
            System.out.println("TextBoxResizeHandle: Invalid location!");
            break;
        }
    }

    private void initialiseEvents() {
        addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent -> {
            if (mouseEvent.isPrimaryButtonDown()) {
                mouseLocation = new Point2D(mouseEvent.getSceneX(), mouseEvent.getSceneY());
                previousAnchorPoint = new Point2D(getCenterX(), getCenterY());
                oppositeAnchorPoint = findOppositeAnchorPoint();

                wrapper.getTextBox().interactSingle();

                // wrapper.getTextBox().requestFocus();
                requestFocus();
                mouseEvent.consume();
            }
        });

        setOnMouseReleased(mouseEvent -> {
            wrapper.getTextBox().focusSingle();

            mouseLocation = null;
            previousAnchorPoint = null;

            oppositeAnchorPoint = null;
        });

        switch (location) {
        case NORTHWEST:
            setOnMouseDragged(this::onDragNW);
            break;
        case NORTHEAST:
            setOnMouseDragged(this::onDragNE);
            break;
        case SOUTHWEST:
            setOnMouseDragged(this::onDragSW);
            break;
        case SOUTHEAST:
            setOnMouseDragged(this::onDragSE);
            break;
        default:
            System.out.println("TextBoxResizeHandle: Invalid location!");
            break;
        }
    }

    private void onDragNW(MouseEvent mouseEvent) {
        if (mouseLocation == null || previousAnchorPoint == null) {
            return;
        }

        double deltaX = canvas.toScale(mouseEvent.getSceneX() - mouseLocation.getX());
        double deltaY = canvas.toScale(mouseEvent.getSceneY() - mouseLocation.getY());
        updateWestX(deltaX);
        updateNorthY(deltaY);
    }

    private void onDragNE(MouseEvent mouseEvent) {
        if (mouseLocation == null || previousAnchorPoint == null) {
            return;
        }

        double deltaX = canvas.toScale(mouseEvent.getSceneX() - mouseLocation.getX());
        double deltaY = canvas.toScale(mouseEvent.getSceneY() - mouseLocation.getY());
        updateEastX(deltaX);
        updateNorthY(deltaY);
    }

    private void onDragSW(MouseEvent mouseEvent) {
        if (mouseLocation == null || previousAnchorPoint == null) {
            return;
        }

        double deltaX = canvas.toScale(mouseEvent.getSceneX() - mouseLocation.getX());
        double deltaY = canvas.toScale(mouseEvent.getSceneY() - mouseLocation.getY());
        updateWestX(deltaX);
        updateSouthY(deltaY);
    }

    private void onDragSE(MouseEvent mouseEvent) {
        if (mouseLocation == null || previousAnchorPoint == null) {
            return;
        }

        double deltaX = canvas.toScale(mouseEvent.getSceneX() - mouseLocation.getX());
        double deltaY = canvas.toScale(mouseEvent.getSceneY() - mouseLocation.getY());
        updateEastX(deltaX);
        updateSouthY(deltaY);
    }

    private void updateWestX(double deltaX) {
        double newX = CanvasMath.clamp(
                previousAnchorPoint.getX() + deltaX,
                0, textBox.getLayoutX() + textBox.getWidth() - TextBox.MIN_WIDTH);

        double newWidth = oppositeAnchorPoint.getX() - newX;

        textBox.setLayoutX(newX);
        textBox.setPrefWidth(newWidth);
    }

    private void updateEastX(double deltaX) {
        double newWidth = CanvasMath.clamp(
                previousAnchorPoint.getX() - textBox.getLayoutX() + deltaX,
                TextBox.MIN_WIDTH, CanvasGrid.WIDTH - textBox.getLayoutX());

        textBox.setPrefWidth(newWidth);
    }

    private void updateNorthY(double deltaY) {
        double newY = CanvasMath.clamp(
                previousAnchorPoint.getY() + deltaY,
                0, textBox.getLayoutY() + textBox.getHeight() - TextBox.MIN_HEIGHT);

        double newHeight = oppositeAnchorPoint.getY() - newY;

        textBox.setLayoutY(newY);
        textBox.setPrefHeight(newHeight);
    }

    private void updateSouthY(double deltaY) {
        double newHeight = CanvasMath.clamp(
                previousAnchorPoint.getY() - textBox.getLayoutY() + deltaY,
                TextBox.MIN_HEIGHT, CanvasGrid.HEIGHT - textBox.getLayoutY());

        textBox.setPrefHeight(newHeight);
    }

    private Point2D findOppositeAnchorPoint() {
        switch (location) {
        case NORTHWEST:
            return new Point2D(textBox.getLayoutX() + textBox.getWidth(), textBox.getLayoutY() + textBox.getHeight());
        case NORTHEAST:
            return new Point2D(textBox.getLayoutX(), textBox.getLayoutY() + textBox.getHeight());
        case SOUTHWEST:
            return new Point2D(textBox.getLayoutX() + textBox.getWidth(), textBox.getLayoutY());
        case SOUTHEAST:
            return new Point2D(textBox.getLayoutX(), textBox.getLayoutY());
        default:
            System.out.println("TextBoxResizeHandle: Invalid location!");
            return null;
        }
    }
}
