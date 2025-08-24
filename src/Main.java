import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Mandelbrot");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new MandelBrot());
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // fullscreen
        frame.setVisible(true);
        System.out.println("hello");
    }
}