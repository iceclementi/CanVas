package seedu.canvas.component.canvas.text;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import seedu.canvas.component.canvas.CanvasGrid;
import seedu.canvas.component.canvas.CanvasNode;
import seedu.canvas.component.canvas.Direction;
import seedu.canvas.component.canvas.DragData;
import seedu.canvas.component.canvas.TheCanvas;
import seedu.canvas.component.canvas.utility.format.text.TextFormatBox;
import seedu.canvas.util.CanvasMath;

import java.util.ArrayList;

public class TextBox extends Rectangle implements CanvasNode {

    public static final double MIN_WIDTH = 40d;
    public static final double MIN_HEIGHT = 30d;

    private TheCanvas canvas = TheCanvas.getInstance();
    private TextBoxWrapper wrapper = new TextBoxWrapper(this);
    private TextBoxContent content = new TextBoxContent(this);

    private Point2D pivotPoint;

    public TextBox(double x, double y, double width, double height) {
        super(x, y, width, height);

        pivotPoint = new Point2D(x, y);

        initialiseStyle();
        initialiseEvents();

        canvas.addNode(this);
    }

    public double getCanvasStartX() {
        return getX();
    }

    public double getCanvasStartY() {
        return getY();
    }

    public double getCanvasEndX() {
        return getX() + getWidth();
    }

    public double getCanvasEndY() {
        return getY() + getHeight();
    }

    public TextBoxContent getContent() {
        return content;
    }

    public ArrayList<Node> getGroup() {
        ArrayList<Node> group = new ArrayList<>();
        group.add(this);
        group.add(content);
        group.addAll(wrapper.getGroup());

        return group;
    }

    public void interactSingle() {
        canvas.interactSingle(this);
        wrapper.interactSingle();

        TextFormatBox.link(this);
    }

    public void focusSingle() {
        canvas.interactSingle(this);
        wrapper.focusSingle();
    }

    public void interactMultiple() {
        wrapper.interactMultiple();
    }

    public void focusMultiple() {
        wrapper.focusMultiple();
    }

    public void unfocus() {
        wrapper.unfocus();
        content.removeHighlight();

        TextFormatBox.unlink();
    }

    public void scale(double endX, double endY) {
        if (pivotPoint.getX() <= endX) {
            scaleEast(endX);
        } else {
            scaleWest(endX);
        }
        if (pivotPoint.getY() <= endY) {
            scaleSouth(endY);
        } else {
            scaleNorth(endY);
        }
    }

    public void move(double newX, double newY) {
        double finalNewX = CanvasMath.clamp(newX, 0, CanvasGrid.WIDTH - getWidth());
        double finalNewY = CanvasMath.clamp(newY, 0, CanvasGrid.HEIGHT - getHeight());

        setX(finalNewX);
        setY(finalNewY);
    }

    public TextBox copy() {
        TextBox copiedTextBox = new TextBox(getX(), getY(), getWidth(), getHeight());

        TextBoxContent newContent = copiedTextBox.content;
        newContent.appendText(content.getText());

        for (int i = 0; i < content.getText().length(); ++i) {
            String style = content.getStyleAtPosition(i + 1);
            newContent.setStyle(i, i + 1, style);
        }

        copiedTextBox.colour((Color) getStroke(), (Color) getFill());

        return copiedTextBox;
    }

    public void dragCopy(double mouseLocationX, double mouseLocationY, DragData dragData) {
        if (dragData.getCopiedCanvasNodes().isEmpty()) {
            return;
        }

        if (dragData.getCopyDirection() == null) {
            dragData.setCopyDirection(computeDirection(this, mouseLocationX, mouseLocationY));

            // Mouse is still within unit
            if (dragData.getCopyDirection() == null) {
                return;
            }
        }

        switch (dragData.getCopyDirection()) {
        case WEST:
            dragCopyWest(mouseLocationX, mouseLocationY, dragData);
            break;
        case EAST:
            dragCopyEast(mouseLocationX, mouseLocationY, dragData);
            break;
        case NORTH:
            dragCopyNorth(mouseLocationX, mouseLocationY, dragData);
            break;
        case SOUTH:
            dragCopySouth(mouseLocationX, mouseLocationY, dragData);
            break;
        default:
            break;
        }
    }

    public void colourLine(Color lineColour) {
        setStroke(lineColour);
    }

    public void colourFill(Color fillColour) {
        setFill(fillColour);
    }

    public void setDefaultSize() {
        setWidth(MIN_WIDTH);
        setHeight(MIN_HEIGHT);
    }

    private void initialiseStyle() {
        colour(Color.BLACK, Color.TRANSPARENT);
        setStrokeWidth(2);
    }

    private void initialiseEvents() {

    }

    private void scaleEast(double endX) {
        double newWidth = CanvasMath.clamp(endX - pivotPoint.getX(), 0, CanvasGrid.WIDTH - pivotPoint.getX());

        setWidth(newWidth);
    }

    private void scaleSouth(double endY) {
        double newHeight = CanvasMath.clamp(endY - pivotPoint.getY(), 0, CanvasGrid.HEIGHT - pivotPoint.getY());

        setHeight(newHeight);
    }

    private void scaleWest(double endX) {
        double newX = CanvasMath.clamp(endX, 0, pivotPoint.getX());
        double newWidth = CanvasMath.clamp(pivotPoint.getX() - newX, 0, pivotPoint.getX());

        setX(newX);
        setWidth(newWidth);
    }

    private void scaleNorth(double endY) {
        double newY = CanvasMath.clamp(endY, 0, pivotPoint.getY());
        double newHeight = CanvasMath.clamp(pivotPoint.getY() - newY, 0, pivotPoint.getY());

        setY(newY);
        setHeight(newHeight);
    }

    private void dragCopyWest(double mouseLocationX, double mouseLocationY, DragData dragData) {
        ArrayList<CanvasNode> copiedTextBoxes = dragData.getCopiedCanvasNodes();

        TextBox targetTextBox = (TextBox) dragData.getRecentCanvasNode();
        Direction currentCopyDirection = computeDirection(targetTextBox, mouseLocationX, mouseLocationY);

        if (currentCopyDirection == Direction.WEST) {
            addTextBox(copiedTextBoxes, targetTextBox,
                    targetTextBox.getCanvasStartX() - targetTextBox.getWidth(),
                    targetTextBox.getCanvasStartY(),
                    targetTextBox.getWidth(), targetTextBox.getHeight());
        } else if (currentCopyDirection == Direction.EAST) {
            removeTextBox(targetTextBox, dragData);
        }
    }

    private void dragCopyEast(double mouseLocationX, double mouseLocationY, DragData dragData) {
        ArrayList<CanvasNode> copiedTextBoxes = dragData.getCopiedCanvasNodes();

        TextBox targetTextBox = (TextBox) dragData.getRecentCanvasNode();
        Direction currentCopyDirection = computeDirection(targetTextBox, mouseLocationX, mouseLocationY);

        if (currentCopyDirection == Direction.EAST) {
            addTextBox(copiedTextBoxes, targetTextBox,
                    targetTextBox.getCanvasEndX(),
                    targetTextBox.getCanvasStartY(),
                    targetTextBox.getWidth(), targetTextBox.getHeight());
        } else if (currentCopyDirection == Direction.WEST) {
            removeTextBox(targetTextBox, dragData);
        }
    }

    private void dragCopyNorth(double mouseLocationX, double mouseLocationY, DragData dragData) {
        ArrayList<CanvasNode> copiedTextBoxes = dragData.getCopiedCanvasNodes();

        TextBox targetTextBox = (TextBox) dragData.getRecentCanvasNode();
        Direction currentCopyDirection = computeDirection(targetTextBox, mouseLocationX, mouseLocationY);

        if (currentCopyDirection == Direction.NORTH) {
            addTextBox(copiedTextBoxes, targetTextBox,
                    targetTextBox.getCanvasStartX(),
                    targetTextBox.getCanvasStartY() - targetTextBox.getHeight(),
                    targetTextBox.getWidth(), targetTextBox.getHeight());
        } else if (currentCopyDirection == Direction.SOUTH) {
            removeTextBox(targetTextBox, dragData);
        }
    }

    private void dragCopySouth(double mouseLocationX, double mouseLocationY, DragData dragData) {
        ArrayList<CanvasNode> copiedTextBoxes = dragData.getCopiedCanvasNodes();

        TextBox targetTextBox = (TextBox) dragData.getRecentCanvasNode();
        Direction currentCopyDirection = computeDirection(targetTextBox, mouseLocationX, mouseLocationY);

        if (currentCopyDirection == Direction.SOUTH) {
            addTextBox(copiedTextBoxes, targetTextBox,
                    targetTextBox.getCanvasStartX(),
                    targetTextBox.getCanvasStartY() + targetTextBox.getHeight(),
                    targetTextBox.getWidth(), targetTextBox.getHeight());
        } else if (currentCopyDirection == Direction.NORTH) {
            removeTextBox(targetTextBox, dragData);
        }
    }

    private void addTextBox(ArrayList<CanvasNode> copiedTextBoxes, TextBox targetTextBox,
            double newX, double newY, double newWidth, double newHeight) {

        if (canvas.isWithinCanvas(newX, newY, newX + newWidth, newY + newHeight)) {

            TextBox newTextBox = targetTextBox.copy();
            newTextBox.move(newX, newY);
            newTextBox.interactSingle();
            canvas.requestFocus();

            copiedTextBoxes.add(newTextBox);
        }
    }

    private void removeTextBox(TextBox targetTextBox, DragData dragData) {
        ArrayList<CanvasNode> copiedDrawings = dragData.getCopiedCanvasNodes();

        if (copiedDrawings.size() > 1) {
            copiedDrawings.remove(targetTextBox);
            canvas.removeNode(targetTextBox);
        }

        if (copiedDrawings.size() == 1) {
            dragData.setCopyDirection(null);
        }

        dragData.getRecentCanvasNode().interactSingle();
    }

    private static Direction computeDirection(TextBox textBox, double mouseLocationX, double mouseLocationY) {
        if (CanvasGrid.toUnit(mouseLocationX - textBox.getCanvasStartX()) < 0) {
            return Direction.WEST;
        } else if (CanvasGrid.toUnit(mouseLocationX - textBox.getCanvasEndX()) > 0) {
            return Direction.EAST;
        } else if (CanvasGrid.toUnit(mouseLocationY - textBox.getCanvasStartY()) < 0) {
            return Direction.NORTH;
        } else if (CanvasGrid.toUnit(mouseLocationY - textBox.getCanvasEndY()) > 0) {
            return Direction.SOUTH;
        } else {
            // Within the textBox
            return null;
        }
    }

    private void colour(Color lineColour, Color fillColor) {
        colourLine(lineColour);
        colourFill(fillColor);
    }
}
