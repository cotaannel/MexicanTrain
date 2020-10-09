/**
 * @author Annel Cota
 *
 * Values class is used to pass values from the GUIController
 * to the GameManager class.
 */

public class Values {
    private int totPlayers;
    private int humanPlayers;
    private int computerPlayers;
    private boolean console;

    /**
     * Sets the number of total players.
     * @param p : total players
     */
    public void setTotalPlayers(int p) { totPlayers = p; }

    /**
     * Sets the amount of human players
     * @param p : human players
     */
    public void setHumanPlayers(int p) { humanPlayers = p; }

    /**
     * Sets the amount of computer players
     * @param p : computer players
     */
    public void setComputerPlayers(int p) { computerPlayers = p; }

    /**
     * Gets the total number of players.
     * @return total players number
     */
    public int returnTotalPlayers() { return totPlayers; }

    /**
     * Gets the total number of human players.
     * @return total human players
     */
    public int returnHumPlayers(){ return humanPlayers; }

    /**
     * Gets the total number of computer players.
     * @return total computer players
     */
    public int returnComPlayers(){ return computerPlayers; }

    /**
     * Checks if console version is being played.
     * @return console version or not
     */
    public boolean checkIfConsole() { return console; }

    /**
     * Makes console version false if playing GUI version.
     */
    public void makeConsoleFalse() { console = false; }

    /**
     * Makes console version true if playing console version.
     */
    public void makeConsoleTrue() { console = true; }

}
