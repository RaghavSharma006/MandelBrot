import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

/**
 * Mandelbrot Set Explorer
 * - Left double-click → Zoom in at clicked point
 * - Right double-click → Zoom out at clicked point
 * - Drag → Pan the Mandelbrot view
 * - Select zoom level
 * - Select Iterations
 * - Get complex coordinates of any points
 * - Generate Julia set of the complex number
 */
public class MandelBrot extends JPanel implements MouseInputListener {

    JTextField realField; //Reference to JFrame realField and imagField
    JTextField imagField;
    JTextField zoomField;
    JTextField zoomLevelField;
    JTextField maxIterationField;

    // Screen dimensions (dynamic from user’s monitor)
    final static int SCREEN_WIDTH;
    final static int SCREEN_HEIGHT;

    // Mandelbrot parameters
    static int maxIterations = 5000;  // max escape-time iterations
    static double zoomFactor = 0.8;   // zoom factor per double-click
    static double zoomed=1;

    // Complex plane boundaries (initial view window)
    double xMin = -2.5;
    double xMax =  2.5;
    double yMin = -2.5;  // Mandelbrot lies roughly in [-2,2] + i[-2,2] but we have taken 2.5
    double yMax =  2.5;  // See Proof in 1.1

    // Buffered image to store computed fractal
    BufferedImage img;

    // Static block to dynamically set display resolution
    static {
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        SCREEN_WIDTH  = screen.width;
        SCREEN_HEIGHT = screen.height;
    }

    // Constructor: initializes image + mouse listeners
    MandelBrot(JTextField real, JTextField imaginary,JTextField zoomField,JTextField maxIterationField,JTextField zoomLevelField) {

        this.realField=real;
        this.imagField=imaginary;
        this.zoomField=zoomField;
        this.maxIterationField=maxIterationField;
        this.zoomLevelField=zoomLevelField;

        img =generate(); // generate Mandelbrot for initial view

        // Register event listeners for interactivity
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    /**
     * Generates Mandelbrot fractal image for current [xMin, xMax] × [yMin, yMax] window.
     * Uses escape-time algorithm: z_{n+1} = z_n^2 + c
     */
    BufferedImage generate() {
        BufferedImage temp= new BufferedImage(SCREEN_WIDTH, SCREEN_HEIGHT, BufferedImage.TYPE_INT_RGB);

        maxIterations=Integer.parseInt(maxIterationField.getText());

        for (int i = 0; i < SCREEN_WIDTH; i++) {
            for (int j = 0; j < SCREEN_HEIGHT; j++) {

                // Convert pixel (i,j) into complex number (c_a + c_b i)
                // Proof in 2.1
                double c_a = xMin + (i / (double) SCREEN_WIDTH) * (xMax - xMin);
                double c_b = yMin + (j / (double) SCREEN_HEIGHT) * (yMax - yMin);

                double z_a = 0, z_b = 0; // z_n = 0 initial
                int numberOfIterations = 0;

                // Equation ( (c_a * c_a) + (c_b * c_b) <= 4 ) proof in 1.2
                if ((c_a * c_a) + (c_b * c_b) > 4) {
                    Color rust = new Color(28, 1, 0); // Reddish-brown fallback
                    temp.setRGB(i, SCREEN_HEIGHT - 1 - j, rust.getRGB());
                    continue;
                }

                // |Z_n|>2 point will reach infinity (See Proof in 1.3).
                // We will use this to check if the point belongs to Mandelbrot set or not.


                // Equation ( (z_a * z_a) + (z_b * z_b) <= 4 ) proof in 1.4
                while ((z_a * z_a) + (z_b * z_b) <= 4 && numberOfIterations < maxIterations) {
                    // Equations proof in 1.5
                    double zn_a = (z_a * z_a) - (z_b * z_b) + (c_a);
                    double zn_b = 2 * (z_a * z_b) + (c_b);

                    z_a = zn_a;
                    z_b = zn_b;
                    numberOfIterations++;
                }

                // Coloring: black if inside Mandelbrot, colorful gradient if outside
                int color = (numberOfIterations == maxIterations)
                        ? 0
                        : Color.HSBtoRGB(numberOfIterations / 256f, 1,
                        numberOfIterations / (numberOfIterations + 8f));

                // Flip vertically because screen coordinates differ from complex plane
                // As Image Buffer left top corner starts from 0,0 but screens bottom left is 0,0 so inverting image
                temp.setRGB(i, SCREEN_HEIGHT - 1 - j, color);

            }
        }
        return temp;
    }

    // Draw image onto JPanel
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(img, 0, 0, null);
    }

    // State flag: avoid overlapping computations
    boolean isGenerating = false;

    /**
     * Mouse clicks:
     * - Double click (left) → Zoom in
     * - Double click (right) → Zoom out
     * - Triple click (left) → Open Julia set for clicked point
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        // Single left-click: show complex number real and imaginary values
        if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 1) {

            // Convert pixel (i,j) into complex number (c_a + c_b i)
            // Proof in 2.1
            double x = xMin + (e.getX() / (double) SCREEN_WIDTH) * (xMax - xMin);
            //double y = yMax - (e.getY() / (double) SCREEN_HEIGHT) * (yMax - yMin); would used it but buffer and jFrame use opposite y so:
            double y = yMax - (e.getY() / (double) SCREEN_HEIGHT) * (yMax - yMin);

            realField.setText(String.valueOf(x)); //Update Text Field in Main Frame
            imagField.setText(String.valueOf(y)); //Update Text Field in Main Frame
        }

        // Double click: zoom in/out
        //Proof in 2.2
        if (e.getClickCount() == 2) {
            if (isGenerating || isDragging) return;
            isGenerating = true;

            // Fractional position of click in window (0.0 = left/top, 1.0 = right/bottom)
            // Ratios with respect to Screen dimensions
            double fx = e.getX() / (double) SCREEN_WIDTH;
            double fy = (SCREEN_HEIGHT-1-e.getY()) / (double) SCREEN_HEIGHT;

            // Find complex-plane coordinates of clicked pixel
            double xCenter = xMin + fx * (xMax - xMin);
            double yCenter = yMin + fy * (yMax - yMin);

            // Decide zoom direction: left = zoom in, right = zoom out
            zoomFactor =1/(Double.parseDouble(zoomField.getText()));
            double scale = (e.getButton() == MouseEvent.BUTTON1) ? zoomFactor : 1.0 / zoomFactor;

            // New window size after zoom
            double newWidth  = (xMax - xMin) * scale;
            double newHeight = (yMax - yMin) * scale;

            // Re-center window around click, preserving relative position (fx,fy)
            xMin = xCenter - (fx * newWidth);
            xMax = xMin + newWidth;
            yMin = yCenter - (fy * newHeight);
            yMax = yMin + newHeight;

            zoomed*=(e.getButton()==MouseEvent.BUTTON1) ? 1/zoomFactor : zoomFactor;
            zoomLevelField.setText(String.valueOf(zoomed));

            img=generate();  // recompute fractal
            repaint();   // redraw
            isGenerating = false;
        }
    }

    // Variables for drag functionality
    double dragStartX, dragStartY;
    boolean isDragging = false;


    // Mouse pressed: start drag
    @Override
    public void mousePressed(MouseEvent e) {
        dragStartX = e.getX();
        dragStartY = e.getY();
        isDragging = true;
    }


    /**
     * Mouse drag → Pan the Mandelbrot view.
     * Converts pixel displacement (dx, dy) into corresponding complex-plane shift,
     * then updates the current window boundaries.
     */
    @Override
    //Proof in 2.3
    public void mouseDragged(MouseEvent e) {
        if (!isDragging || isGenerating) return;

        // Pixel movement
        double dx = dragStartX - e.getX();
        double dy = dragStartY - e.getY();

        // Convert to complex plane shift
        double xCenter = (dx / SCREEN_WIDTH) * (xMax - xMin);
        double yCenter = (dy / SCREEN_HEIGHT) * (yMax - yMin);

        // Shift view window
        xMin += xCenter;
        xMax += xCenter;
        yMin -= yCenter;   // Notice minus because screen y is inverted
        yMax -= yCenter;

        dragStartX = e.getX();
        dragStartY = e.getY();
    }

    // Mouse released: stop drag
    @Override
    public void mouseReleased(MouseEvent e) {
        img=generate();
        repaint();
        isDragging = false;
    }

    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}
    @Override
    public void mouseMoved(MouseEvent e) {}
}
