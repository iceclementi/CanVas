package seedu.canvas.component.canvas.text;

import javafx.geometry.Insets;
import javafx.geometry.Point2D;
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
import seedu.canvas.component.canvas.utility.format.text.TextPaletteColour;
import seedu.canvas.component.canvas.utility.format.text.TextSizeSpinner;
import seedu.canvas.component.canvas.utility.format.text.TextStyleButton;
import seedu.canvas.storage.FilePath;
import seedu.canvas.util.CanvasMath;
import seedu.canvas.util.ComponentUtil;

import java.util.ArrayList;

public class TextBox extends InlineCssTextArea implements CanvasNode {

    public static final double MIN_WIDTH = 40d;
    public static final double MIN_HEIGHT = 30d;

    private TheCanvas canvas = TheCanvas.getInstance();
    private TextBoxWrapper wrapper = new TextBoxWrapper(this);

    private Point2D pivotPoint;

    private boolean ignore = false;

    public TextBox(double x, double y) {
        super();

        pivotPoint = new Point2D(x, y);

        initialiseStyle();
        initialiseEvents();

        canvas.addNode(this);
        relocate(x, y);
    }

    public double getCanvasStartX() {
        return getLayoutX();
    }

    public double getCanvasStartY() {
        return getLayoutY();
    }

    public double getCanvasEndX() {
        return getLayoutX() + getWidth();
    }

    public double getCanvasEndY() {
        return getLayoutY() + getHeight();
    }

    public ArrayList<Node> getGroup() {
        ArrayList<Node> group = new ArrayList<>();
        group.add(this);
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

        if (!getSelectedText().isEmpty()) {
            selectRange(0, 0);
        }

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

        relocate(finalNewX, finalNewY);
    }

    public void colourLine(Color lineColour) {
        setBorder(new Border(
                new BorderStroke(lineColour, BorderStrokeStyle.SOLID, new CornerRadii(0), new BorderWidths(2))));
    }

    public void colourFill(Color colour) {
        setBackground(new Background(new BackgroundFill(colour, CornerRadii.EMPTY, Insets.EMPTY)));
    }

    public void setDefaultSize() {
        setPrefSize(CanvasGrid.OFFSET * 4, CanvasGrid.OFFSET * 2);
    }

    private void scaleEast(double endX) {
        double newWidth = CanvasMath.clamp(endX - pivotPoint.getX(), 0, CanvasGrid.WIDTH - pivotPoint.getX());

        setPrefWidth(newWidth);
    }

    private void scaleSouth(double endY) {
        double newHeight = CanvasMath.clamp(endY - pivotPoint.getY(), 0, CanvasGrid.HEIGHT - pivotPoint.getY());

        setPrefHeight(newHeight);
    }

    private void scaleWest(double endX) {
        double newX = CanvasMath.clamp(endX, 0, pivotPoint.getX());
        double newWidth = CanvasMath.clamp(pivotPoint.getX() - newX, 0, pivotPoint.getX());

        setLayoutX(newX);
        setPrefWidth(newWidth);
    }

    private void scaleNorth(double endY) {
        double newY = CanvasMath.clamp(endY, 0, pivotPoint.getY());
        double newHeight = CanvasMath.clamp(pivotPoint.getY() - newY, 0, pivotPoint.getY());

        setLayoutY(newY);
        setPrefHeight(newHeight);
    }

    private void initialiseStyle() {
        ComponentUtil.setStyleClass(this, FilePath.TEXT_STYLE_PATH, "default-font");
        ComponentUtil.setStyleClass(this, "align-left");

        setBorder(new Border(
                new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(0), new BorderWidths(2))));
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

        addBasicTextListener();
        addKeyShortcutEvent();
        addResponsiveTextStyleListener();
    }

    private void addBasicTextListener() {
        textProperty().addListener((observable, oldText, newText) -> {

            if (ignore) {
                return;
            }

            int caretPosition = getCaretPosition();

            int afterLength = newText.length() - caretPosition;

            int oldBeforeLength = oldText.length() - afterLength;
            int newBeforeLength = newText.length() - afterLength;

            updateText(oldText.substring(0, oldBeforeLength), newText.substring(0, newBeforeLength));
        });
    }

    private void addKeyShortcutEvent() {
        setOnKeyPressed(keyEvent -> {
            if (keyEvent.isControlDown()) {

                switch (keyEvent.getCode()) {
                case B:
                    TextStyleButton.applyTextStyle(TextStyle.BOLD);
                    break;
                case I:
                    TextStyleButton.applyTextStyle(TextStyle.ITALIC);
                    break;
                case U:
                    TextStyleButton.applyTextStyle(TextStyle.UNDERLINE);
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
                        TextSizeSpinner.decrement();
                    }
                    break;
                case PERIOD:
                    if (keyEvent.isShiftDown()) {
                        TextSizeSpinner.increment();
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

        String nextStyle = generateNextStyle(index + 1);

        for (int i = index; i < newText.length(); ++i) {
            setStyle(i, i + 1, nextStyle);
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

        newStyle = newStyle.replace(retrieveSingularStyle(TextStyle.FONT_SIZE, newStyle), "");
        newStyle += String.format("%s%dpt;\n", TextStyle.FONT_SIZE, TextSizeSpinner.getSize());

        newStyle = newStyle.replace(retrieveSingularStyle(TextStyle.FONT_COLOUR, newStyle), "");
        newStyle += String.format("%s%s;\n", TextStyle.FONT_COLOUR, TextPaletteColour.getFillColour());

        return newStyle;
    }

    public void applyTextStyle(String style) {
        if (getSelectedText().isEmpty()) {
            return;
        }

        int startIndex = getSelection().getStart();
        int endIndex = getSelection().getEnd();

        boolean isStyleAbsent = false;

        for (int i = startIndex; i < endIndex; ++i) {
            if (!getStyleAtPosition(i + 1).contains(style)) {
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
        synchroniseTextFormatButtons();
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

            String toReplace = retrieveSingularStyle(styleTemplate, i + 1);
            currentStyles = currentStyles.replace(toReplace, "");

            currentStyles += style;

            // System.out.println(currentStyles);

            setStyle(i, i + 1, currentStyles);
        }

        forceStyleChange(startIndex, endIndex);
    }


    private void forceStyleChange(int startIndex, int endIndex) {
        ignore = true;
        appendText("-");
        deleteText(getText().length() - 1, getText().length());
        ignore = false;
        selectRange(startIndex, endIndex);
    }

    private void addResponsiveTextStyleListener() {
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

    public void synchroniseTextFormatButtons() {
        if (getSelectedText().isEmpty()) {
            int index = getCaretPosition();
            TextStyleButton.applyTextStyle(
                    containsStyle(TextStyle.BOLD, index),
                    containsStyle(TextStyle.ITALIC, index),
                    containsStyle(TextStyle.UNDERLINE, index),
                    containsStyle(TextStyle.STRIKETHROUGH, index)
            );
            TextPaletteColour.pick(retrieveSingularStyle(TextStyle.FONT_COLOUR, index));
            updateTextSizeSpinner(index, index + 1);
        } else {
            int startIndex = getSelection().getStart() + 1;
            int endIndex = getSelection().getEnd() + 1;
            TextStyleButton.applyTextStyle(
                    containsStyle(TextStyle.BOLD, startIndex, endIndex),
                    containsStyle(TextStyle.ITALIC, startIndex, endIndex),
                    containsStyle(TextStyle.UNDERLINE, startIndex, endIndex),
                    containsStyle(TextStyle.STRIKETHROUGH, startIndex, endIndex)
            );

            if (containsSingularStyle(TextStyle.FONT_COLOUR, startIndex, endIndex)) {
                TextPaletteColour.pick(retrieveSingularStyle(TextStyle.FONT_COLOUR, startIndex));
            } else {
                TextPaletteColour.resetAll();
            }

            updateTextSizeSpinner(startIndex, endIndex);
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

    private boolean containsSingularStyle(String styleTemplate, int startIndex, int endIndex) {
        String toMatch = retrieveSingularStyle(styleTemplate, startIndex);

        for (int i = startIndex + 1; i < endIndex; ++i) {
            if (!getStyleAtPosition(i).contains(toMatch)) {
                return false;
            }
        }
        return true;
    }

    private void updateTextSizeSpinner(int startIndex, int endIndex) {
        String firstSizeStyle = retrieveSingularStyle(TextStyle.FONT_SIZE, startIndex);
        if (firstSizeStyle.isEmpty()) {
            return;
        }

        int minSize = Integer.parseInt(firstSizeStyle.replace(TextStyle.FONT_SIZE, "").replace("pt;\n", ""));
        boolean isSame = true;

        for (int i = startIndex + 1; i < endIndex; ++i) {
            String sizeStyle = retrieveSingularStyle(TextStyle.FONT_SIZE, i);
            int size = Integer.parseInt(sizeStyle.replace(TextStyle.FONT_SIZE, "").replace("pt;\n", ""));

            if (size != minSize) {
                isSame = false;
                minSize = Math.min(minSize, size);
            }
        }

        if (isSame) {
            TextSizeSpinner.applySize(String.valueOf(minSize), minSize);
        } else {
            TextSizeSpinner.applySize(String.format("%d+", minSize), minSize);
        }
    }

    private String retrieveSingularStyle(String styleTemplate, int index) {
        String currentStyles = getStyleAtPosition(index);
        return retrieveSingularStyle(styleTemplate, currentStyles);
    }

    private String retrieveSingularStyle(String styleTemplate, String currentStyles) {
        if (currentStyles.contains(styleTemplate)) {
            int startIndexToReplace = currentStyles.indexOf(styleTemplate);
            int lastIndexToReplace = currentStyles.indexOf("\n", startIndexToReplace) + 1;
            return currentStyles.substring(startIndexToReplace, lastIndexToReplace);
        } else {
            System.out.println("TextBox: Singular style absent");
            return "";
        }
    }
}
