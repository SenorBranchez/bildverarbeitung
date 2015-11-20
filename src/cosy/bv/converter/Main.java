package cosy.bv.converter;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String [] args) {

        BufferedImage srcImage;
        JFrame mainFrame;

        GrayscaleImageDisplay display;
        File output;
        String filename = "test.jpg";
        int[] srcImageData;
        int[] destImageYUVData;
        float[] destImageHSVData;
        double[] destImageLabData;
        int width;
        int height;


        mainFrame = new JFrame();
        display = new GrayscaleImageDisplay();

        mainFrame.setSize(800,600);
        mainFrame.add(display);

        mainFrame.setVisible(true);



        try {

            srcImage = ImageIO.read( new File(filename) );

            RGBImage rgbImage = new RGBImage(filename);
            

            //YUVImage yuvImage = ColorSpaceConverter.convertRgbToYuv(rgbImage);
            //HSVImage hsvImage = ColorSpaceConverter.convertRgbToHsv(rgbImage);
            //LABImage labImage = ColorSpaceConverter.convertRgbToLab(rgbImage);
            //System.out.println(labImage.toString());

        } catch (IOException e) {
            System.err.print("File " + filename + " not found");
        }
    }
}
