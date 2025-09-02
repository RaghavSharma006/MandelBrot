import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

/**
 * Julia Set Explorer
 * - Left double-click → Zoom in at clicked point
 * - Right double-click → Zoom out at clicked point
 * - Drag → Pan the Mandelbrot view
 * - Select zoom level
 * - Select Iterations
 */

public class Julia extends JPanel implements MouseInputListener {

    JTextField juliaZoomField;
    JTextField juliaZoomLevelField;
    JTextField juliaMaxIterationField;

    // Screen dimensions taken from system display
    final static int SCREEN_HEIGHT;
    final static int SCREEN_WIDTH;

    // Julia set constants (fixed complex number c = c_a + c_b*i)
    final double c_a;
    final double c_b;

    // Julia parameters
    static int maxIterations = 5000;  // max escape-time iterations
    static double zoomFactor = 0.8;   // zoom factor per double-click
    static double zoomed=1;


    // Current rectangle boundaries in complex plane
    double xMin = -2.5;
    double xMax = 2.5;
    double yMin = -2.5;   // Julia set also lies roughly in [-2,2] + i[-2,2] but we have taken 2.5
    double yMax =  2.5;  // See Proof in 1.1

    // The image buffer where we render pixels
    BufferedImage img;

    // Static initializer: detect screen resolution
    static {
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        SCREEN_WIDTH = screen.width;
        SCREEN_HEIGHT = screen.height;
    }


    // Constructor when Julia set is directly given a complex constant
    Julia(double real, double imaginary,JTextField zoomField , JTextField zoomLevelField,JTextField maxIterationField) {

        this.juliaZoomField=zoomField;
        this.juliaZoomLevelField=zoomLevelField;
        this.juliaMaxIterationField =maxIterationField;

        c_a = real;
        c_b = imaginary;
        img=generate();

        addMouseListener(this);
        addMouseMotionListener(this);
    }

    // Generate Julia set image
    BufferedImage generate() {
        BufferedImage temp = new BufferedImage(SCREEN_WIDTH, SCREEN_HEIGHT, BufferedImage.TYPE_INT_RGB);
        maxIterations=Integer.parseInt(juliaMaxIterationField.getText());

        for (int i = 0; i < SCREEN_WIDTH; i++) {
            for (int j = 0; j < SCREEN_HEIGHT; j++) {
                

                // Convert pixel to complex plane coordinate (z)
                // Proof in 2.1
                double z_a = xMin + (i / (double) SCREEN_WIDTH) * (xMax - xMin);
                double z_b = yMin + (j / (double) SCREEN_HEIGHT) * (yMax - yMin);

                int numberOfIterations = 0;

                // Equation ( (c_a * c_a) + (c_b * c_b) <= 4 ) proof in 1.2
                if ((c_a * c_a) + (c_b * c_b) > 4) {
                    Color rust = new Color(28, 1,0); // Reddish-brown fallback
                    temp.setRGB(i,SCREEN_HEIGHT-1- j, rust.getRGB());
                    continue;
                }

                // Escape-time iteration: stop if |z| > 2 (proof in 1.3) or max iterations reached
                // Equation ( (z_a * z_a) + (z_b * z_b) <= 4 ) proof in 1.4
                while ((z_a * z_a) + (z_b * z_b) <= 4 && numberOfIterations < maxIterations) {
                    //Equations Proof in 1.5
                    double zn_a = (z_a * z_a) - (z_b * z_b) + (c_a);  // Real part
                    double zn_b = 2 * (z_a * z_b) + (c_b);            // Imaginary part

                    z_a = zn_a;
                    z_b = zn_b;
                    numberOfIterations++;
                }

                // Coloring: black if inside, else color based on iteration count
                int color = numberOfIterations == maxIterations
                        ? 0
                        : Color.HSBtoRGB(
                        numberOfIterations / 256f,
                        1,
                        numberOfIterations / (numberOfIterations + 8f)
                );

                // Flip vertically (because screen y-axis is inverted compared to complex plane)
                temp.setRGB(i, SCREEN_HEIGHT - 1 - j, color);
            }
        }
        return temp;
    }

    // Draw Julia set image
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(img, 0, 0, null);
    }

    boolean isGenerating = false;

    // Zoom on double click
    @Override
    public void mouseClicked(MouseEvent e) { //Proof in 2.2
        // Double click: zoom in/out
        //Proof in 2.2
        if (e.getClickCount() == 2) {
            if (isGenerating || isDragging) return;
            isGenerating = true;

            // Fractional position of click in window (0.0 = left/top, 1.0 = right/bottom)
            // Ratios with respect to Screen dimensions
            double fx = e.getX() / (double) SCREEN_WIDTH;
            double fy = e.getY() / (double) SCREEN_HEIGHT;

            // Find complex-plane coordinates of clicked pixel
            double xCenter = xMin + fx * (xMax - xMin);
            double yCenter = yMin + fy * (yMax - yMin);

            // Decide zoom direction: left = zoom in, right = zoom out
            zoomFactor=1/(Double.parseDouble(juliaZoomField.getText()));
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
            juliaZoomLevelField.setText(String.valueOf(zoomed));

            img=generate();  // recompute fractal
            repaint();   // redraw
            isGenerating = false;
        }
    }

    double dragStartX, dragStartY;
    boolean isDragging = false;

    // Start dragging
    @Override
    public void mousePressed(MouseEvent e) {
        dragStartX = e.getX();
        dragStartY = e.getY();
        isDragging = true;
    }

    // Handle dragging (panning)
    // Proof in 2.3
    @Override
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

        img=generate();
        repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        isDragging = false;
    }

    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}
    @Override
    public void mouseMoved(MouseEvent e) {}
}
