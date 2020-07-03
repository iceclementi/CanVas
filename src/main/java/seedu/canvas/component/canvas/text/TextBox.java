package seedu.canvas.component.canvas.text;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import org.fxmisc.richtext.InlineCssTextArea;
import seedu.canvas.component.canvas.CanvasNode;
import seedu.canvas.component.canvas.CanvasGrid;
import seedu.canvas.component.canvas.TheCanvas;
import seedu.canvas.component.canvas.utility.format.text.TextAlignmentButton;
import seedu.canvas.component.canvas.utility.format.text.TextFormatBox;
import seedu.canvas.component.canvas.utility.format.text.TextStyleButton;
import seedu.canvas.storage.FilePath;
import seedu.canvas.util.CanvasMath;
import seedu.canvas.util.ComponentUtil;

import java.util.ArrayList;
import java.util.HashSet;

public class TextBox extends InlineCssTextArea implements CanvasNode {

    public static final double MIN_WIDTH = 40d;
    public static final double MIN_HEIGHT = 30d;

    private TheCanvas canvas = TheCanvas.getInstance();
    private TextBoxWrapper wrapper = new TextBoxWrapper(this);

    private ArrayList<HashSet<String>> characterStyles = new ArrayList<>();
    private final HashSet<String> defaultStyle = new HashSet<>() {{
        add("colour-black");
    }};
    private boolean ignore = false;
    private boolean isPaste = false;

    private int currentSize = 10;

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

        TextFormatBox.link(this);
    }

    public void focus() {
        toFront();
        canvas.focusSingle(this);
        wrapper.focus();
    }

    public void unfocus() {
        wrapper.unfocus();

        TextFormatBox.unlink();

        if (!getSelectedText().isEmpty()) {
            selectRange(0, 0);
        }
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
        ComponentUtil.setStyleClass(this, FilePath.TEXT_STYLE_PATH, "default-font");
        ComponentUtil.setStyleClass(this, "align-left");

        setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(0), new BorderWidths(2))));
        setBackground(null);
        setWrapText(true);
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

        // addTextListener();
        addBasicTextListener();
        addKeyShortcutEvent();
        addCaretListener();
    }

    private void addBasicTextListener() {
        textProperty().addListener((observable, oldText, newText) -> {

            if (ignore) {
                return;
            }

            if (isPaste) {
                isPaste = false;
                return;
            }

            int caretPosition = getCaretPosition();

            int afterLength = newText.length() - caretPosition;

            int oldBeforeLength = oldText.length() - afterLength;
            int newBeforeLength = newText.length() - afterLength;

            updateText(oldText.substring(0, oldBeforeLength), newText.substring(0, newBeforeLength));
        });
    }

    private void addTextListener() {
        textProperty().addListener((observable, oldText, newText) -> {

            if (ignore) {
                return;
            }

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

                switch (keyEvent.getCode()) {
                case B:
                    applyTextStyle(TextStyle.BOLD);
                    break;
                case I:
                    applyTextStyle(TextStyle.ITALIC);
                    break;
                case U:
                    applyTextStyle(TextStyle.UNDERLINE);
                    break;
                case L:
                    applyTextAlignment("align-left");
                    break;
                case E:
                    applyTextAlignment("align-centre");
                    break;
                case R:
                    applyTextAlignment("align-right");
                    break;
                case J:
                    applyTextAlignment("align-justify");
                    break;
                case COMMA:
                    if (keyEvent.isShiftDown()) {
                        applyTextSize(--currentSize);
                    }
                    break;
                case PERIOD:
                    if (keyEvent.isShiftDown()) {
                        applyTextSize(++currentSize);
                    }
                    break;
                default:
                    break;
                }
            }
        });
    }

    private void updateText(String oldText, String newText) {
        int index = 0;
        int end = Math.min(oldText.length(), newText.length());

        for (; index < end; ++index) {
            if (oldText.charAt(index) != newText.charAt(index)) {
                break;
            }
        }

        String previousStyle = generateNextStyle(index + 1);

        for (int i = index; i < newText.length(); ++i) {
            setStyle(i, i + 1, previousStyle);
        }
    }

    private String generateNextStyle(int index) {
        String previousStyle = index == 1 ? TextStyle.DEFAULT_STYLE : getStyleAtPosition(index);

        String newStyle = previousStyle
                .replace(TextStyle.BOLD, "")
                .replace(TextStyle.ITALIC, "")
                .replace(TextStyle.UNDERLINE, "")
                .replace(TextStyle.STRIKETHROUGH, "");

        if (TextStyleButton.isApply(TextStyle.BOLD)) {
            newStyle += TextStyle.BOLD;
        }
        if (TextStyleButton.isApply(TextStyle.ITALIC)) {
            newStyle += TextStyle.ITALIC;
        }
        if (TextStyleButton.isApply(TextStyle.UNDERLINE)) {
            newStyle += TextStyle.UNDERLINE;
        }
        if (TextStyleButton.isApply(TextStyle.STRIKETHROUGH)) {
            newStyle += TextStyle.STRIKETHROUGH;
        }

        return newStyle;
    }

    public void applyTextStyle(String style) {
        int startIndex = getSelection().getStart();
        int endIndex = getSelection().getEnd();

        boolean isStyleAbsent = false;

        for (int i = startIndex; i < endIndex; ++i) {
            if (!getStyleAtPosition(i+1).contains(style)) {
                isStyleAbsent = true;
                break;
            }
        }

        if (isStyleAbsent) {
            for (int i = startIndex; i < endIndex; ++i) {
                String currentStyles = getStyleAtPosition(i + 1);
                if (!currentStyles.contains(style)) {
                    currentStyles += style;
                }
                setStyle(i, i + 1, currentStyles);
            }
        } else {
            for (int i = startIndex; i < endIndex; ++i) {
                String currentStyles = getStyleAtPosition(i + 1);
                currentStyles = currentStyles.replace(style, "");
                setStyle(i, i + 1, currentStyles);
            }
        }

        forceStyleChange(startIndex, endIndex);
    }

    public void applyTextAlignment(String alignmentStyle) {
        getStyleClass().removeIf(style -> style.contains("align"));
        ComponentUtil.setStyleClass(this, alignmentStyle);

        TextAlignmentButton.apply(alignmentStyle);
    }

    public void applyTextSize(int size) {
        int startIndex = getSelection().getStart();
        int endIndex = getSelection().getEnd();

        String sizeStyle = String.format("%s%dpt;\n", TextStyle.FONT_SIZE, size);
        applySingularStyle(sizeStyle, TextStyle.FONT_SIZE, startIndex, endIndex);
    }

    public void applyTextFill(String colour) {
        int startIndex = getSelection().getStart();
        int endIndex = getSelection().getEnd();

        String fillStyle = String.format("%s%s;\n", TextStyle.FONT_COLOUR, colour);
        applySingularStyle(fillStyle, TextStyle.FONT_COLOUR, startIndex, endIndex);
    }

    private void applySingularStyle(String style, String styleTemplate, int startIndex, int endIndex) {
        for (int i = startIndex; i < endIndex; ++i) {
            String currentStyles = getStyleAtPosition(i + 1);

            if (currentStyles.contains(styleTemplate)) {
                int startIndexToReplace = currentStyles.indexOf(styleTemplate);
                int lastIndexToReplace = currentStyles.indexOf("\n", startIndexToReplace) + 1;
                String toReplace = currentStyles.substring(startIndexToReplace, lastIndexToReplace);

                currentStyles = currentStyles.replace(toReplace, "");
            }

            currentStyles += style;

            System.out.println(currentStyles);

            setStyle(i, i + 1, currentStyles);
        }

        forceStyleChange(startIndex, endIndex);
    }

    private void updateCharacterStyles(String oldText, String newText) {
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

    private void forceStyleChange(int startIndex, int endIndex) {
        ignore = true;
        appendText("-");
        deleteText(getText().length() - 1, getText().length());
        ignore = false;
        selectRange(startIndex, endIndex);
    }

    private void addCaretListener() {
        caretPositionProperty().addListener(observable -> {
            if (!ignore) {
                synchroniseTextFormatButtons();
            }
        });

        selectedTextProperty().addListener(observable -> {
            if (!ignore) {
                synchroniseTextFormatButtons();
            }
        });
    }

    private void synchroniseTextFormatButtons() {
        if (getSelectedText().isEmpty()) {
            int index = getCaretPosition();
            TextStyleButton.apply(
                    containsStyle(TextStyle.BOLD, index),
                    containsStyle(TextStyle.ITALIC, index),
                    containsStyle(TextStyle.UNDERLINE, index),
                    containsStyle(TextStyle.STRIKETHROUGH, index)
            );
        } else {
            int startIndex = getSelection().getStart() + 1;
            int endIndex = getSelection().getEnd() + 1;
            TextStyleButton.apply(
                    containsStyle(TextStyle.BOLD, startIndex, endIndex),
                    containsStyle(TextStyle.ITALIC, startIndex, endIndex),
                    containsStyle(TextStyle.UNDERLINE, startIndex, endIndex),
                    containsStyle(TextStyle.STRIKETHROUGH, startIndex, endIndex)
            );
        }
    }

    private boolean containsStyle(String style, int index) {
        return getStyleAtPosition(index).contains(style);
    }

    private boolean containsStyle(String style, int startIndex, int endIndex) {
        for (int i = startIndex; i < endIndex; ++i) {
            if (!getStyleAtPosition(i).contains(style)) {
                return false;
            }
        }
        return true;
    }
}
