package sample;

public class EndTurn extends Action{ //an action that ends the turn

    public EndTurn() {
    }

    public void applyActionToGameState(GameState gameState) {
        gameState.turnCounter++;
        gameState.enemyTurn = !gameState.enemyTurn;
    }

    public String toString() {
        return "End turn";
    }
}
