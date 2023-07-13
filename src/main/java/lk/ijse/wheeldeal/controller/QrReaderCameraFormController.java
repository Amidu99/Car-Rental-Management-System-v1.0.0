package lk.ijse.wheeldeal.controller;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.jfoenix.controls.JFXTextArea;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class QrReaderCameraFormController {
    @FXML
    private ImageView imgCamera;

    @FXML
    private JFXTextArea txtDetails;
    private VideoCapture capture;
    private Mat frame;
    private boolean process;

    public QrReaderCameraFormController() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        capture = new VideoCapture(0);
        frame = new Mat();
        process = true;
        new Thread(() -> {
            while (process) {
                // Capture a new frame from the camera
                capture.read(frame);
                // Convert the frame to a byte array
                MatOfByte buffer = new MatOfByte();
                Imgcodecs.imencode(".bmp", frame, buffer);
                byte[] imageData = buffer.toArray();
                // Create a new thread to detect the QR code
                new Thread(() -> {
                    detectQRCode(imageData);
                }).start();
                // Update the ImageView with the new frame
                Platform.runLater(() -> {
                    Image image = new Image(new ByteArrayInputStream(imageData));
                    imgCamera.setImage(image);
                });
            }
        }).start();
    }

    private void detectQRCode(byte[] imageData) {
        try {
            InputStream in = new ByteArrayInputStream(imageData);
            BufferedImage bufferedImage = ImageIO.read(in);
            LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
            Reader reader = new MultiFormatReader();
            Result result=null;
            try {
                result = reader.decode(bitmap);
                if(result!=null){
                    txtDetails.setText(result.getText());
                }
            } catch (NotFoundException | ChecksumException | FormatException e) {System.out.print("");}
        } catch (IOException | IndexOutOfBoundsException e) {System.out.print("");}
    }

    @FXML
    public void btnExitOnAction(ActionEvent actionEvent) {
        try {
            process = false;
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally{
            capture.release();
            frame.release();
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.close();
        }
    }
}