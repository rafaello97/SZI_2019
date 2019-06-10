import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Swiat extends JFrame
{



    //początkowe pozycje śmieciarki - prosta animacja
    int xSmieciarka=0;
    int ySmieciarka=75;

    ArrayList<String> files = new ArrayList<String>();

    //tablica przechowujace informacje o danej kratke
  public static int[][] world = new int[60][40];
    ArrayList<Integer> zablokowaneX = new ArrayList<>();
    ArrayList<Integer> zablokowaneY = new ArrayList<>();
    ArrayList<Integer> zablokowaneXdrzewa = new ArrayList<>();
    ArrayList<Integer> zablokowaneYdrzewa = new ArrayList<>();
    ArrayList<Integer> przebyteX = new ArrayList<>();
    ArrayList<Integer> przebyteY = new ArrayList<>();

    List<Integer> xSorted;
    List<Integer> ySorted;
    // zmienna sprawdzająca kierunek ruchu śmieciarki animacji początkowej
    boolean goLeft = false;




    // zmienne .png
    private BufferedImage smieciarka_prawo;
    private BufferedImage smieciarka_lewo;
    private BufferedImage smieciarka_dol;
    private BufferedImage smieciarka_gora;
    private BufferedImage wysypisko;
    private BufferedImage arrow;

    private BufferedImage plastik;
    private BufferedImage szklo;
    private BufferedImage aluminium;
    private BufferedImage papier;

    private BufferedImage drzewo_1;
    private BufferedImage drzewo_2;
    static private int ILOSC_RZECZY = 1000;


    Pozycje poz = new Pozycje();
    Node initialNode;
    //= new Node(0, 39);
    Node finalNode;
    //= new Node(51, 5);
    int rows = 60;
    int cols = 40;
    List<Node> path;
    Node node;

    AStar aStar;


    public Swiat() throws InterruptedException, IOException {
        super("Sztuczna Inteligencja - projekt");

        try {


            smieciarka_prawo = ImageIO.read(new File("resources/garbage_right.png"));
            smieciarka_lewo = ImageIO.read(new File("resources/garbage_left.png"));
            smieciarka_dol = ImageIO.read(new File("resources/garbage_down.png"));
            smieciarka_gora = ImageIO.read(new File("resources/garbage_up.png"));
            wysypisko = ImageIO.read(new File("resources/landfill.png"));
            arrow = ImageIO.read(new File("resources/arrow.png"));
            plastik = ImageIO.read(new File("resources/plastic.jpg"));
            szklo = ImageIO.read(new File("resources/szklo.jpg"));
            aluminium = ImageIO.read(new File("resources/aluminium.jpg"));
            papier = ImageIO.read(new File("resources/papier.jpg"));
            drzewo_1 = ImageIO.read(new File("resources/tree_1.jpg"));
            drzewo_2 = ImageIO.read(new File("resources/tree_2.jpg"));

        } catch (IOException e) {
            e.printStackTrace();
        }


        setContentPane(new Init());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 1000);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
        setVisible(true);
        setResizable(false);

        gerenowanieSwiata();

        Thread.sleep(3000);
        idz(0, 39);

    }


    int xLast, yLast;
    boolean flag = false;

    public void idzWysypisko() throws InterruptedException, IOException {

        initialNode = new Node(node.getRow(), node.getCol());

        finalNode = new Node(51, 5);
        aStar = new AStar(rows, cols, initialNode, finalNode);
        aStar.setBlocks(zablokowaneXdrzewa, zablokowaneYdrzewa);
        path = aStar.findPath();

        for (int i = 0; i < path.size(); i++) {

            node = path.get(i);
            Thread.sleep(150);
            repaint();

            System.out.println("X na: " + node.getRow() + " Y na: " + node.getCol());
        }


        flag = true;


    }




    public void idz(int x, int y) throws InterruptedException, IOException {


        ArrayList<Integer> list = new ArrayList<>(zwrocNajblizszaPrzeszkode(x,y));
        initialNode = new Node(x, y);

        try {
            finalNode = new Node(list.get(0), list.get(1));
            xLast = x;
            yLast = y;
            aStar = new AStar(rows, cols, initialNode, finalNode);

            aStar.setBlocks(zablokowaneXdrzewa, zablokowaneYdrzewa);
            // aStar.setBlocks(zablokowaneX, zablokowaneY);

            path = aStar.findPath();

            for (int i = 0; i < path.size(); i++) {

                node = path.get(i);
                Thread.sleep(150);
                repaint();

                System.out.println("X na: " + node.getRow() + " Y na: " + node.getCol());
            }


            zablokowaneX.remove(new Integer(node.getRow()));
            zablokowaneY.remove(new Integer(node.getCol()));

            poz.setZablokowaneX(zablokowaneX);
            poz.setZablokowaneY(zablokowaneY);


            String smiec = getSmiec(list.get(0), list.get(1));
            File dir = new File("tensorflow_siec/photos/");
            getFileNames(dir);
            Random rand = new Random();
            String next = files.get(rand.nextInt(files.size()));
            System.out.println(next);
            Image.predict(dir + "/" + next);


            File dirN = new File("numery_domow/photos/");
            File[] files = dirN.listFiles();

            Random rando = new Random();

            File file = files[rando.nextInt(files.length)];
            Image.predictNumber(file.toString());


            //Image.predict("resources/"+smiec);
            //  Image.predict("/Users/dymitrsoltysiak/Desktop/plastic.jpg");

            idz(node.getRow(), node.getCol());
        }catch (IndexOutOfBoundsException i)
        {
            System.out.println("HI");
            idzWysypisko();
        }

    }


    public void gerenowanieSwiata() {

        for (int i = 0; i < 60; i++) {
            for (int j = 0; j < 40; j++) {
                world[i][j] = 0;
            }
        }

        ArrayList<Integer> x = new ArrayList<>();
        ArrayList<Integer> y = new ArrayList<>();

        for (int i = 0; i < ILOSC_RZECZY; i++) {

            int randomX = ThreadLocalRandom.current().nextInt(0, 59 + 1);
            int randomY = ThreadLocalRandom.current().nextInt(0, 39 + 1);

            if (!x.contains(randomX) && !y.contains(randomY)) {
                x.add(randomX);
                y.add(randomY);

                int random = ThreadLocalRandom.current().nextInt(0, 6 + 1);
                world[randomX][randomY] = random;
                System.out.println("X: " + randomX + " Y: " + randomY + " -" + " RAN: " + world[randomX][randomY]);
                if (world[randomX][randomY] != 0) {
                    if (world[randomX][randomY] == 5 || world[randomX][randomY] == 6) {
                        System.out.println("DRZEWO");
                        zablokowaneXdrzewa.add(randomX);
                        zablokowaneYdrzewa.add(randomY);
                    } else {
                        System.out.println("BLOK");
                        zablokowaneX.add(randomX);
                        zablokowaneY.add(randomY);
                    }

                }

            } else {
                continue;
            }

        }

        poz.setZablokowaneX(zablokowaneX);
        poz.setZablokowaneY(zablokowaneY);

    }

     class Init extends JPanel
     {
        Droga droga = new Droga();


        @Override
        public void paintComponent(Graphics g)
        {
            super.paintComponent(g);



            g.setColor(Color.WHITE);
            g.fillRect( 0,0,1200, 1000);

            droga.draw((Graphics2D) g);



            g.setColor(Color.BLACK);

            g.setFont(new Font("Courier New", Font.BOLD, 22));
            g.drawString("WYSYPISKO", 1080, 50);
            g.drawImage(arrow, 1050, 85, this);
            g.drawImage(wysypisko, 1100, 50, this);


            g.setFont(new Font("Courier New", Font.BOLD, 18));
            g.drawString("Smieci zebranych: " + Info.getSmieci(), 5, 30);



            g.setColor(Color.BLUE);
            for (int i=0; i<przebyteX.size(); i++)
            {
                g.fillRect(przebyteX.get(i)*20,przebyteY.get(i)*20,20,20);
            }




         //**** generowanie śmieci oraz drzew ****//
            /* poczatek */

            for (int i=0; i<60; i++)
            {
                for (int j=0; j<40; j++)
                {




                    if (world[i][j] == 1 )
                    {


                        g.setColor(Color.LIGHT_GRAY);
                        g.fillRect(i*20,j*20,20,20);
                     //   g.drawImage(plastik.getScaledInstance(30,30,200), i*20, j*20, this);
                        g.setColor(Color.BLACK);
                        g.setFont(new Font("Courier New", Font.BOLD, 10));
                        g.drawString((i+1) + "," + (j+1), i*20  ,j*20);
                        g.setFont(new Font("Courier New", Font.BOLD, 22));

                    }
                    else if (world[i][j] == 2)
                    {
                        g.setColor(Color.LIGHT_GRAY);
                        g.fillRect(i*20,j*20,20,20);
                    //    g.drawImage(aluminium.getScaledInstance(30,30,200), i*20, j*20, this);
                        g.setColor(Color.BLACK);
                        g.setFont(new Font("Courier New", Font.BOLD, 10));
                        g.drawString((i+1) + "," + (j+1), i*20  ,j*20);
                        g.setFont(new Font("Courier New", Font.BOLD, 22));
                    }
                    else if (world[i][j] == 3)
                    {
                        g.setColor(Color.LIGHT_GRAY);
                        g.fillRect(i*20,j*20,20,20);
                   //     g.drawImage(szklo.getScaledInstance(30,30,200), i*20, j*20, this);
                        g.setColor(Color.BLACK);
                        g.setFont(new Font("Courier New", Font.BOLD, 10));
                        g.drawString((i+1) + "," + (j+1), i*20  ,j*20);
                        g.setFont(new Font("Courier New", Font.BOLD, 22));
                    }
                    else if (world[i][j] == 4)
                    {
                        g.setColor(Color.LIGHT_GRAY);
                        g.fillRect(i*20,j*20,20,20);
                     //   g.drawImage(papier.getScaledInstance(30,30,200), i*20, j*20, this);
                        g.setColor(Color.BLACK);
                        g.setFont(new Font("Courier New", Font.BOLD, 10));
                        g.drawString((i+1) + "," + (j+1), i*20  ,j*20);
                        g.setFont(new Font("Courier New", Font.BOLD, 22));
                    }
                    else if (world[i][j] == 5)
                    {
                        g.setColor(Color.LIGHT_GRAY);
                        g.fillRect(i*20,j*20,20,20);
                        g.drawImage(drzewo_1, i*20, j*20, this);
                        g.setColor(Color.BLACK);
                        g.setFont(new Font("Courier New", Font.BOLD, 10));
                        g.drawString((i+1) + "," + (j+1), i*20  ,j*20);
                        g.setFont(new Font("Courier New", Font.BOLD, 22));
                    }
                    else if (world[i][j] == 6)
                    {
                        g.setColor(Color.LIGHT_GRAY);
                        g.fillRect(i*20,j*20,20,20);
                        g.drawImage(drzewo_2, i*20, j*20, this);
                        g.setColor(Color.BLACK);
                        g.setFont(new Font("Courier New", Font.BOLD, 10));
                        g.drawString((i+1) + "," + (j+1), i*20  ,j*20);
                        g.setFont(new Font("Courier New", Font.BOLD, 22));
                    }


                }
            }

                g.setColor(Color.LIGHT_GRAY);
                g.fillRect(node.getRow()*20,node.getCol()*20,20,20);
                g.setColor(Color.BLACK);
                g.drawImage(smieciarka_prawo, node.getRow()*20, node.getCol()*20, this);
                g.setFont(new Font("Courier New", Font.BOLD, 10));
                g.drawString((node.getRow()+1) + "," + (node.getCol()+1), node.getRow()*20  ,node.getCol()*20);


            przebyteX.add(node.getRow());
            przebyteY.add(node.getCol());

            g.setColor(Color.GREEN);
            g.fillRect(0*20,39*20,20,20);
            g.setColor(Color.RED);
            g.fillRect(51*20,5*20,20,20);


        }
    }



    public static String getSmiec(int i, int j)
    {
        String rodzaj = null;
        if (world[i][j] == 1)
        {
            rodzaj = "plastic.jpg";
        }
        else if (world[i][j] == 2)
        {
            rodzaj = "aluminium.jpg";
        }
        else if (world[i][j] == 3)
        {
            rodzaj = "szklo.jpg";
        }
        else if (world[i][j] == 4)
        {
            rodzaj = "papier.jpg";
        }
        else if (world[i][j] == 5)
        {
            rodzaj = "tree_1.jpg";
        }
        else
        {
            rodzaj = "tree_2.jpg";
        }

        return rodzaj;
    }

    public ArrayList<Integer> zwrocNajblizszaPrzeszkode(int xSmieciarki, int ySmieciarki) throws IOException, InterruptedException {

        ArrayList<Integer> zwracanaLista = new ArrayList<>();
        zwracanaLista.clear();


        xSorted = new ArrayList<>(poz.getZablokowaneX());
        ySorted = new ArrayList<>(poz.getZablokowaneY());

        ArrayList<Integer> odleglosciElementow = new ArrayList<>();
        for (int i = 0; i < zablokowaneX.size(); i++) {

            float odleglosc = (float) Math.sqrt(
                    Math.pow(xSmieciarki - zablokowaneX.get(i), 2) +
                            Math.pow(ySmieciarki - zablokowaneY.get(i), 2));
            odleglosciElementow.add((int) odleglosc);

        }

        try {
            int minIndex = odleglosciElementow.indexOf(Collections.min(odleglosciElementow));


            int najblizszyOdleglosc = odleglosciElementow.get(minIndex);
            int pozycjaXNajblizszy = xSorted.get(minIndex);
            int pozycjaYNajblizszy = ySorted.get(minIndex);

            System.out.println("X: " + pozycjaXNajblizszy + " | Y: " + pozycjaYNajblizszy + " | E: " + najblizszyOdleglosc);

            zwracanaLista.add(0, pozycjaXNajblizszy);
            zwracanaLista.add(1, pozycjaYNajblizszy);
            zwracanaLista.add(2, najblizszyOdleglosc);

            }catch (NoSuchElementException ex) {


                idzWysypisko();

            }


        return zwracanaLista;

    }





    public final class Pozycje {
        public ArrayList<Integer> listX = new ArrayList<>();
        public ArrayList<Integer> listY = new ArrayList<>();

        public ArrayList<Integer> getZablokowaneX() {
            return listX;
        }

        public void setZablokowaneX(ArrayList<Integer> list) {
            this.listX = list;
        }

        public ArrayList<Integer> getZablokowaneY() {
            return listY;
        }

        public void setZablokowaneY(ArrayList<Integer> list) {
            this.listY = list;
        }


    }


    public static final class Info {
            static int smieciAll = 0;
            int plastik;
            int papier;
            int szklo;
            int aluminium;

        public static int getSmieci() {
            return smieciAll;
        }

        public static void setSmieci(int smieci) {
            smieciAll = smieci;
        }


    }

    private  void getFileNames(File curDir) {

        File[] filesList = curDir.listFiles();
        for (File f : filesList) {
            if (f.isDirectory())
                getFileNames(f);
            if (f.isFile()) {
                files.add(f.getName());
            }
        }
    }


    public static void main(String args[]) throws InterruptedException, IOException {
        new Swiat();
    }
}