import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main implements ActionListener {

    private static JTextField realField;
    private static JTextField imagField;
    private static JTextField zoomField;
    private static JTextField zoomLevelTextField;
    private static JTextField maxIterationField;

    public static void main(String[] args) {

        // Create main JFrame
        JFrame frame = new JFrame("Mandelbrot");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Main control panel
        JPanel controls = new JPanel(new BorderLayout());

        // ===== Left panel: Zoom + Iterations =====
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftPanel.add(new JLabel("Zoom:"));
        zoomField = new JTextField("1.25", 5);
        leftPanel.add(zoomField);

        leftPanel.add(new JLabel("Iterations:"));
        maxIterationField = new JTextField("5000", 5);
        leftPanel.add(maxIterationField);

        // ===== Center panel: Real, Imaginary, Generate =====
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        centerPanel.add(new JLabel("Real:"));
        realField = new JTextField("0.0", 13);
        centerPanel.add(realField);

        centerPanel.add(new JLabel("Imaginary:"));
        imagField = new JTextField("0.0", 13);
        centerPanel.add(imagField);

        JButton generateBtn = new JButton("Generate");
        centerPanel.add(generateBtn);

        // ===== Right panel: Zoom Level (read-only) =====
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.add(new JLabel("Zoom Level:"));
        zoomLevelTextField = new JTextField("1", 10);
        zoomLevelTextField.setEditable(false);
        rightPanel.add(zoomLevelTextField);

        // Add subpanels to main controls panel
        controls.add(leftPanel, BorderLayout.WEST);
        controls.add(centerPanel, BorderLayout.CENTER);
        controls.add(rightPanel, BorderLayout.EAST);

        // Add control panel to frame
        frame.add(controls, BorderLayout.SOUTH);

        // Create Mandelbrot panel
        MandelBrot mandelbrotPanel = new MandelBrot(realField, imagField, zoomField, maxIterationField, zoomLevelTextField);
        frame.add(mandelbrotPanel, BorderLayout.CENTER);

        // Register Generate button listener
        generateBtn.addActionListener(new Main());

        // Full-screen window
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);

        System.out.println("Application started");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            double real = Double.parseDouble(realField.getText());
            double imag = Double.parseDouble(imagField.getText());

            // Create Julia JFrame
            JFrame juliaFrame = new JFrame();
            juliaFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            juliaFrame.setLayout(new BorderLayout());

            // Initialize local fields for Julia
            JTextField localJuliaZoomField = new JTextField(zoomField.getText(), 5);
            JTextField localJuliaMaxIterField = new JTextField(maxIterationField.getText(), 5);
            JTextField localJuliaZoomLevelField = new JTextField(zoomLevelTextField.getText(), 10);
            localJuliaZoomLevelField.setEditable(false);

            // Create Julia panel
            Julia juliaPanel = new Julia(real, imag,
                    localJuliaZoomField,
                    localJuliaZoomLevelField,
                    localJuliaMaxIterField);

            // Julia control panel
            JPanel juliaControlsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            juliaControlsPanel.add(new JLabel("Zoom:"));
            juliaControlsPanel.add(localJuliaZoomField);
            juliaControlsPanel.add(new JLabel("Iterations:"));
            juliaControlsPanel.add(localJuliaMaxIterField);
            juliaControlsPanel.add(new JLabel("Zoomed:"));
            juliaControlsPanel.add(localJuliaZoomLevelField);

            // Add components to Julia frame
            juliaFrame.add(juliaPanel, BorderLayout.CENTER);
            juliaFrame.add(juliaControlsPanel, BorderLayout.SOUTH);

            juliaFrame.setTitle("Julia Set for " + juliaPanel.c_a + " + " + juliaPanel.c_b + "i");
            juliaFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            juliaFrame.setVisible(true);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Invalid input! Please enter numbers.");
        }
    }
}
