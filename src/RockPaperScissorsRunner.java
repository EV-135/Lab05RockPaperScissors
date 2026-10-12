import javax.swing.SwingUtilities;

/**
 * Starts the Rock Paper Scissors application.
 */
public class RockPaperScissorsRunner {

    /**
     * Creates and displays the game window.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            RockPaperScissorsFrame frame =
                    new RockPaperScissorsFrame();
            frame.setVisible(true);
        });
    }
}