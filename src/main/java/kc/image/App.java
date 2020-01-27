package kc.image;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.MBFImage;
import sun.misc.BASE64Decoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.Instant;
import java.util.Base64;

/** Hello world! */
public class App {

    public static void main(String args[]) throws IOException {
        URL fileUrl = App.class.getClassLoader().getResource("img/bugs.jpg");
        File file = new File(fileUrl.getPath());
        String folder = file.getParentFile().getAbsolutePath();
        String fileName = file.getName();
        String fileNameWithoutExtension = StringUtils.substringBefore(fileName, ".");
        String fileExtension = StringUtils.substringAfterLast(fileName, ".");

        // image to base64 text
        String imgBase64Text = toBase64Text(fileUrl.getPath());
        System.out.println(imgBase64Text);

        // save base64 text to file
        String newFilePath =
                String.format(
                        "%s/%s-%s.%s",
                        folder,
                        fileNameWithoutExtension,
                        Instant.now().toEpochMilli(),
                        fileExtension);
        // saveImage(imgBase64Text, newFilePath);
        System.out.println(String.format("Image created: %s", newFilePath));

        // image type (extension)
        System.out.println(getImageExtension(imgBase64Text));

        // base64 text to BufferedImage
        BufferedImage bufferedImage = toBufferedImage(imgBase64Text);
        System.out.println(
                String.format("%sx%s", bufferedImage.getWidth(), bufferedImage.getHeight()));

        // base64 text to BufferedImage
        MBFImage mbfImage = toMBFImage(imgBase64Text);
        System.out.println(String.format("%sx%s", mbfImage.getWidth(), mbfImage.getHeight()));
    }

    public static String toBase64Text(String imgFilePath) throws IOException {
        byte[] fileContent = FileUtils.readFileToByteArray(new File(imgFilePath));
        String encodedString = Base64.getEncoder().encodeToString(fileContent);
        return encodedString;
    }

    public static void saveImage(String imgBase64Text, String filePath) throws IOException {
        byte[] decodedBytes = Base64.getDecoder().decode(imgBase64Text);
        FileUtils.writeByteArrayToFile(new File(filePath), decodedBytes);
    }

    public static BufferedImage toBufferedImage(String imgBase64Text) throws IOException {
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] imageByte = decoder.decodeBuffer(imgBase64Text);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(imageByte);
        // not work for gif
        BufferedImage bufferedImage = ImageIO.read(byteArrayInputStream);
        byteArrayInputStream.close();
        return bufferedImage;
    }

    public static MBFImage toMBFImage(String imgBase64Text) throws IOException {
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] imageByte = decoder.decodeBuffer(imgBase64Text);
        // not work for gif
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(imageByte);
        MBFImage image = ImageUtilities.readMBF(byteArrayInputStream);
        byteArrayInputStream.close();
        return image;
    }

    public static String getImageExtension(String imgBase64Text) {
        if (StringUtils.isBlank(imgBase64Text)) {
            return null;
        } else {
            char firstChar = imgBase64Text.charAt(0);
            switch (firstChar) {
                case '/':
                    return "jpg";
                case 'i':
                    return "png";
                case 'R':
                    return "gif";
                case 'U':
                    return "webp";

                default:
                    return null;
            }
        }
    }
}
