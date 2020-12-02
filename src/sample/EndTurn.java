package sample;

public class EndTurn extends Action{ //an action that ends the turn

    public EndTurn() {
    }

    public void applyActionToGameState(GameState gameState) {
        gameState.turnCounter++;
        if (gameState.enemyTurn) {
            for (Hero hero : gameState.enemyHeroes) {
                hero.costedActions = 0;
            }
        }
        else {
            for (Hero hero : gameState.friendlyHeroes) {
                hero.costedActions = 0;
            }
        }
        gameState.enemyTurn = !gameState.enemyTurn;

    }

    public String toString() {
        return "End turn";
    }
}
