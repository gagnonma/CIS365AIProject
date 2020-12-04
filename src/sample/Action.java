package sample;

public class Action {
    public double weight = 1;
    public double goodness = 1;
    public Action() {
    }

    public void applyActionToGameState(GameState gameState) { //to be overridden.

    }

    public String toString() { //Also to be overridden. This is useful for if you want to print out the best action in English.
        return "Default action";
    }
}
