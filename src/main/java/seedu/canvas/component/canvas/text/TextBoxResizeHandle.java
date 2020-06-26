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
            break;
        case NORTHEAST:
            centerXProperty().bind(wrapper.xProperty().add(wrapper.widthProperty()));
            centerYProperty().bind(wrapper.yProperty());
            break;
        case SOUTHWEST:
            centerXProperty().bind(wrapper.xProperty());
            centerYProperty().bind(wrapper.yProperty().add(wrapper.heightProperty()));
            break;
        case SOUTHEAST:
            centerXProperty().bind(wrapper.xProperty().add(wrapper.widthProperty()));
            centerYProperty().bind(wrapper.yProperty().add(wrapper.heightProperty()));
            break;
        default:
            System.out.println("TextBoxResizeHandle: Invalid location!");
            break;
        }
    }

    private void initialiseEvents() {
        setOnMouseEntered(mouseEvent -> {
            switch (location) {
            case NORTHWEST:
                setCursor(Cursor.NW_RESIZE);
                break;
            case NORTHEAST:
                setCursor(Cursor.NE_RESIZE);
                break;
            case SOUTHWEST:
                setCursor(Cursor.SW_RESIZE);
                break;
            case SOUTHEAST:
                setCursor(Cursor.SE_RESIZE);
                break;
            default:
                setCursor(Cursor.DEFAULT);
                break;
            }
        });

        setOnMouseExited(mouseEvent -> {
            if (!isInteracting) {
                setCursor(Cursor.DEFAULT);
            }
        });

        setOnMousePressed(mouseEvent -> {
            wrapper.getTextBox().interact();

            mouseLocation = new Point2D(mouseEvent.getSceneX(), mouseEvent.getSceneY());
            previousAnchorPoint = new Point2D(getCenterX(), getCenterY());
            isInteracting = true;

            switch (location) {
            case NORTHWEST:
                oppositeAnchorPoint = new Point2D(textBox.getLayoutX() + textBox.getWidth(), textBox.getLayoutY() + textBox.getHeight());
                break;
            case NORTHEAST:
                oppositeAnchorPoint = new Point2D(textBox.getLayoutX(), textBox.getLayoutY() + textBox.getHeight());
                break;
            case SOUTHWEST:
                oppositeAnchorPoint = new Point2D(textBox.getLayoutX() + textBox.getWidth(), textBox.getLayoutY());
                break;
            case SOUTHEAST:
                oppositeAnchorPoint = new Point2D(textBox.getLayoutX(), textBox.getLayoutY());
                break;
            default:
                break;
            }
        });

        setOnMouseReleased(mouseEvent -> {
            wrapper.getTextBox().focus();

            mouseLocation = null;
            previousAnchorPoint = null;
            isInteracting = false;

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
                0, textBox.getLayoutX() + textBox.getWidth() - CanvasGrid.OFFSET);

        double newWidth = oppositeAnchorPoint.getX() - newX;

        textBox.setLayoutX(newX);
        textBox.setPrefWidth(newWidth);
    }

    private void updateEastX(double deltaX) {
        double newWidth = CanvasMath.clamp(
                previousAnchorPoint.getX() - textBox.getLayoutX() + deltaX,
                CanvasGrid.OFFSET, CanvasGrid.WIDTH - textBox.getLayoutX());

        textBox.setPrefWidth(newWidth);
    }

    private void updateNorthY(double deltaY) {
        double newY = CanvasMath.clamp(
                previousAnchorPoint.getY() + deltaY,
                0, textBox.getLayoutY() + textBox.getHeight() - CanvasGrid.OFFSET);

        double newHeight = oppositeAnchorPoint.getY() - newY;

        textBox.setLayoutY(newY);
        textBox.setPrefHeight(newHeight);
    }

    private void updateSouthY(double deltaY) {
        double newHeight = CanvasMath.clamp(
                previousAnchorPoint.getY() - textBox.getLayoutY() + deltaY,
                CanvasGrid.OFFSET, CanvasGrid.HEIGHT - textBox.getLayoutY());

        textBox.setPrefHeight(newHeight);
    }

    private Point2D findPreviousHandleLocation() {
        switch (location) {
        case NORTHWEST:
            return new Point2D(textBox.getLayoutX(), textBox.getLayoutY());
        case NORTHEAST:
            return new Point2D(textBox.getLayoutX() + textBox.getWidth(), textBox.getLayoutY());
        case SOUTHWEST:
            return new Point2D(textBox.getLayoutX(), textBox.getLayoutY() + textBox.getHeight());
        case SOUTHEAST:
            return new Point2D(textBox.getLayoutX() + textBox.getWidth(), textBox.getLayoutY() + textBox.getHeight());
        default:
            System.out.println("TextBoxResizeHandle: Invalid location!");
            return null;
        }
    }
}
