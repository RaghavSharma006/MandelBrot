import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class MandelBrot extends JPanel implements MouseWheelListener,MouseInputListener {

    final static int  SCREEN_WIDTH;
    final  static int SCREEN_HEIGHT;
    final static int MAX_ITERATIONS =1000;
    final static double ZOOM_FACTOR=0.8;

    double xMin=-2.5;
    double xMax=2.5;
    double yMin=-2.5;
    double yMax=2.5;

    BufferedImage img;

    static
    {
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        SCREEN_WIDTH= screen.width;
        SCREEN_HEIGHT = screen.height;
    }
    MandelBrot()
    {
        img=new BufferedImage(SCREEN_WIDTH,SCREEN_HEIGHT,BufferedImage.TYPE_INT_RGB);
        generate();
        addMouseWheelListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    void generate()
    {
        for(int i = 0; i< SCREEN_WIDTH; i++)
        {
            for (int j = 0; j < SCREEN_HEIGHT; j++) {

                double c_a = xMin + (i/(double)SCREEN_WIDTH)*(xMax-xMin);
                double c_b = yMin + (j/(double)SCREEN_HEIGHT)*(yMax-yMin);

                double z_a=0,z_b=0;
                int numberOfIterations=0;

                if((c_a*c_a) + (c_b*c_b) >4) {
                    Color rust = new Color(70, 65, 14); // Reddish-rust tone
                    img.setRGB(i, j, rust.getRGB());
                    continue;
                }

                while( (z_a*z_a) + (z_b*z_b) <=4 && numberOfIterations < MAX_ITERATIONS)
                {
                    double zn_a=(z_a*z_a)-(z_b*z_b)+(c_a);
                    double zn_b=2*(z_a*z_b)+(c_b);

                    z_a=zn_a;
                    z_b=zn_b;
                    numberOfIterations++;
                }
                int color = numberOfIterations == MAX_ITERATIONS ? 0 : Color.HSBtoRGB(numberOfIterations / 256f, 1, numberOfIterations/ (numberOfIterations + 8f));
                img.setRGB(i,SCREEN_HEIGHT-1-j, color);
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(img, 0, 0, null);
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {

    }


    boolean isGenerating=false;
    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount()==3) {
            System.out.println("triple");

            JFrame juliaFrame = new JFrame();
            Julia juliaPanel = new Julia(e.getX(),e.getY(),0);
            juliaFrame.add(juliaPanel);
            juliaFrame.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
            juliaFrame.setTitle("Julia Set for "+ juliaPanel.c_a+"+"+juliaPanel.c_b+"i");
            juliaFrame.setVisible(true);
            juliaFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            System.out.println("END");
        }
        if(e.getClickCount()==2)
        {
            if (isGenerating || isDragging) return;
            else isGenerating = true;

            double scale;
            if(e.getButton()== MouseEvent.BUTTON1)  scale = ZOOM_FACTOR;
            else scale = 1.0 / ZOOM_FACTOR;

            double xCenter= xMin + (e.getX() / (double) SCREEN_WIDTH) * (xMax - xMin);
            double yCenter= yMin + (e.getY() / (double) SCREEN_HEIGHT) * (yMax - yMin);

            double fx=e.getX()/(double)SCREEN_WIDTH;
            double fy=e.getY()/(double)SCREEN_HEIGHT;

            double newWidth  = (xMax - xMin) * scale;
            double newHeight = (yMax - yMin) * scale;


            xMin = xCenter - (fx*newWidth);
            xMax = xMin + newWidth;

            yMin = yCenter - (fy*newHeight);
            yMax = yMin + newHeight;

            generate();
            repaint();
            isGenerating = false;
        }
    }

    double dragStartX, dragStartY;
    boolean isDragging = false;
    @Override
    public void mousePressed(MouseEvent e) {
        dragStartX = e.getX();
        dragStartY = e.getY();
        isDragging = true;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        isDragging = false;
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (!isDragging || isGenerating) return;
        double dx = dragStartX - e.getX();
        double dy = dragStartY - e.getY();

        double xCenter= (dx/SCREEN_WIDTH) * (xMax-xMin);
        double yCenter= (dy/SCREEN_HEIGHT) * (yMax-yMin);

        xMin += xCenter;
        xMax += xCenter;
        yMin -= yCenter;
        yMax -= yCenter;

        dragStartX = e.getX();
        dragStartY = e.getY();

        generate();
        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }
}
