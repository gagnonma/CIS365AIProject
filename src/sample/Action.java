package sample;

public class Action {
    public Hero performer;

    public Action() {//Hero actionPerformer) {
        //this.performer = actionPerformer;
    }

    public GameState getNewGameStateFromAction(GameState gameState) { //This is meant to be overridden by each Action type
        return gameState;
    }
}
