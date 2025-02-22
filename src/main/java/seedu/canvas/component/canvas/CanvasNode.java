package seedu.canvas.component.canvas;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import seedu.canvas.component.canvas.unit.LineUnit;

import java.util.ArrayList;

public interface CanvasNode {

    ArrayList<Node> getGroup();

    double getCanvasStartX();

    double getCanvasStartY();

    double getCanvasEndX();

    double getCanvasEndY();

    void interactSingle();

    void focusSingle();

    void unfocus();

    void interactMultiple();

    void focusMultiple();

    void colourLine(Color lineColour);

    void colourFill(Color fillColour);

    void move(double newX, double newY);

    CanvasNode copy();

    void dragCopy(double mouseLocationX, double mouseLocationY, DragData dragData);
}
