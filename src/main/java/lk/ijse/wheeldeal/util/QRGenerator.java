package lk.ijse.wheeldeal.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;

public class QRGenerator {
    private static final int QR_CODE_SIZE = 200;

    public static void generateQRCode(String content, String fileName) {
        try {
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            BitMatrix matrix = new MultiFormatWriter().encode(content, com.google.zxing.BarcodeFormat.QR_CODE, QR_CODE_SIZE, QR_CODE_SIZE, hints);
            int width = matrix.getWidth();
            int height = matrix.getHeight();
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    image.setRGB(x, y, matrix.get(x, y) ? 0x000000 : 0xFFFFFF);
                }
            }
            String filePath = "C:\\Users\\TOSHIBA\\Documents\\Generated QRs\\"+fileName;
            File file = new File(filePath);
            ImageIO.write(image, "png", file);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}