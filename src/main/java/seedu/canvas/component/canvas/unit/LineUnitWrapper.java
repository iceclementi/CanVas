package seedu.canvas.component.canvas.unit;

import javafx.scene.Node;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import seedu.canvas.component.canvas.CanvasHandle;
import seedu.canvas.component.canvas.Direction;

import java.util.ArrayList;
import java.util.Arrays;

public class LineUnitWrapper {

    private LineMoveHandle moveHandle;
    private LineResizeHandle resizeHandleWest;
    private LineResizeHandle resizeHandleEast;

    private LineUnit lineUnit;

    public LineUnitWrapper(LineUnit lineUnit) {
        this.lineUnit = lineUnit;

        moveHandle = new LineMoveHandle(lineUnit);
        resizeHandleWest = new LineResizeHandle(lineUnit, Direction.WEST);
        resizeHandleEast = new LineResizeHandle(lineUnit, Direction.EAST);
    }

    public LineUnit getLineUnit() {
        return lineUnit;
    }

    public ArrayList<Node> getGroup() {
        return new ArrayList<>(getHandles());
    }

    public void interactSingle() {
        lineUnit.toFront();
        highlight(Color.CORNFLOWERBLUE);
        getHandles().forEach(CanvasHandle::interactSingle);
    }

    public void focusSingle() {
        lineUnit.toFront();
        highlight(Color.LIGHTGREEN);
        getHandles().forEach(CanvasHandle::focusSingle);
    }

    public void interactMultiple() {
        lineUnit.toFront();
        highlight(Color.CADETBLUE);
        moveHandle.interactMultiple();
    }

    public void focusMultiple() {
        lineUnit.toFront();
        highlight(Color.GREEN);
        moveHandle.focusMultiple();
    }

    public void unfocus() {
        lineUnit.setEffect(null);
        getHandles().forEach(CanvasHandle::unfocus);
    }

    private ArrayList<CanvasHandle> getHandles() {
        return new ArrayList<>(Arrays.asList(moveHandle, resizeHandleEast, resizeHandleWest));
    }

    private void highlight(Color colour) {
        lineUnit.setEffect(new DropShadow(BlurType.GAUSSIAN, colour, 7, 0.5, 0, 0));
    }
}
