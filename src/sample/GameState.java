package sample;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class GameState {
    public int turnCounter = 0;
    public boolean enemyTurn = false;
    public ArrayList<Hero> friendlyHeroes;
    public ArrayList<Hero> enemyHeroes;

    public GameState(ArrayList<Hero> friendlyHeroList, ArrayList<Hero> enemyHeroList) {
        //Make a copy of each hero via copy constructor
        for (Hero hero : friendlyHeroList) {
            Hero clone = new Hero(hero);
            friendlyHeroes.add(clone);
        }

        for (Hero hero : enemyHeroList) {
            Hero clone = new Hero(hero);
            enemyHeroes.add(clone);
        }
    }

    public GameState(GameState state) { //Copy constructor
        this.turnCounter = state.turnCounter;
        this.enemyTurn = state.enemyTurn;
        for (Hero hero : state.friendlyHeroes) {
            Hero clone = new Hero(hero);
            this.friendlyHeroes.add(clone);
        }
        for (Hero hero : state.enemyHeroes) {
            Hero clone = new Hero(hero);
            this.enemyHeroes.add(clone);
        }
    }

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

    public ArrayList<Action> getValidActions() {
        ArrayList<Hero> heroes;
        if (enemyTurn) {
            heroes = getLivingEnemies();
        }
        else {
            heroes = getLivingFriendlies();
        }

        //heroes now represents the living heros on the side whose turn it is, i.e, all the heroes which could perform an action.
        return new ArrayList<Action>();
    }

    public double evaluateTerminalState(int maxTurns) { //Returns -1 if not terminal, 0 for friendly loss, 0.5 for tie, 1 for friendly win
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
            //Technically, this isn't exactly how victory points work but I think it's a close enough heuristic / approximation
            if (friendlyDamageTaken > enemyDamageTaken) {
                return 0; //Friendly took more damage, enemy wins
            }
            else if (friendlyDamageTaken < enemyDamageTaken) {
                return 1; //Enemy took more damage, friendly wins
            }
            else {
                return 0.5; //We even dealt the same amount of damage all game, unlikely, but a tie
            }
        }

        return -1; //The game state is not terminal.
    }

}