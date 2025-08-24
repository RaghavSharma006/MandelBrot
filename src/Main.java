import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Create a new window (JFrame) with the title "Mandelbrot"
        JFrame frame = new JFrame("Mandelbrot");

        // Make sure the program closes when you press the close (X) button
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Add our custom MandelBrot panel (where drawing happens)
        frame.add(new MandelBrot());

        // Set the window to open in full screen (maximized mode)
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        // Make the window visible
        frame.setVisible(true);

        // Just a console check to see if program started
        System.out.println("hello");
    }
}
