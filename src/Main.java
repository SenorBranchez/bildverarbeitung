import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String [] args) {

        BufferedImage srcImage;
        File output;
        String filename = "test.jpg";
        int[] srcImageData;
        int[] destImageYUVData;
        float[] destImageHSVData;
        double[] destImageLabData;
        int width;
        int height;

        try {

            srcImage = ImageIO.read( new File(filename) );
            output = new File("out.jpeg");

            width = srcImage.getWidth();
            height = srcImage.getHeight();

            srcImageData = srcImage.getRGB(0, 0, width, height, null, 0, width);

            destImageYUVData = ColorSpaceConverter.convertRgb2Yuv(srcImageData, width, height);
            destImageHSVData = ColorSpaceConverter.convertRgb2Hsv(srcImageData, width, height);
            destImageLabData = ColorSpaceConverter.convertRgbToLab(srcImageData, width, height);


        } catch (IOException e) {
            System.err.print("File " + filename + " not found");
        }
    }
}
