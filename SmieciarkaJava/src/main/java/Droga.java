import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;

public class Droga {




    boolean createBoard = false;

    public Droga()
    {

    }





    public void draw(Graphics2D g)
    {

        g.setColor(Color.LIGHT_GRAY);


            for (int z = 0; z < 1200; z = z + 20) {
                g.drawLine(z, 0, z, 1000);
            }

            for (int z = 0; z < 1000; z = z + 20) {
            g.drawLine(0, z, 1200, z);
            }



        g.setColor(Color.GRAY);
        g.setStroke(new BasicStroke(60));


  /*  /// pionowo
        g.drawLine(0,100,1200,100);

        g.drawLine(100, 700, 800, 700);





    /// pion
        g.drawLine(100,100,100,700);

        g.drawLine(800,700,800,100); */

    }



}
