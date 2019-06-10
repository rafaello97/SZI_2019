import javax.swing.*;
import java.awt.*;

public class Frame extends JFrame {


    static int width;
    static int height;

    public Frame()
    {
        setTitle("Image");
        setSize(700,700);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);

        setVisible(true);



    }




    public int getW() {          //getter
        return width;
    }

    public void setW(Integer w) { //setter
        width = w;
    }


    public static Integer getH() {          //getter
        return height;
    }

    public void setH(Integer w) { //setter
        height = w;
    }


}
