package cosy.bv.converter;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class GrayscaleImageDisplay extends JPanel{

    private BufferedImage image;

    public GrayscaleImageDisplay() {

        image = new BufferedImage( 256, 256, BufferedImage.TYPE_BYTE_GRAY);

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, null); // see javadoc for more info on the parameters
    }


    public void setChannel(Channel c) {

        Object channel[][] = c.toArray();


        for(int x = 0; x < channel.length; x++) {
            for(int y = 0; y < channel[x].length; y++) {
                Integer rgb = (Integer)channel[x][y]<<16 | (Integer)channel[x][y] << 8 | (Integer)channel[x][y];
                image.setRGB(x, y, rgb);
            }
        }


        File outputfile = new File("gray.jpg");
        try {
            ImageIO.write(image, "jpg", outputfile);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}