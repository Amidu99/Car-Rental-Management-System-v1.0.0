package lk.ijse.wheeldeal;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.StageStyle;
import lk.ijse.wheeldeal.controller.LoadingFormController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AppInitializer extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/loading_form.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root,600, 400, Color.TRANSPARENT);
        stage.getIcons().add(new Image("asset/icons/icon.png"));
        root.setStyle("-fx-background-color: transparent");
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.centerOnScreen();
        stage.setScene(scene);
        stage.show();

        LoadingFormController loadingController = loader.getController();
        loadingController.initialize();
    }

    public static void main(String[] args) {
        launch();
    }
}