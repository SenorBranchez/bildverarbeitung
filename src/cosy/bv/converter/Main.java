package cosy.bv.converter;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String [] args) {

        BufferedImage srcImage;
        JFrame mainFrameR;
        JFrame mainFrameG;
        JFrame mainFrameB;

        GrayscaleImageDisplay displayR;
        GrayscaleImageDisplay displayG;
        GrayscaleImageDisplay displayB;
        File output;
        String filename = "akh0001-cut-1112629217893.png";
        int[] srcImageData;
        int[] destImageYUVData;
        float[] destImageHSVData;
        double[] destImageLabData;
        int width;
        int height;


        mainFrameR = new JFrame();
        mainFrameG = new JFrame();
        mainFrameB = new JFrame();

        displayR = new GrayscaleImageDisplay();
        displayG = new GrayscaleImageDisplay();
        displayB = new GrayscaleImageDisplay();

        mainFrameR.setSize(256, 256);
        mainFrameG.setSize(256, 256);
        mainFrameB.setSize(256, 256);

        mainFrameR.setTitle("Channel R");
        mainFrameG.setTitle("Channel G");
        mainFrameB.setTitle("Channel B");


        mainFrameR.setVisible(true);
        mainFrameG.setVisible(true);
        mainFrameB.setVisible(true);



        try {

            srcImage = ImageIO.read( new File(filename) );

            RGBImage rgbImage = new RGBImage(filename);

            displayR.setChannel(rgbImage.getChannel("r"));
            displayG.setChannel(rgbImage.getChannel("g"));
            displayB.setChannel(rgbImage.getChannel("b"));

            mainFrameR.add(displayR);
            mainFrameG.add(displayG);
            mainFrameB.add(displayB);

            mainFrameR.repaint();
            mainFrameB.repaint();
            mainFrameG.repaint();


            //YUVImage yuvImage = ColorSpaceConverter.convertRgbToYuv(rgbImage);
            //HSVImage hsvImage = ColorSpaceConverter.convertRgbToHsv(rgbImage);
            //LABImage labImage = ColorSpaceConverter.convertRgbToLab(rgbImage);
            //System.out.println(labImage.toString());

        } catch (IOException e) {
            System.err.print("File " + filename + " not found");
        }
    }
}
