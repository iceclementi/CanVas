package seedu.canvas.component.canvas.text;

import javafx.geometry.Insets;
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

public class TextBox extends StyleClassedTextArea implements CanvasComponent {

    private final static double OFFSET = CanvasGrid.OFFSET;

    public TextBox(double x, double y) {
        super();

        initialiseStyle();
        initialiseEvents();

        TheCanvas.getInstance().getChildren().add(this);
        relocate(x, y);
    }

    public void interact() {
        toFront();
    }

    public void focus() {
        toFront();
    }

    public void unfocus() {

    }

    public void scale(double endX, double endY) {
        double newWidth = clamp(endX - getLayoutX(), OFFSET, CanvasGrid.WIDTH - getLayoutX());
        double newHeight = clamp(endY - getLayoutY(), OFFSET, CanvasGrid.HEIGHT - getLayoutY());

        setPrefSize(newWidth, newHeight);
    }

    public void move(double deltaX, double deltaY) {

    }

    public void colour() {

    }

    public void colour(Color textColour) {
        
    }

    public void setDefaultSize() {
        setPrefSize(OFFSET * 5, OFFSET * 3);
    }

    private void initialiseStyle() {
        setBorder(new Border(new BorderStroke(Color.GREEN, BorderStrokeStyle.SOLID, new CornerRadii(0), new BorderWidths(2))));
        setBackground(null);
        setWrapText(true);
        setStyle("-fx-font-size: 10pt");
        setPadding(new Insets(5));

        setMinSize(0, 0);
        setPrefSize(0, 0);
    }

    private void initialiseEvents() {

    }

    private double clamp(double value, double minimum, double maximum) {
        return Math.min(Math.max(value, minimum), maximum);
    }

}
