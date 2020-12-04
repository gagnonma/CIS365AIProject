package sample;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;

public class GameState {
    public int turnCounter = 0;
    public boolean enemyTurn = false;
    public ArrayList<Hero> friendlyHeroes;
    public ArrayList<Hero> enemyHeroes;
    Model model;

    public HashSet<Node> occupiedNodes;

    public GameState(ArrayList<Hero> friendlyHeroList, ArrayList<Hero> enemyHeroList, Model m) {
        //Make a copy of each hero via copy constructor
        friendlyHeroes = new ArrayList<Hero>();
        enemyHeroes = new ArrayList<Hero>();
        occupiedNodes = new HashSet<Node>();
        for (Hero hero : friendlyHeroList) {
            Hero clone = new Hero(hero);
            friendlyHeroes.add(clone);
            occupiedNodes.add(hero.node);
        }

        for (Hero hero : enemyHeroList) {
            Hero clone = new Hero(hero);
            enemyHeroes.add(clone);
            occupiedNodes.add(hero.node);
        }
        model = m;
    }

    public GameState(GameState state) { //Copy constructor
        this.turnCounter = state.turnCounter;
        this.enemyTurn = state.enemyTurn;
        this.model = state.model;
        friendlyHeroes = new ArrayList<Hero>();
        enemyHeroes = new ArrayList<Hero>();
        for (Hero hero : state.friendlyHeroes) {
            Hero clone = new Hero(hero);
            this.friendlyHeroes.add(clone);
        }
        for (Hero hero : state.enemyHeroes) {
            Hero clone = new Hero(hero);
            this.enemyHeroes.add(clone);
        }
        this.occupiedNodes = (HashSet<Node>) state.occupiedNodes.clone();
    }

    /*public ArrayList<Node> getNodesFromNode(Node node, int distance) { //Returns a list of reachable nodes within distance of node
        ArrayList<Node> reachable = new ArrayList<Node>();
        reachable.add(node);
        int lastIndex = 0;
        for (int i = 0; i < distance; i++) {
            for (int k = lastIndex; k < reachable.size(); k++) {
                Node n = reachable.get(k);
                for (Node child : n.connectedNodes) {
                    if (!reachable.contains(child)) {
                        reachable.add(child);
                    }
                }
            }
        }
    }*/

    public ArrayList<Hero> getLivingFriendlies() {
        ArrayList<Hero> livingFriendlies = new ArrayList<Hero>();
        for (Hero hero : friendlyHeroes) {
            if (!hero.isKOd()) {
                livingFriendlies.add(hero);
            }
        }
        return livingFriendlies;
    }

    public ArrayList<Hero> getLivingEnemies() {
        ArrayList<Hero> livingEnemies = new ArrayList<Hero>();
        for (Hero hero : enemyHeroes) {
            if (!hero.isKOd()) {
                livingEnemies.add(hero);
            }
        }
        return livingEnemies;
    }

    public ArrayList<Hero> getLivingHeroes () { //Get all living heroes regardless of team
        ArrayList<Hero> livingHeroes = new ArrayList<Hero>();;
        livingHeroes.addAll(getLivingFriendlies());
        livingHeroes.addAll(getLivingEnemies());
        return livingHeroes;
    }

    public ArrayList<Hero> getActionableHeroes() { //Gets an array of all heroes who could perform an action (ie, it's their turn, they're alive, and they have 0 costed actions)
        ArrayList<Hero> actionableHeroes;
        if (enemyTurn) {
            actionableHeroes = getLivingEnemies();
        }
        else
        {
            actionableHeroes = getLivingFriendlies();
        }
        ArrayList<Hero> heroesWithActionsLeft = new ArrayList<Hero>();
        for (Hero hero : actionableHeroes) { //Go back and remove the ones who don't have 0 costed actions
            if (hero.costedActions <= 0) {
                heroesWithActionsLeft.add(hero);
            }
        }

        return heroesWithActionsLeft;
    }

    public ArrayList<Action> getValidActions() {
        ArrayList<Action> validActions = new ArrayList<Action>();
        //First, we know that ending turn is always an option so we will add this action to the list immediately.
        EndTurn endTurn = new EndTurn();
        endTurn.goodness = 0.9;
        validActions.add(endTurn);

        ArrayList<Hero> actionableHeroes = getActionableHeroes();
//        System.out.println(actionableHeroes);

        for (Hero hero : actionableHeroes) {
            //If a hero has nonzero tokens, its always a move to clear its tokens.
            if (hero.tokens > 0) {
                ClearActionTokens clearActionTokens = new ClearActionTokens(hero);
                clearActionTokens.weight = 150;
                validActions.add(clearActionTokens);
            }

            if (hero.tokens < 2) {
                //First let's find every valid Movement for each hero.
                HashSet<Node> reachableNodes = model.getPrecomputedReachableNodes(hero); //model.getReachableNodes(hero);
                //System.out.println(reachableNodes);W
                for (Node node : reachableNodes) {
                    if (!occupiedNodes.contains(node)) {
                        Move move = new Move(hero, node);
                        if (hero.tokens > 0) {
                            move.weight = 0.05;//0.05;
                            move.goodness = 0.95;
                        } else {
                            move.goodness = 0.98;
                            move.weight = 0.1;
                        }
                        validActions.add(move);
                    }
                }
            }


            //Now let's find every attackable hero and create an action to attack it.
            ArrayList<Hero> attackableHeroes;
            if (enemyTurn) {
                attackableHeroes = getLivingFriendlies();
            } else {
                attackableHeroes = getLivingEnemies();
            }

            for (Hero attackableHero : attackableHeroes) {
                if (model.inRange(hero.node, attackableHero.node, hero.range)) {
                    BasicAttack attack = new BasicAttack(hero, attackableHero);
                    attack.weight = 5;
                    validActions.add(attack);
                }
            }
        }

        return validActions;
    }

    public double evaluateTerminalState(int maxTurns) { //Returns -1 if not terminal, 0 for friendly loss, 1 for friendly win, otherwise in between
        boolean allFriendlyHeroesKOd = true;
        int friendlyDamageTaken = 0;
        for (Hero hero : friendlyHeroes) {
            friendlyDamageTaken += hero.click;
            if(!hero.isKOd()) {
                allFriendlyHeroesKOd = false;
            }
        }
        if (allFriendlyHeroesKOd) {
            return 0; //All the friendlies are KO'd, enemy wins.
        }

        int enemyDamageTaken = 0;
        boolean allEnemyHeroesKOd = true;
        for (Hero hero : enemyHeroes) {
            enemyDamageTaken += hero.click;
            if(!hero.isKOd()) {
                allEnemyHeroesKOd = false;
            }
        }
        if (allEnemyHeroesKOd) {
            return 1; //All the enemies are KO'd, friendly wins.
        }

        if (turnCounter >= maxTurns) { //We hit the max moves, so we evaluate according to Heroclix Victory Points rules

            return 0.5 + ((enemyDamageTaken - friendlyDamageTaken) / 38.0); //hardcoded hax
            //Technically, this isn't exactly how victory points work but I think it's a close enough heuristic / approximation
            /*/
            if (friendlyDamageTaken > enemyDamageTaken) {
                return 0; //Friendly took more damage, enemy wins
            }
            else if (friendlyDamageTaken < enemyDamageTaken) {
                return 1; //Enemy took more damage, friendly wins
            }
            else {

                //return 0.5; //We even dealt the same amount of damage all game, unlikely, but a tie
            }/*/
        }

        return -1; //The game state is not terminal.
    }

}
