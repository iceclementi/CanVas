package seedu.canvas.component.canvas.text;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.IndexRange;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import org.fxmisc.richtext.StyleClassedTextArea;
import seedu.canvas.component.canvas.CanvasNode;
import seedu.canvas.component.canvas.CanvasGrid;
import seedu.canvas.component.canvas.TheCanvas;
import seedu.canvas.storage.FilePath;
import seedu.canvas.util.CanvasMath;
import seedu.canvas.util.ComponentUtil;

import java.util.ArrayList;
import java.util.HashSet;

public class TextBox extends StyleClassedTextArea implements CanvasNode {

    public static final double MIN_WIDTH = 40d;
    public static final double MIN_HEIGHT = 30d;

    private TheCanvas canvas = TheCanvas.getInstance();
    private TextBoxWrapper wrapper = new TextBoxWrapper(this);

    private ArrayList<HashSet<String>> characterStyles = new ArrayList<>();
    private final HashSet<String> defaultStyle = new HashSet<>() {{
        add("colour-black");
    }};
    private boolean ignore = false;

    public TextBox(double x, double y) {
        super();

        initialiseStyle();
        initialiseEvents();

        canvas.addNode(this);
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
        canvas.interactSingle(this);
        wrapper.interact();
    }

    public void focus() {
        toFront();
        canvas.focusSingle(this);
        wrapper.focus();
    }

    public void unfocus() {
        wrapper.unfocus();
    }

    public void scale(double endX, double endY) {
        double newWidth = CanvasMath.clamp(endX - getLayoutX(), 0, CanvasGrid.WIDTH - getLayoutX());
        double newHeight = CanvasMath.clamp(endY - getLayoutY(), 0, CanvasGrid.HEIGHT - getLayoutY());

        setPrefSize(newWidth, newHeight);
    }

    public void move(double newX, double newY) {
        double finalNewX = CanvasMath.clamp(newX, 0, CanvasGrid.WIDTH - getWidth());
        double finalNewY = CanvasMath.clamp(newY, 0, CanvasGrid.HEIGHT - getHeight());

        relocate(finalNewX, finalNewY);
    }

    public void colourLine(Color lineColour) {
        setBorder(new Border(new BorderStroke(lineColour, BorderStrokeStyle.SOLID, new CornerRadii(0), new BorderWidths(2))));
    }

    public void colourFill(Color colour) {
        setBackground(new Background(new BackgroundFill(colour, CornerRadii.EMPTY, Insets.EMPTY)));
    }

    public void colourText(Color textColour) {
        
    }

    public void setDefaultSize() {
        setPrefSize(CanvasGrid.OFFSET * 4, CanvasGrid.OFFSET * 2);
    }

    private void initialiseStyle() {
        ComponentUtil.setStyleClass(this, FilePath.TEXT_STYLE_PATH, "default");

        setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(0), new BorderWidths(2))));
        setBackground(null);
        setWrapText(true);
        setStyle("-fx-font-size: 10pt;");
        setPadding(new Insets(2));

        setMinSize(0, 0);
        setPrefSize(0, 0);

        unfocus();
    }

    private void initialiseEvents() {
        TextBoxEventManager eventManager = new TextBoxEventManager();

        addEventFilter(MouseEvent.MOUSE_PRESSED, eventManager.getOnMousePressed());
        // addEventFilter(MouseEvent.MOUSE_DRAGGED, eventManager.getOnMouseDragged());
        addEventFilter(MouseEvent.MOUSE_RELEASED, eventManager.getOnMouseReleased());

        addTextListener();
        addKeyShortcutEvent();
    }

    private void addTextListener() {
        textProperty().addListener((observable, oldText, newText) -> {
            int caretPosition = getCaretPosition();

            int afterLength = newText.length() - caretPosition;

            int oldBeforeLength = oldText.length() - afterLength;
            int newBeforeLength = newText.length() - afterLength;

            updateCharacterStyles(oldText.substring(0, oldBeforeLength), newText.substring(0, newBeforeLength));
        });
    }

    private void addKeyShortcutEvent() {
        setOnKeyPressed(keyEvent -> {
            if (keyEvent.isControlDown()) {
                IndexRange range = getSelection();

                switch (keyEvent.getCode()) {
                case B:
                    updateTextStyles(range.getStart(), range.getEnd(), "bold");
                    break;
                case I:
                    updateTextStyles(range.getStart(), range.getEnd(), "italic");
                    break;
                case U:
                    updateTextStyles(range.getStart(), range.getEnd(), "underline");
                    break;
                case V:
                    reapplyStyle();
                    break;
                default:
                    break;
                }
            }
        });
    }

    private void updateCharacterStyles(String oldText, String newText) {
        if (ignore) {
            return;
        }

        int index = 0;

        if (newText.length() >= oldText.length()) {
            for (; index < oldText.length(); ++index) {
                if (oldText.charAt(index) != newText.charAt(index)) {
                    break;
                }
            }

            HashSet<String> previousStyle = index == 0 ? defaultStyle : characterStyles.get(index - 1);

            // System.out.println(String.format("Change from %s", index));

            if (oldText.length() > index) {
                characterStyles.subList(index, oldText.length()).clear();
            }

            for (int i = index; i < newText.length(); ++i) {
                characterStyles.add(i, new HashSet<>(previousStyle));
            }

            // System.out.println(String.format("Array Length is %s", characterStyles.size()));
        } else {
            for (; index < newText.length(); ++index) {
                if (oldText.charAt(index) != newText.charAt(index)) {
                    break;
                }
            }

            HashSet<String> previousStyle = index == 0 ? defaultStyle : characterStyles.get(index - 1);

            if (oldText.length() > index) {
                characterStyles.subList(index, oldText.length()).clear();
            }

            for (int i = index; i < newText.length(); ++i) {
                characterStyles.add(i, new HashSet<>(previousStyle));
            }
        }
    }

    private void updateTextStyles(int startIndex, int endIndex, String style) {
        boolean isStyleAbsent = false;

        for (int i = startIndex; i < endIndex; ++i) {
            if (!characterStyles.get(i).contains(style)) {
                isStyleAbsent = true;
                break;
            }
        }

        if (isStyleAbsent) {
            for (int i = startIndex; i < endIndex; ++i) {
                characterStyles.get(i).add(style);
                setStyle(i, i + 1, characterStyles.get(i));
                forceStyleChange(startIndex, endIndex);

                // System.out.println(String.format("Setting character at %s to %s to %s", i, i + 1, characterStyles.get(i)));
            }
        } else {
            for (int i = startIndex; i < endIndex; ++i) {
                characterStyles.get(i).remove(style);
                setStyle(i, i + 1, characterStyles.get(i));
                forceStyleChange(startIndex, endIndex);
            }
        }
    }

    private void reapplyStyle() {
        for (int i = 0; i < getText().length(); ++i) {
            setStyle(i, i + 1, characterStyles.get(i));
        }
    }

    private void forceStyleChange(int startIndex, int endIndex) {
        ignore = true;
        appendText("-");
        deleteText(getText().length() - 1, getText().length());
        ignore = false;
        selectRange(startIndex, endIndex);
    }
}
