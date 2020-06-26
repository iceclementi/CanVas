package seedu.canvas.component.canvas;

import javafx.scene.Node;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public interface CanvasNode {

    ArrayList<Node> getGroup();

    void interact();

    void focus();

    void unfocus();

    void colourLine(Color lineColour);

    void colourFill(Color fillColour);
}
