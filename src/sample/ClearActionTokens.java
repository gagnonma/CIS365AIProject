package sample;

public class ClearActionTokens extends Action{ //Clear the action tokens of 1 hero, uses the action
    Hero hero;
    public ClearActionTokens(Hero h) {
        hero = h;
    }

    public void applyActionToGameState(GameState gameState) {
        hero.tokens = 0; //todo also mark the hero as not able to do another action... costed action tracker?
        hero.costedActions = 1;
    }

    public String toString() {
        return "Clear action tokens for " + hero.toString();
    }
}
