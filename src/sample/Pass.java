package sample;

public class Pass extends Action{ //an action that passes the turn

    public Pass() {
    }

    public void applyActionToGameState(GameState gameState) {
        gameState.turnCounter++;
        gameState.enemyTurn = !gameState.enemyTurn;
    }
}
