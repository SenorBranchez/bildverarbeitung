package cosy.bv.converter;

import java.awt.*;


/*
    RGB to CIELab color space conversion: http://rsb.info.nih.gov/ij/plugins/download/Color_Space_Converter.java
 */

public class ColorSpaceConverter {

    /**
     * sRGB to XYZ conversion matrix
     */
    private static double[][] M   = { {0.4124, 0.3576,  0.1805}, {0.2126, 0.7152,  0.0722}, {0.0193, 0.1192,  0.9505} };

    private static double[] whitePoint = { 95.047f, 100.000f, 108.883f};


    public static int[] convertRgb2Yuv(int[] srcImage, int width, int height) {

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

    public static float[] convertRgb2Hsv(int[] srcImage, int width, int height) {

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

        return destImage;
    }

    public static double[] convertRgbToLab(int[] srcImage, int width, int height) {

        int numOfPixels = width*height;
        double[] destImage = new double[numOfPixels*3];

        int r;
        int g;
        int b;

        double[] lab = new double[3];

        for( int i = 0; i < numOfPixels; i+=3) {

            r = srcImage[i];
            g = srcImage[i+1];
            b = srcImage[i+2];

            lab = xyzToLab(rgbToXyz(r, g, b));

            destImage[i] = lab[0];
            destImage[i+1] = lab[1];
            destImage[i+2] = lab[2];
        }

        return destImage;
    }


    /**
     *
     * Helper methode used to convert RGB to CIELab
     *
     */
    private static double[] rgbToXyz(int rgb[]) {
        return rgbToXyz(rgb[0], rgb[1], rgb[2]);
    }

    private static double[] rgbToXyz(int R, int G, int B) {
        double[] result = new double[3];

        // convert 0..255 into 0..1
        double r = R / 255.0;
        double g = G / 255.0;
        double b = B / 255.0;

        // assume sRGB
        if (r <= 0.04045) {
            r = r / 12.92;
        }
        else {
            r = Math.pow(((r + 0.055) / 1.055), 2.4);
        }
        if (g <= 0.04045) {
            g = g / 12.92;
        }
        else {
            g = Math.pow(((g + 0.055) / 1.055), 2.4);
        }
        if (b <= 0.04045) {
            b = b / 12.92;
        }
        else {
            b = Math.pow(((b + 0.055) / 1.055), 2.4);
        }

        r *= 100.0;
        g *= 100.0;
        b *= 100.0;

        // [X Y Z] = [r g b][M]
        result[0] = (r * M[0][0]) + (g * M[0][1]) + (b * M[0][2]);
        result[1] = (r * M[1][0]) + (g * M[1][1]) + (b * M[1][2]);
        result[2] = (r * M[2][0]) + (g * M[2][1]) + (b * M[2][2]);

        return result;
    }

    private static double[] xyzToLab(double xyz[]) {
        return xyzToLab(xyz[0], xyz[1], xyz[2]);
    }

    private static double[] xyzToLab(double X, double Y, double Z) {

        double x = X / whitePoint[0];
        double y = Y / whitePoint[1];
        double z = Z / whitePoint[2];

        if (x > 0.008856) {
            x = Math.pow(x, 1.0 / 3.0);
        }
        else {
            x = (7.787 * x) + (16.0 / 116.0);
        }
        if (y > 0.008856) {
            y = Math.pow(y, 1.0 / 3.0);
        }
        else {
            y = (7.787 * y) + (16.0 / 116.0);
        }
        if (z > 0.008856) {
            z = Math.pow(z, 1.0 / 3.0);
        }
        else {
            z = (7.787 * z) + (16.0 / 116.0);
        }

        double[] result = new double[3];

        result[0] = (116.0 * y) - 16.0;
        result[1] = 500.0 * (x - y);
        result[2] = 200.0 * (y - z);

        return result;
    }


}
