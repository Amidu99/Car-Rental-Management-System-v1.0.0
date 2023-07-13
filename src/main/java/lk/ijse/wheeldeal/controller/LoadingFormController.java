package lk.ijse.wheeldeal.controller;

import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.fxml.FXMLLoader;
import javafx.concurrent.Task;
import java.io.IOException;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.Objects;
import javafx.fxml.FXML;

public class LoadingFormController {
    @FXML
    private AnchorPane root;

    @FXML
    private Label progressLabel;

    @FXML
    private ProgressBar progressBar;
    private boolean isLoading = false;

    public void initialize() {
        if (!isLoading) {
            isLoading = true;
            Task<Void> task = new Task<>() {
            @Override
                protected Void call() throws Exception {
                    for (int i = 0; i <= 100; i++) {
                        updateProgress(i, 100);
                        updateMessage("Loading "+i+"%");
                        if(i==100){loadLoginForm();}
                        Thread.sleep(80);
                    }
                    return null;
                }
            };

        progressBar.progressProperty().bind(task.progressProperty());
        progressLabel.textProperty().bind(task.messageProperty());
        new Thread(task).start();
        }
    }

    private void loadLoginForm() {
        Platform.runLater(() -> {
            try {
                Stage loading = (Stage) root.getScene().getWindow();
                loading.close();
                AnchorPane anchorPane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/login_form.fxml")));
                Scene scene = new Scene(anchorPane);
                Stage login = new Stage();
                login.setTitle("Welcome to WheelDeal Auto Rental..");
                login.setScene(scene);
                login.setResizable(false);
                login.getIcons().add(new Image("asset/icons/icon.png"));
                login.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}