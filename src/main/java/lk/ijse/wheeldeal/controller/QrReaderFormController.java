package lk.ijse.wheeldeal.controller;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.jfoenix.controls.JFXTextArea;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class QrReaderFormController {
    @FXML
    private ImageView qrViewer;

    @FXML
    private JFXTextArea txtDetails;

    @FXML
    private void btnBrowseOnAction(MouseEvent event) {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(new Stage());
        try {
            if(file != null) {
                Image image = new Image(new FileInputStream(file));
                qrViewer.setImage(image);
                InputStream inputStream = new FileInputStream(file.getAbsolutePath());
                BufferedImage bufferedImage = ImageIO.read(inputStream);
                LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
                BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
                Reader reader = new MultiFormatReader();
                Result result = reader.decode(bitmap);
                txtDetails.setText(result.getText());
            }
        }
        catch (IOException | ChecksumException | NotFoundException | FormatException e) {
            e.printStackTrace();
        }
    }

    public void imgClickOnAction(MouseEvent event) {
        btnBrowseOnAction(event);
    }

    public void btnExitOnAction(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }
}