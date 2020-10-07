public class Main {
    private static Values values = new Values();
    public static void main(String[] args) {
        values.makeConsoleTrue();
        new GameManager();
    }
    public static Values getValues() { return values; }
}

