import java.util.Random;

/**
 * Chooses a random move for the computer.
 */
public class RandomStrategy implements Strategy {

    private final Random random = new Random();

    /**
     * Returns a randomly selected move.
     *
     * @param playerMove the player's move
     * @return R, P, or S
     */
    @Override
    public String getMove(String playerMove) {
        String[] moves = {"R", "P", "S"};
        return moves[random.nextInt(moves.length)];
    }
}