package seedu.canvas;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import seedu.canvas.storage.FilePath;
import seedu.canvas.util.SceneUtil;

import java.io.IOException;

public class CanVas extends Application {

    @Override
    public void init() {
    }

    @Override
    public void start(Stage stage) throws IOException {
        stage.setTitle("FRACTAL");
        stage.getIcons().add(new Image(FilePath.ICON_IMAGE_PATH));
        stage.setMinWidth(800);
        stage.setMinHeight(600);

        stage.setOnCloseRequest(event -> {
            // Close event
            stage.close();
        });

        FXMLLoader sceneLoader = new FXMLLoader(getClass().getResource(FilePath.MENU_SCENE_PATH));
        Parent root = sceneLoader.load();
        Scene menuScene = new Scene(root);

        stage.setScene(menuScene);
        stage.show();

        SceneUtil.centreScene(stage);
    }
}
