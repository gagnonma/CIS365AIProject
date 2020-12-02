package sample;

public class Move extends Action{ //an action that moves a hero to a new map position.
    Hero hero;
    Node node;
    public Move(Hero h, Node newPosition) {
        hero = h;
        node = newPosition;
    }

    public void applyActionToGameState(GameState gameState) {
        hero.x = node.x;
        hero.y = node.y;
        hero.incrementTokens();
        //todo, the hero should store the specific node, it would make things easier
    }

    public String toString() {
        return "Move " + hero.toString() + " to " + node.name;
    }
}
