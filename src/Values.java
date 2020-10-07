public class Values {
    private int totPlayers;
    private int humanPlayers;
    private int computerPlayers;
    private boolean console;

    public void setTotalPlayers(int p) { totPlayers = p; }
    public void setHumanPlayers(int p) { humanPlayers = p; }
    public void setComputerPlayers(int p) { computerPlayers = p; }

    public int returnTotalPlayers() { return totPlayers; }
    public int returnHumPlayers(){
        return humanPlayers;
    }

    public int returnComPlayers(){
        return computerPlayers;
    }

    public boolean checkIfConsole() { return console; }
    public void makeConsoleFalse() { console = false; }
    public void makeConsoleTrue() { console = true; }

}
