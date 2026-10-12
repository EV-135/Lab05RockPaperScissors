
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

/**
 * Provides the GUI and game logic for Rock Paper Scissors.
 */
public class RockPaperScissorsFrame extends JFrame {

    private final JTextField playerWinsField;
    private final JTextField computerWinsField;
    private final JTextField tiesField;
    private final JTextArea historyArea;

    private int playerWins = 0;
    private int computerWins = 0;
    private int ties = 0;

    private int rockCount = 0;
    private int paperCount = 0;
    private int scissorsCount = 0;

    private String lastPlayerMove = null;

    private final Random random = new Random();

    private final Strategy randomStrategy = new RandomStrategy();
    private final Strategy cheatStrategy = new Cheat();

    /**
     * Constructs the game window and its components.
     */
    public RockPaperScissorsFrame() {
        setTitle("Rock Paper Scissors Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(650, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Game buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBorder(
                BorderFactory.createTitledBorder("Choose Your Move"));

        JButton rockButton = createMoveButton("Rock", "R", "rock.png");
        JButton paperButton = createMoveButton("Paper", "P", "paper.png");
        JButton scissorsButton =
                createMoveButton("Scissors", "S", "scissors.png");

        JButton quitButton = new JButton("Quit");
        quitButton.addActionListener(e -> dispose());

        buttonPanel.add(rockButton);
        buttonPanel.add(paperButton);
        buttonPanel.add(scissorsButton);
        buttonPanel.add(quitButton);

        // One listener handles all three moves
        ActionListener moveListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                String playerMove = ae.getActionCommand();
                playRound(playerMove);
            }
        };

        rockButton.addActionListener(moveListener);
        paperButton.addActionListener(moveListener);
        scissorsButton.addActionListener(moveListener);

        // Statistics panel
        JPanel statsPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        statsPanel.setBorder(
                BorderFactory.createTitledBorder("Game Statistics"));

        playerWinsField = createReadOnlyField();
        computerWinsField = createReadOnlyField();
        tiesField = createReadOnlyField();

        statsPanel.add(new JLabel("Player Wins:"));
        statsPanel.add(playerWinsField);
        statsPanel.add(new JLabel("Computer Wins:"));
        statsPanel.add(computerWinsField);
        statsPanel.add(new JLabel("Ties:"));
        statsPanel.add(tiesField);

        // Game history panel
        historyArea = new JTextArea(10, 40);
        historyArea.setEditable(false);
        historyArea.setLineWrap(true);
        historyArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(historyArea);
        scrollPane.setBorder(
                BorderFactory.createTitledBorder("Game History"));

        add(buttonPanel, BorderLayout.NORTH);
        add(statsPanel, BorderLayout.WEST);
        add(scrollPane, BorderLayout.CENTER);

        updateStats();
    }

    /**
     * Creates a button for a player's move.
     *
     * @param label the button label
     * @param move the move's action command
     * @param imageName the image filename
     * @return the configured button
     */
    private JButton createMoveButton(
            String label, String move, String imageName) {

        JButton button = new JButton(label);
        button.setActionCommand(move);

        java.net.URL imageURL =
                getClass().getResource("/" + imageName);

        if (imageURL != null) {
            ImageIcon icon = new ImageIcon(imageURL);
            Image scaledImage = icon.getImage().getScaledInstance(
                    50, 50, Image.SCALE_SMOOTH);
            button.setIcon(new ImageIcon(scaledImage));
            button.setVerticalTextPosition(SwingConstants.BOTTOM);
            button.setHorizontalTextPosition(SwingConstants.CENTER);
        }

        return button;
    }

    /**
     * Creates a non-editable statistics field.
     *
     * @return the configured text field
     */
    private JTextField createReadOnlyField() {
        JTextField field = new JTextField("0", 5);
        field.setEditable(false);
        return field;
    }

    /**
     * Plays one round and updates the results and statistics.
     *
     * @param playerMove the player's move
     */
    private void playRound(String playerMove) {
        int choice = random.nextInt(100) + 1;

        Strategy strategy;
        String strategyName;

        if (choice <= 10) {
            strategy = cheatStrategy;
            strategyName = "Cheat";
        } else if (choice <= 30) {
            strategy = new LeastUsed();
            strategyName = "Least Used";
        } else if (choice <= 50) {
            strategy = new MostUsed();
            strategyName = "Most Used";
        } else if (choice <= 70) {
            strategy = new LastUsed();
            strategyName = "Last Used";
        } else {
            strategy = randomStrategy;
            strategyName = "Random";
        }

        String computerMove = strategy.getMove(playerMove);
        String result = determineResult(playerMove, computerMove);

        historyArea.append(
                result + " (Computer: " + strategyName + ")\n");

        // Update statistics using the current round
        if (result.contains("Player wins")) {
            playerWins++;
        } else if (result.contains("Computer wins")) {
            computerWins++;
        } else {
            ties++;
        }

        // Record the player's move for future strategies
        switch (playerMove) {
            case "R":
                rockCount++;
                break;
            case "P":
                paperCount++;
                break;
            case "S":
                scissorsCount++;
                break;
        }

        lastPlayerMove = playerMove;
        updateStats();
        historyArea.setCaretPosition(
                historyArea.getDocument().getLength());
    }

    /**
     * Determines the result of a round.
     *
     * @param playerMove the player's move
     * @param computerMove the computer's move
     * @return a description of the result
     */
    private String determineResult(
            String playerMove, String computerMove) {

        if (playerMove.equals(computerMove)) {
            return "Tie! Both chose " + moveName(playerMove) + ".";
        }

        if ((playerMove.equals("R") && computerMove.equals("S"))
                || (playerMove.equals("P") && computerMove.equals("R"))
                || (playerMove.equals("S") && computerMove.equals("P"))) {

            return moveName(playerMove) + " beats "
                    + moveName(computerMove) + ". (Player wins!)";
        }

        return moveName(computerMove) + " beats "
                + moveName(playerMove) + ". (Computer wins!)";
    }

    /**
     * Converts a move code to its full name.
     *
     * @param move the move code
     * @return the move's name
     */
    private String moveName(String move) {
        switch (move) {
            case "R":
                return "Rock";
            case "P":
                return "Paper";
            case "S":
                return "Scissors";
            default:
                return "Unknown";
        }
    }

    /**
     * Refreshes the displayed statistics.
     */
    private void updateStats() {
        playerWinsField.setText(String.valueOf(playerWins));
        computerWinsField.setText(String.valueOf(computerWins));
        tiesField.setText(String.valueOf(ties));
    }

    /**
     * Returns the move that beats the specified move.
     *
     * @param move the move to beat
     * @return the winning move
     */
    private String winningMoveAgainst(String move) {
        switch (move) {
            case "R":
                return "P";
            case "P":
                return "S";
            case "S":
                return "R";
            default:
                return "R";
        }
    }

    /**
     * Returns a random move.
     *
     * @return R, P, or S
     */
    private String randomMove() {
        String[] moves = {"R", "P", "S"};
        return moves[random.nextInt(moves.length)];
    }

    /**
     * Chooses a move that beats the player's least-used move.
     */
    private class LeastUsed implements Strategy {

        /**
         * Selects the least-used player move and counters it.
         *
         * @param playerMove the player's current move
         * @return the computer's move
         */
        @Override
        public String getMove(String playerMove) {
            int minimum = Math.min(rockCount,
                    Math.min(paperCount, scissorsCount));

            java.util.ArrayList<String> candidates =
                    new java.util.ArrayList<>();

            if (rockCount == minimum) {
                candidates.add("R");
            }
            if (paperCount == minimum) {
                candidates.add("P");
            }
            if (scissorsCount == minimum) {
                candidates.add("S");
            }

            String predictedMove =
                    candidates.get(random.nextInt(candidates.size()));

            return winningMoveAgainst(predictedMove);
        }
    }

    /**
     * Chooses a move that beats the player's most-used move.
     */
    private class MostUsed implements Strategy {

        /**
         * Selects the most-used player move and counters it.
         *
         * @param playerMove the player's current move
         * @return the computer's move
         */
        @Override
        public String getMove(String playerMove) {
            int maximum = Math.max(rockCount,
                    Math.max(paperCount, scissorsCount));

            java.util.ArrayList<String> candidates =
                    new java.util.ArrayList<>();

            if (rockCount == maximum) {
                candidates.add("R");
            }
            if (paperCount == maximum) {
                candidates.add("P");
            }
            if (scissorsCount == maximum) {
                candidates.add("S");
            }

            String predictedMove =
                    candidates.get(random.nextInt(candidates.size()));

            return winningMoveAgainst(predictedMove);
        }
    }

    /**
     * Counters the player's move from the previous round.
     */
    private class LastUsed implements Strategy {

        /**
         * Selects a move that beats the player's previous move.
         *
         * @param playerMove the player's current move
         * @return the computer's move
         */
        @Override
        public String getMove(String playerMove) {
            if (lastPlayerMove == null) {
                return randomMove();
            }

            return winningMoveAgainst(lastPlayerMove);
        }
    }
}
