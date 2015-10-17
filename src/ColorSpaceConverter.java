import java.awt.*;

public class ColorSpaceConverter {

    static int[] convertRgb2Yuv( int[] srcImage, int width, int height) {

        int numOfPixels = width*height;
        int[] destImage = new int[numOfPixels*3];

        int r;
        int g;
        int b;

        int y;
        int u;
        int v;

        for( int i = 0; i < numOfPixels; i+=3) {

            r = srcImage[i];
            g = srcImage[i+1];
            b = srcImage[i+2];

            y = (int) Math.round( 0.299*r + 0.587*g + 0.114*b );
            u = (int) Math.round( 0.492 * ( b-y) );
            v = (int) Math.round( 0.299 * (r-y) );

            destImage[i]  = y;
            destImage[i+1]  = u;
            destImage[i+2]  = v;
        }

        return destImage;
    }

    static float[] convertRgb2Hsv( int[] srcImage, int width, int height) {

        int numOfPixels = width*height;
        float[] destImage = new float[numOfPixels*3];

        int r;
        int g;
        int b;

        float[] hsv = new float[3];

        for( int i = 0; i < numOfPixels; i+=3) {

            r = srcImage[i];
            g = srcImage[i+1];
            b = srcImage[i+2];

            // Note: HSB is the same as HSV
            Color.RGBtoHSB(r,g,b,hsv);

            destImage[i]  = hsv[0];
            destImage[i+1]  = hsv[1];
            destImage[i+2]  = hsv[2];


        }

        return null;
    }
}
