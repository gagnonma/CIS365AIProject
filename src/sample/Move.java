package sample;

public class Move extends Action{ //an action that moves a hero to a new map position.
    Hero hero;
    Node node;
    public Move(Hero h, Node newPosition) {
        hero = h;
        node = newPosition;
    }

    public void applyActionToGameState(GameState gameState) {
        gameState.occupiedNodes.remove(hero.node);
        hero.node = node;
        hero.x = node.x;
        hero.y = node.y;
        if (hero.tokens > 0) { //pushing damage
            hero.incrementClick();
        }
        hero.incrementTokens();
        hero.costedActions++;
        gameState.occupiedNodes.add(hero.node);
    }

    public String toString() {
        if (hero.tokens > 0) {
            return "Move with pushing " + hero.toString() + " to " + node.name;
        }
        return "Move " + hero.toString() + " to " + node.name;
    }
}
