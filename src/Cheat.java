/**
 * Chooses the move that beats the player's current move.
 */
public class Cheat implements Strategy {

    /**
     * Returns the winning move against the player.
     *
     * @param playerMove the player's move
     * @return the winning computer move
     */
    @Override
    public String getMove(String playerMove) {
        switch (playerMove) {
            case "R":
                return "P";
            case "P":
                return "S";
            case "S":
                return "R";
            default:
                return "X";
        }
    }
}