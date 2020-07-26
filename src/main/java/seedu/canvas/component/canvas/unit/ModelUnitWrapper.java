package seedu.canvas.component.canvas.unit;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import seedu.canvas.component.canvas.CanvasHandle;
import seedu.canvas.component.canvas.Direction;

import java.util.ArrayList;
import java.util.Arrays;

public class ModelUnitWrapper extends Rectangle {

    private ModelMoveHandle moveHandle;
    private ModelResizeHandle resizeHandleNW;
    private ModelResizeHandle resizeHandleNE;
    private ModelResizeHandle resizeHandleSW;
    private ModelResizeHandle resizeHandleSE;

    private ModelUnit modelUnit;

    public ModelUnitWrapper(ModelUnit modelUnit) {
        super();
        this.modelUnit = modelUnit;

        moveHandle = new ModelMoveHandle(modelUnit);
        resizeHandleNW = new ModelResizeHandle(modelUnit, Direction.NORTHWEST);
        resizeHandleNE = new ModelResizeHandle(modelUnit, Direction.NORTHEAST);
        resizeHandleSW = new ModelResizeHandle(modelUnit, Direction.SOUTHWEST);
        resizeHandleSE = new ModelResizeHandle(modelUnit, Direction.SOUTHEAST);

        initialiseStyle();
    }

    public ModelUnit getModelUnit() {
        return modelUnit;
    }

    public ArrayList<Node> getGroup() {
        ArrayList<Node> group = new ArrayList<>();
        group.add(this);
        group.addAll(getHandles());

        return group;
    }

    public void interactSingle() {
        bringForward();
        setStroke(Color.CORNFLOWERBLUE);
        getHandles().forEach(CanvasHandle::interactSingle);
    }

    public void focusSingle() {
        bringForward();
        setStroke(Color.LIGHTGREEN);
        getHandles().forEach(CanvasHandle::focusSingle);
    }

    public void interactMultiple() {
        bringForward();
        setStroke(Color.CADETBLUE);
        moveHandle.interactMultiple();
    }

    public void focusMultiple() {
        bringForward();
        setStroke(Color.GREEN);
        moveHandle.focusMultiple();
    }

    public void unfocus() {
        setVisible(false);
        getHandles().forEach(CanvasHandle::unfocus);
    }

    private void initialiseStyle() {
        setStrokeWidth(7);
        setOpacity(0.4);
        setFill(null);

        xProperty().bind(modelUnit.xProperty());
        yProperty().bind(modelUnit.yProperty());
        widthProperty().bind(modelUnit.widthProperty());
        heightProperty().bind(modelUnit.heightProperty());
    }

    private ArrayList<CanvasHandle> getHandles() {
        return new ArrayList<>(
                Arrays.asList(moveHandle, resizeHandleNW, resizeHandleNE, resizeHandleSW, resizeHandleSE));
    }

    private void bringForward() {
        toFront();
        setVisible(true);
        modelUnit.toFront();
    }
}
