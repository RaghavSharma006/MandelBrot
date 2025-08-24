import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

/**
 * Mandelbrot Set Explorer
 * - Left double-click → Zoom in at clicked point
 * - Right double-click → Zoom out at clicked point
 * - Triple left-click → Open Julia set corresponding to clicked complex points
 * - Drag → Pan the Mandelbrot view
 */
public class MandelBrot extends JPanel implements MouseWheelListener, MouseInputListener {

    // Screen dimensions (dynamic from user’s monitor)
    final static int SCREEN_WIDTH;
    final static int SCREEN_HEIGHT;

    // Mandelbrot parameters
    final static int MAX_ITERATIONS = 1000;  // max escape-time iterations
    final static double ZOOM_FACTOR = 0.8;   // zoom factor per double-click

    // Complex plane boundaries (initial view window)
    double xMin = -2.5;
    double xMax =  2.5;
    double yMin = -2.5;  // Mandelbrot lies roughly in [-2,2] + i[-2,2] but we have taken 2.5
    double yMax =  2.5;

    // Buffered image to store computed fractal
    BufferedImage img;

    // Static block to dynamically set display resolution
    static {
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        SCREEN_WIDTH  = screen.width;
        SCREEN_HEIGHT = screen.height;
    }

    // Constructor: initializes image + mouse listeners
    MandelBrot() {
        img = new BufferedImage(SCREEN_WIDTH, SCREEN_HEIGHT, BufferedImage.TYPE_INT_RGB);
        generate(); // generate Mandelbrot for initial view

        // Register event listeners for interactivity
        addMouseWheelListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    /**
     * Generates Mandelbrot fractal image for current [xMin, xMax] × [yMin, yMax] window.
     * Uses escape-time algorithm: z_{n+1} = z_n^2 + c
     */
    void generate() {
        for (int i = 0; i < SCREEN_WIDTH; i++) {
            for (int j = 0; j < SCREEN_HEIGHT; j++) {

                // Convert pixel (i,j) into complex number (c_a + c_b i)
                double c_a = xMin + (i / (double) SCREEN_WIDTH) * (xMax - xMin);
                double c_b = yMin + (j / (double) SCREEN_HEIGHT) * (yMax - yMin);

                double z_a = 0, z_b = 0; // z = 0 initial
                int numberOfIterations = 0;

                // Optimization: points outside main circle |c| > 2 cannot belong to Mandelbrot
                if ((c_a * c_a) + (c_b * c_b) > 4) {
                    Color rust = new Color(70, 65, 14); // Reddish-brown fallback
                    img.setRGB(i, j, rust.getRGB());
                    continue;
                }

                // Escape-time iteration: stop if |z| > 2 or max iterations reached
                while ((z_a * z_a) + (z_b * z_b) <= 4 && numberOfIterations < MAX_ITERATIONS) {
                    double zn_a = (z_a * z_a) - (z_b * z_b) + (c_a);
                    double zn_b = 2 * (z_a * z_b) + (c_b);

                    z_a = zn_a;
                    z_b = zn_b;
                    numberOfIterations++;
                }

                // Coloring: black if inside Mandelbrot, colorful gradient if outside
                int color = (numberOfIterations == MAX_ITERATIONS)
                        ? 0
                        : Color.HSBtoRGB(numberOfIterations / 256f, 1,
                        numberOfIterations / (numberOfIterations + 8f));

                // Flip vertically because screen coordinates differ from complex plane
                img.setRGB(i, SCREEN_HEIGHT - 1 - j, color);
            }
        }
    }

    // Draw image onto JPanel
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(img, 0, 0, null);
    }

    // Currently unused (mouse wheel zoom could be added here)
    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {}

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
        // Triple left-click: show Julia set for clicked c
        if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 3) {
            System.out.println("Triple click → Julia Set");

            JFrame juliaFrame = new JFrame();
            Julia juliaPanel = new Julia(e.getX(), e.getY(), 0);
            juliaFrame.add(juliaPanel);
            juliaFrame.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
            juliaFrame.setTitle("Julia Set for " + juliaPanel.c_a + " + " + juliaPanel.c_b + "i");
            juliaFrame.setVisible(true);
            juliaFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        }

        // Double click: zoom in/out
        if (e.getClickCount() == 2) {
            if (isGenerating || isDragging) return;
            else isGenerating = true;

            // Decide zoom direction: left = zoom in, right = zoom out
            double scale = (e.getButton() == MouseEvent.BUTTON1) ? ZOOM_FACTOR : 1.0 / ZOOM_FACTOR;

            // Find complex-plane coordinates of clicked pixel
            double xCenter = xMin + (e.getX() / (double) SCREEN_WIDTH) * (xMax - xMin);
            double yCenter = yMin + (e.getY() / (double) SCREEN_HEIGHT) * (yMax - yMin);

            // Fractional position of click in window (0.0 = left/top, 1.0 = right/bottom)
            double fx = e.getX() / (double) SCREEN_WIDTH;
            double fy = e.getY() / (double) SCREEN_HEIGHT;

            // New window size after zoom
            double newWidth  = (xMax - xMin) * scale;
            double newHeight = (yMax - yMin) * scale;

            // Re-center window around click, preserving relative position (fx,fy)
            xMin = xCenter - (fx * newWidth);
            xMax = xMin + newWidth;
            yMin = yCenter - (fy * newHeight);
            yMax = yMin + newHeight;

            generate();  // recompute fractal
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

    // Mouse released: stop drag
    @Override
    public void mouseReleased(MouseEvent e) {
        isDragging = false;
    }

    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}

    /**
     * Mouse drag → Pan the Mandelbrot view
     */
    @Override
    public void mouseDragged(MouseEvent e) {
        if (!isDragging || isGenerating) return;

        // How much the mouse moved in pixels
        double dx = dragStartX - e.getX();
        double dy = dragStartY - e.getY();

        // Convert pixel shift to complex-plane shift
        double xCenter = (dx / SCREEN_WIDTH) * (xMax - xMin);
        double yCenter = (dy / SCREEN_HEIGHT) * (yMax - yMin);

        // Adjust window boundaries accordingly
        xMin += xCenter;
        xMax += xCenter;
        yMin -= yCenter;  // minus because screen Y increases downward
        yMax -= yCenter;

        // Update drag start reference
        dragStartX = e.getX();
        dragStartY = e.getY();

        generate(); // recompute
        repaint();  // redraw
    }

    @Override
    public void mouseMoved(MouseEvent e) {}
}
