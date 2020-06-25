package seedu.canvas.component.canvas.text;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import org.fxmisc.richtext.StyleClassedTextArea;
import seedu.canvas.component.canvas.CanvasComponent;
import seedu.canvas.component.canvas.CanvasGrid;
import seedu.canvas.component.canvas.TheCanvas;

import java.util.ArrayList;

public class TextBox extends StyleClassedTextArea implements CanvasComponent {

    private TextBoxWrapper wrapper = new TextBoxWrapper(this);

    public TextBox(double x, double y) {
        super();

        initialiseStyle();
        initialiseEvents();

        TheCanvas.getInstance().getChildren().addAll(getGroup());
        relocate(x, y);
    }

    public ArrayList<Node> getGroup() {
        ArrayList<Node> group = new ArrayList<>();
        group.add(this);
        group.addAll(wrapper.getGroup());

        return group;
    }

    public void interact() {
        toFront();
        wrapper.interact();
    }

    public void focus() {
        toFront();
        wrapper.focus();
    }

    public void unfocus() {
        wrapper.unfocus();
    }

    public void scale(double endX, double endY) {
        double newWidth = clamp(endX - getLayoutX(), CanvasGrid.OFFSET, CanvasGrid.WIDTH - getLayoutX());
        double newHeight = clamp(endY - getLayoutY(), CanvasGrid.OFFSET, CanvasGrid.HEIGHT - getLayoutY());

        setPrefSize(newWidth, newHeight);
    }

    public void move(double newX, double newY) {
        double finalNewX = clamp(newX, 0, CanvasGrid.WIDTH - getWidth());
        double finalNewY = clamp(newY, 0, CanvasGrid.HEIGHT - getHeight());

        relocate(finalNewX, finalNewY);
    }

    public void colour() {

    }

    public void colour(Color textColour) {
        
    }

    public void setDefaultSize() {
        setPrefSize(CanvasGrid.OFFSET * 5, CanvasGrid.OFFSET * 3);
    }

    private void initialiseStyle() {
        setBorder(new Border(new BorderStroke(Color.GREEN, BorderStrokeStyle.SOLID, new CornerRadii(0), new BorderWidths(2))));
        setBackground(null);
        setWrapText(true);
        setStyle("-fx-font-size: 10pt");
        setPadding(new Insets(5));

        setMinSize(0, 0);
        setPrefSize(0, 0);

        unfocus();
    }

    private void initialiseEvents() {
        TextBoxEventManager eventManager = new TextBoxEventManager();

        addEventFilter(MouseEvent.MOUSE_PRESSED, eventManager.getOnMousePressed());
        // addEventFilter(MouseEvent.MOUSE_DRAGGED, eventManager.getOnMouseDragged());
        addEventFilter(MouseEvent.MOUSE_RELEASED, eventManager.getOnMouseReleased());
    }

    private double clamp(double value, double minimum, double maximum) {
        return Math.min(Math.max(value, minimum), maximum);
    }

}
