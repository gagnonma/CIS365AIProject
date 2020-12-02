package sample;

public class Pass extends Action{ //an action that passes the turn

    public Pass() {
    }

    public void applyActionToGameState(GameState gameState) {
        gameState.turnCounter++;
        //When your a ends, all heroes action tokens are reset to 0 (i think)
        for (Hero hero : gameState.enemyHeroes) {
            hero.tokens = 0;
        }
        for (Hero hero : gameState.friendlyHeroes) {
            hero.tokens = 0;
        }
        gameState.enemyTurn = !gameState.enemyTurn;
    }
}
