package cosy.bv.converter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class RGBImage extends Image {

    public RGBImage( String filename ) throws IOException {

        BufferedImage srcImage = ImageIO.read(new File(filename));

        Channel<Integer> r = new Channel<Integer>(srcImage.getWidth(),srcImage.getHeight());
        Channel<Integer> g = new Channel<Integer>(srcImage.getWidth(),srcImage.getHeight());
        Channel<Integer> b = new Channel<Integer>(srcImage.getWidth(),srcImage.getHeight());

        int rgb;

        for( int x=0; x<srcImage.getWidth(); x++ ) {
            for( int y=0; y<srcImage.getHeight(); y++ ) {

                rgb = srcImage.getRGB(x,y);

                r.setPixelValue( x, y, (rgb >> 16) & 0xFF);
                g.setPixelValue(x, y, (rgb >> 8) & 0xFF);
                b.setPixelValue( x, y, rgb & 0xFF  );

            }
        }

        addChannel("r", r);
        addChannel("g", g);
        addChannel("b", b);

    }


}
