package lk.ijse.wheeldeal.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lk.ijse.wheeldeal.model.VehicleImageModel;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

public class VehicleImageFormController {

    @FXML
    private Label lblVehiNo;

    @FXML
    private ImageView imgView;
    private Image image;
    private InputStream inputStream;

    public void btnBrowseOnAction(ActionEvent actionEvent) throws SQLException, IOException {
        String vehiNo = lblVehiNo.getText();
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(new Stage());
        FileInputStream fis;
        if(VehicleImageModel.isEmpty(vehiNo)) {
            if (file != null) {
                try {
                    fis = new FileInputStream(file);
                    boolean isAdded = VehicleImageModel.addImage(fis, vehiNo);
                    if (isAdded) {
                        new Alert(Alert.AlertType.CONFIRMATION, "Image Added Successfully!").show();
                        inputStream = VehicleImageModel.getImage(vehiNo);
                        if (inputStream != null) {
                            image = new Image(inputStream);
                            imgView.setImage(image);
                            imgView.setPreserveRatio(false);
                        }
                    }
                }catch (SQLException | IOException | ClassNotFoundException e) {
                    new Alert(Alert.AlertType.ERROR, "Something went wrong, Image not added..\n"+e).show();
                }
            }
        } else {
            if (file != null) {
                fis = new FileInputStream(file);
                try {
                    boolean isUpdated = VehicleImageModel.updateImage(fis, vehiNo);
                    if (isUpdated) {
                        new Alert(Alert.AlertType.CONFIRMATION, "Image Updated Successfully!").show();
                        inputStream = VehicleImageModel.getImage(vehiNo);
                        if (inputStream != null) {
                            image = new Image(inputStream);
                            imgView.setImage(image);
                            imgView.setPreserveRatio(false);
                        }
                    }
                } catch (SQLException | ClassNotFoundException e) {
                    new Alert(Alert.AlertType.ERROR, "Something went wrong, Image not added..\n" + e).show();
                }
            }
        }
    }

    public void setImage(String vehiNo) {
        try {
            inputStream = VehicleImageModel.getImage(vehiNo);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (inputStream != null) {
            image = new Image(inputStream);
            imgView.setImage(image);
            imgView.setPreserveRatio(false);
        } else{
            String imagePath = "asset/icons/add-new-image.png";
            Image image = new Image(imagePath);
            imgView.setImage(image);
        }
    }

    public void setVehicleNoLabel(String vehiNo){
        lblVehiNo.setText(vehiNo);
    }

    public void btnImgExitOnAction(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }
}