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


    public static YUVImage convertRgbToYuv(RGBImage rgbImage) {

        int width;
        int height;

        int tmp_r;
        int tmp_g;
        int tmp_b;
        int tmp_y;
        int tmp_u;
        int tmp_v;

        YUVImage yuvImage;

        Channel<Integer> r = rgbImage.getChannel("r");
        Channel<Integer> g = rgbImage.getChannel("g");
        Channel<Integer> b = rgbImage.getChannel("b");

        width = r.getWidth();
        height = r.getHeight();

        Channel<Integer> y = new Channel<Integer>(width, height);
        Channel<Integer> u = new Channel<Integer>(width, height);
        Channel<Integer> v = new Channel<Integer>(width, height);

        yuvImage = new YUVImage();


        for(int i=0; i<width; i++) {
            for(int j=0; j<height; j++) {

                tmp_r = r.getPixelValue(i,j);
                tmp_g = g.getPixelValue(i,j);
                tmp_b = b.getPixelValue(i,j);

                tmp_y = (int) Math.round( 0.299*tmp_r + 0.587*tmp_g + 0.114*tmp_b );
                tmp_u = (int) Math.round( 0.492 * ( tmp_b - tmp_y ) );
                tmp_v = (int) Math.round( 0.299 * (tmp_r - tmp_y) );

                y.setPixelValue(i, j, tmp_y);
                u.setPixelValue(i, j, tmp_u);
                v.setPixelValue(i, j, tmp_v);
            }
        }

        yuvImage.addChannel("y", y);
        yuvImage.addChannel("u", u);
        yuvImage.addChannel("v", v);

        return yuvImage;
    }

    public static HSVImage convertRgbToHsv(RGBImage rgbImage) {

        int width;
        int height;

        int tmp_r;
        int tmp_g;
        int tmp_b;
        float hsv[] = new float[3];

        HSVImage hsvImage;

        Channel<Integer> r = rgbImage.getChannel("r");
        Channel<Integer> g = rgbImage.getChannel("g");
        Channel<Integer> b = rgbImage.getChannel("b");

        width = r.getWidth();
        height = r.getHeight();

        Channel<Float> h = new Channel<Float>(width, height);
        Channel<Float> s = new Channel<Float>(width, height);
        Channel<Float> v = new Channel<Float>(width, height);

        hsvImage = new HSVImage();

        for(int i=0; i<width; i++) {
            for(int j=0; j<height; j++) {

                tmp_r = r.getPixelValue(i,j);
                tmp_g = g.getPixelValue(i,j);
                tmp_b = b.getPixelValue(i,j);

                Color.RGBtoHSB(tmp_r, tmp_g, tmp_b, hsv);

                h.setPixelValue(i, j, hsv[0]);
                s.setPixelValue(i, j, hsv[1]);
                v.setPixelValue(i, j, hsv[2]);
            }
        }

        hsvImage.addChannel("h", h);
        hsvImage.addChannel("s", s);
        hsvImage.addChannel("v", v);

        return hsvImage;

    }

    public static LABImage convertRgbToLab(RGBImage rgbImage) {

        int width;
        int height;

        int tmp_r;
        int tmp_g;
        int tmp_b;

        double[] lab = new double[3];

        LABImage labImage;

        Channel<Integer> r = rgbImage.getChannel("r");
        Channel<Integer> g = rgbImage.getChannel("g");
        Channel<Integer> b = rgbImage.getChannel("b");

        width = r.getWidth();
        height = r.getHeight();

        Channel<Double> cielab_l = new Channel<Double>(width, height);
        Channel<Double> cielab_a = new Channel<Double>(width, height);
        Channel<Double> cielab_b = new Channel<Double>(width, height);

        labImage = new LABImage();


        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {

                tmp_r = r.getPixelValue(i, j);
                tmp_g = g.getPixelValue(i, j);
                tmp_b = b.getPixelValue(i, j);

                lab = xyzToLab(rgbToXyz(tmp_r, tmp_g, tmp_b));

                cielab_l.setPixelValue(i, j, lab[0]);
                cielab_a.setPixelValue(i, j, lab[1]);
                cielab_b.setPixelValue(i, j, lab[2]);
            }
        }

        labImage.addChannel("l", cielab_l);
        labImage.addChannel("a", cielab_a);
        labImage.addChannel("b", cielab_b);

        return labImage;
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
