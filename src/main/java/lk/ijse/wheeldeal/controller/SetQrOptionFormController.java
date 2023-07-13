package lk.ijse.wheeldeal.controller;

import animatefx.animation.FadeInUp;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.io.IOException;

public class SetQrOptionFormController {
    @FXML
    private Label lblCamera;

    @FXML
    private Label lblUpload;

    public void btnCameraOnAction(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/qr_reader_camera_form.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("QR Code Scanner");
        stage.getIcons().add(new Image("asset/icons/icon.png"));
        stage.centerOnScreen();
        stage.setResizable(false);
        stage.show();
        Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        window.close();
    }

    public void btnUploadOnAction(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/qr_reader_form.fxml"));
        Parent root = loader.load();
        new FadeInUp(root).play();
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("QR Code Scanner");
        stage.getIcons().add(new Image("asset/icons/icon.png"));
        stage.centerOnScreen();
        stage.setResizable(false);
        stage.show();
        Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        window.close();
    }

    public void btnCameraEnter(MouseEvent event) {
        lblCamera.setVisible(true);
    }

    public void btnCameraExit(MouseEvent event) {
        lblCamera.setVisible(false);
    }

    public void btnUploadEnter(MouseEvent event) {
        lblUpload.setVisible(true);
    }

    public void btnUploadExit(MouseEvent event) {
        lblUpload.setVisible(false);
    }
}