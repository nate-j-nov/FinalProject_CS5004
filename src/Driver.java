import javax.swing.*;

/**
 * @author Nate Novak
 * CS5004 Summer 2021
 * Driver class of the application
 */
public class Driver {
    public static void main(String[] args) {
        Controller controller = new Controller();

        View view = new View(controller);

        view.setTitle("Nata");
        view.setSize(JFrame.MAXIMIZED_HORIZ, JFrame.MAXIMIZED_VERT);
        view.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        view.setVisible(true);
    }
}
