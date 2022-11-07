import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.DefaultDrawingSupplier;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYBarPainter;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.data.statistics.HistogramDataset;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.image.Raster;
import java.awt.image.RescaleOp;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;


public class Main extends JFrame implements ActionListener {

    private static final DecimalFormat df = new DecimalFormat("0.00");

    // menubar
    static JMenuBar mb;

    // JMenu
    static JMenu x;

    // Menu items
    static JMenuItem m1, m2, m3, m4, m5, m6, m7, m8, m9, m10;
    private static BufferedImage originalImg;
    private static JFrame frame;
    private static JLabel lbl = new JLabel();


    public class Histogram {

        private static final int BINS = 256;
        private final BufferedImage image = originalImg;
        private HistogramDataset dataset;
        private XYBarRenderer renderer;


        private ChartPanel createChartPanel() {
            // dataset
            dataset = new HistogramDataset();
            Raster raster = image.getRaster();
            final int w = image.getWidth();
            final int h = image.getHeight();
            double[] r = new double[w * h];
            r = raster.getSamples(0, 0, w, h, 0, r);
            dataset.addSeries("Red", r, BINS);
            r = raster.getSamples(0, 0, w, h, 1, r);
            dataset.addSeries("Green", r, BINS);
            r = raster.getSamples(0, 0, w, h, 2, r);
            dataset.addSeries("Blue", r, BINS);
            // chart
            JFreeChart chart = ChartFactory.createHistogram("Histogram", "Value",
                    "Count", dataset, PlotOrientation.VERTICAL, true, true, false);
            XYPlot plot = (XYPlot) chart.getPlot();
            renderer = (XYBarRenderer) plot.getRenderer();
            renderer.setBarPainter(new StandardXYBarPainter());
            // translucent red, green & blue
            Paint[] paintArray = {
                    new Color(0x80ff0000, true),
                    new Color(0x8000ff00, true),
                    new Color(0x800000ff, true)
            };
            plot.setDrawingSupplier(new DefaultDrawingSupplier(
                    paintArray,
                    DefaultDrawingSupplier.DEFAULT_FILL_PAINT_SEQUENCE,
                    DefaultDrawingSupplier.DEFAULT_OUTLINE_PAINT_SEQUENCE,
                    DefaultDrawingSupplier.DEFAULT_STROKE_SEQUENCE,
                    DefaultDrawingSupplier.DEFAULT_OUTLINE_STROKE_SEQUENCE,
                    DefaultDrawingSupplier.DEFAULT_SHAPE_SEQUENCE));
            ChartPanel panel = new ChartPanel(chart);
            panel.setMouseWheelEnabled(true);
            return panel;
        }

        private JPanel createControlPanel() {
            JPanel panel = new JPanel();
            panel.add(new JCheckBox(new VisibleAction(0)));
            panel.add(new JCheckBox(new VisibleAction(1)));
            panel.add(new JCheckBox(new VisibleAction(2)));
            return panel;
        }

        private class VisibleAction extends AbstractAction {

            private final int i;

            public VisibleAction(int i) {
                this.i = i;
                this.putValue(NAME, (String) dataset.getSeriesKey(i));
                this.putValue(SELECTED_KEY, true);
                renderer.setSeriesVisible(i, true);
            }

            @Override
            public void actionPerformed(ActionEvent e) {
                renderer.setSeriesVisible(i, !renderer.getSeriesVisible(i));
            }
        }

        private void display() {
            JFrame f = new JFrame("Histogram");
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.add(createChartPanel());
            f.add(createControlPanel(), BorderLayout.SOUTH);
            f.add(new JLabel(), BorderLayout.WEST);
            f.pack();
            f.setLocationRelativeTo(null);
            f.setVisible(true);
        }
    }


    public static void main(String[] args) throws IOException {

        // create an object of the class
        Main m = new Main();

        // write your code here
        originalImg = ImageIO.read(
                new File("src/main/resources/Starry_Night.jpg"));
        ImageIcon icon = new ImageIcon(originalImg);
        frame = new JFrame();


        // create a menubar
        mb = new JMenuBar();

        // create a menu
        x = new JMenu("Menu");

        // create menuitems
        m1 = new JMenuItem("Rotate");
        m2 = new JMenuItem("Crop");
        m3 = new JMenuItem("Brightness and contrast");
        m4 = new JMenuItem("Black and White");
        m5 = new JMenuItem("RGB matrix");
        m6 = new JMenuItem("Histogram");
        m7 = new JMenuItem("Change pixels");
        m8 = new JMenuItem("2 photografies");
        m9 = new JMenuItem("Whitest and Darkest");

        // add menu items to menu
        x.add(m1);
        x.add(m2);
        x.add(m3);
        x.add(m4);
        x.add(m5);
        x.add(m6);
        x.add(m7);
        x.add(m8);
        x.add(m9);

        // add menu to menu bar
        mb.add(x);

        // add menubar to frame
        frame.setJMenuBar(mb);

        m1.addActionListener(m);
        m2.addActionListener(m);
        m3.addActionListener(m);
        m4.addActionListener(m);
        m5.addActionListener(m);
        m6.addActionListener(m);
        m7.addActionListener(m);
        m8.addActionListener(m);
        m9.addActionListener(m);

        frame.setLayout(new FlowLayout());
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension screenSize = tk.getScreenSize();
        frame.setSize(screenSize.width, screenSize.height);

//        frame.setSize(200, 300);
        lbl.setIcon(icon);
        frame.add(lbl);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String s = e.getActionCommand();

        // set the label to the menuItem that is selected

        if (s.equals("Rotate")) {
            // The required drawing location
            int width = originalImg.getWidth();
            int height = originalImg.getHeight();

            // Creating a new buffered image
            BufferedImage newImage = new BufferedImage(
                    originalImg.getWidth(), originalImg.getHeight(), originalImg.getType());

            // creating Graphics in buffered image
            Graphics2D g2 = newImage.createGraphics();

            g2.rotate(Math.toRadians(90), width / 2,
                    height / 2);
            g2.drawImage(originalImg, null, 0, 0);

            originalImg = newImage;

            lbl.removeAll();
            frame.remove(lbl);

            ImageIcon icon = new ImageIcon(originalImg);
            lbl = new JLabel();
            lbl.setIcon(icon);
            frame.add(lbl);
            SwingUtilities.updateComponentTreeUI(frame);

        } else if (s.equals("Crop")) {


            JFrame newframe = new JFrame();
            newframe.setSize(400, 400);
            newframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            Container container = newframe.getContentPane();
            container.setLayout(new FlowLayout());

            JTextField x = new JTextField();
            JTextField y = new JTextField();
            JTextField w = new JTextField();
            JTextField h = new JTextField();

            x.setPreferredSize(new Dimension(50, 25));
            y.setPreferredSize(new Dimension(50, 25));
            w.setPreferredSize(new Dimension(50, 25));
            h.setPreferredSize(new Dimension(50, 25));

            JLabel label = new JLabel("X x Y x W x H");

            JButton okButton = new JButton("OK");
            okButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    String xString = x.getText();
                    Integer x = Integer.valueOf(xString);
                    String yString = y.getText();
                    Integer y = Integer.valueOf(yString);
                    String wString = w.getText();
                    Integer w = Integer.valueOf(wString);
                    String hString = h.getText();
                    Integer h = Integer.valueOf(hString);

                    BufferedImage SubImg
                            = originalImg.getSubimage(x, y, w, h);

                    ImageIcon icon = new ImageIcon(SubImg);
                    lbl.setIcon(icon);
                    newframe.add(lbl);
                    SwingUtilities.updateComponentTreeUI(newframe);
                }
            });

            container.add(x);
            container.add(y);
            container.add(w);
            container.add(h);
            container.add(okButton);
            container.add(label);

            newframe.setVisible(true);

        } else if (s.equals("Brightness and contrast")) {

            RescaleOp rescaleOp = new RescaleOp(1.2f, 15, null);

            rescaleOp.filter(originalImg, originalImg);
            ImageIcon icon = new ImageIcon(originalImg);
            lbl.setIcon(icon);
            frame.add(lbl);
            frame.setVisible(true);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        } else if (s.equals("Black and White")) {

            ColorConvertOp op = new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null);
            op.filter(originalImg, originalImg);

            ImageIcon icon = new ImageIcon(originalImg);

            lbl.setIcon(icon);
            frame.add(lbl);
            frame.setVisible(true);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        } else if (s.equals("RGB matrix")) {

            BufferedImage SubImg
                    = originalImg.getSubimage(50, 50, 20, 20);


            int[][] R = new int[SubImg.getWidth()][SubImg.getHeight()];
            int[][] G = new int[SubImg.getWidth()][SubImg.getHeight()];
            int[][] B = new int[SubImg.getWidth()][SubImg.getHeight()];

            for (int r = 0; r < SubImg.getWidth(); r++) {
                System.out.println("  [R  G  B] ");

                for (int c = 0; c < SubImg.getHeight(); c++) {
                    //Uses the Java color class to do the conversion from int to RGB
                    Color temp = new Color(SubImg.getRGB(r, c));
                    R[r][c] = temp.getRed();
                    G[r][c] = temp.getGreen();
                    B[r][c] = temp.getBlue();

                    System.out.println("[ " + R[r][c] + " " + G[r][c] + " " + B[r][c] + " ]");


                    SubImg.setRGB(r, c, Color.RED.getRGB());
                    ImageIcon icon = new ImageIcon(originalImg);
                    lbl.setIcon(icon);
                    frame.add(lbl);
                }
                System.out.println("-----------------------");
                SwingUtilities.updateComponentTreeUI(frame);
            }


        } else if (s.equals("Histogram")) {

            EventQueue.invokeLater(() -> {
                new Histogram().display();
            });

        } else if (s.equals("Change pixels")) {

            JFrame newframe = new JFrame();
            newframe.setSize(400, 400);
            newframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            Container container = newframe.getContentPane();
            container.setLayout(new FlowLayout());

            JTextField textField = new JTextField();
            JTextField textField2 = new JTextField();
            textField.setPreferredSize(new Dimension(150, 25));

            textField2.setPreferredSize(new Dimension(150, 25));

            newframe.add(textField2);

            JLabel label = new JLabel("WIDTH x HEIGHT");

            JButton okButton = new JButton("OK");
            okButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    String input = textField.getText();

                    Integer height = Integer.valueOf(input);

                    String input2 = textField2.getText();
                    Integer width = Integer.valueOf(input2);

                    Image image = originalImg.getScaledInstance(width, height, Image.SCALE_DEFAULT);

                    lbl = new JLabel();

                    ImageIcon icon = new ImageIcon(image);
                    lbl.setIcon(icon);
                    newframe.add(lbl);
                    SwingUtilities.updateComponentTreeUI(newframe);
                }
            });

            container.add(textField);
            container.add(okButton);
            container.add(label);

            newframe.setVisible(true);

        }else if (s.equals("2 photografies")){
            
            BufferedImage image = null;
            Image original = originalImg.getScaledInstance(500, 300, Image.SCALE_DEFAULT);
            JLabel label = new JLabel();

            JFrame newframe = new JFrame();
            newframe.setLayout(new FlowLayout());
            Toolkit tk = Toolkit.getDefaultToolkit();
            Dimension screenSize = tk.getScreenSize();
            newframe.setSize(screenSize.width, screenSize.height);
            newframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            try {
                image = ImageIO.read(
                        new File("src/main/resources/black.jpg"));
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }

            ImageIcon icon = new ImageIcon(image);
            label.setIcon(icon);
            newframe.add(label);
            newframe.setVisible(true);

            Graphics g = image.getGraphics();
            g.drawImage(original, 100,100,null);
            g.dispose();
            label.repaint();

        } else if (s.equals("Whitest and Darkest")) {

            Color black = new Color(0x80000000, true);
            Color white = new Color(0x80ffffff, true);

            BufferedImage SubImg
                    = originalImg.getSubimage(600 , 40, 200, 200);


            int[][] R = new int[SubImg.getWidth()][SubImg.getHeight()];
            int[][] G = new int[SubImg.getWidth()][SubImg.getHeight()];
            int[][] B = new int[SubImg.getWidth()][SubImg.getHeight()];

            for (int r = 0; r < SubImg.getWidth(); r++) {
                System.out.println("  [R  G  B] ");

                for (int c = 0; c < SubImg.getHeight(); c++) {
                    //Uses the Java color class to do the conversion from int to RGB
                    Color temp = new Color(SubImg.getRGB(r, c));
                    R[r][c] = temp.getRed();
                    G[r][c] = temp.getGreen();
                    B[r][c] = temp.getBlue();

                    double Rdouble =R[r][c];
                    double Gdouble =G[r][c];
                    double Bdouble =B[r][c];

                    double  Rcolor =  Rdouble /255;
                    double  Gcolor =  Gdouble /255;
                    double  Bcolor =  Bdouble /255;

                    double  sum = (Rcolor + Gcolor + Bcolor) / 3;

                    System.out.println("[ " + R[r][c] + " " + G[r][c] + " " + B[r][c] + " ]");
                    System.out.println("White balance: " +sum);

                    if (sum > 0.5) {
                        //je svetle
                        SubImg.setRGB(r, c, white.getRGB());
                        ImageIcon icon = new ImageIcon(originalImg);
                        lbl.setIcon(icon);
                        frame.add(lbl);
                        SwingUtilities.updateComponentTreeUI(frame);

                    } else {
                        //je tmave
                        SubImg.setRGB(r, c, black.getRGB());
                        ImageIcon icon = new ImageIcon(originalImg);
                        lbl.setIcon(icon);
                        frame.add(lbl);
                        SwingUtilities.updateComponentTreeUI(frame);
                    }

                }
                System.out.println("-----------------------");
                SwingUtilities.updateComponentTreeUI(frame);
            }


        }

    }


}



