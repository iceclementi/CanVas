package seedu.canvas.component.canvas.text;

import javafx.geometry.Insets;
import javafx.scene.input.MouseEvent;
import org.fxmisc.richtext.InlineCssTextArea;
import seedu.canvas.component.canvas.utility.format.text.TextAlignmentButton;
import seedu.canvas.component.canvas.utility.format.text.TextPaletteColour;
import seedu.canvas.component.canvas.utility.format.text.TextSizeSpinner;
import seedu.canvas.component.canvas.utility.format.text.TextStyleButton;
import seedu.canvas.storage.FilePath;
import seedu.canvas.util.ComponentUtil;

public class TextBoxContent extends InlineCssTextArea {

    private boolean ignore = false;

    private TextBox textBox;

    public TextBoxContent(TextBox textBox) {
        this.textBox = textBox;

        initialiseStyle();
        initialiseEvents();
    }

    public TextBox getTextBox() {
        return textBox;
    }

    public void removeHighlight() {
        if (!getSelectedText().isEmpty()) {
            selectRange(0, 0);
        }
    }

    private void initialiseStyle() {
        layoutXProperty().bind(textBox.xProperty());
        layoutYProperty().bind(textBox.yProperty());
        prefWidthProperty().bind(textBox.widthProperty());
        prefHeightProperty().bind(textBox.heightProperty());

        ComponentUtil.setStyleClass(this, FilePath.TEXT_STYLE_PATH, "default-font");
        ComponentUtil.setStyleClass(this, "align-left");

        setBackground(null);

        setWrapText(true);
        setPadding(new Insets(2));
    }

    private void initialiseEvents() {
        TextBoxContentEventManager eventManager = new TextBoxContentEventManager();

        addEventFilter(MouseEvent.MOUSE_PRESSED, eventManager.getOnMousePressed());
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
