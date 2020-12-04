package sample;
import java.util.ArrayList;
import java.util.Random;

public class MonteCarloSimulator {
    public int MAX_ROLLOUT_DEPTH_TURNS = 10; //The max rollout depth in turns. The turn counter increments any time a "Pass" action is used.
    public int NUM_ROLLOUTS_PER_ACTION = 500; //how many rollouts we perform on each valid action from the base game state

    public MonteCarloSimulator() {

    }

    public Action getRandomActionWeighted(ArrayList<Action> actions) {
        double totalWeight = 0;
        for (Action action : actions) {
            totalWeight += action.weight;
        }
        //System.out.println(totalWeight);

        double threshold = Math.random() * totalWeight;
        double sumWeight = 0;
        for (Action action : actions) {
            sumWeight += action.weight;
            if (sumWeight >= threshold) {
                return action;
            }
        }
        return null;
    }

    public double rollout(GameState gameState, int actionIndex, int maxDepth) { //Returns the value of one rollout for an action - 0 is a loss, 1 is a win, 0.5 is a tie.
        GameState cloneState = new GameState(gameState);
        Action action = cloneState.getValidActions().get(actionIndex);
        action.applyActionToGameState(cloneState);
        Random rand = new Random();
        double evaluation = cloneState.evaluateTerminalState(maxDepth);
        while(evaluation == -1) { //While we haven't reached a terminal state
            ArrayList<Action> validActions = cloneState.getValidActions();
            /*/System.out.println("Valid actions at this point in the rollout");
            for (Action a : validActions) {
                System.out.println(a.toString());
            }
            System.out.println("----------------");/*/
            //Pick a random action from the valid ones
            Action chosenAction = getRandomActionWeighted(validActions); //validActions.get(rand.nextInt(validActions.size()));
            //System.out.println(chosenAction.toString());
            chosenAction.applyActionToGameState(cloneState); //Apply the action to the game state
            //System.out.println(chosenAction.toString());
            evaluation = cloneState.evaluateTerminalState(maxDepth);
        }
        //int i = 1;
        return evaluation;
    }

    public double evaluateAction(GameState gameState, int actionIndex) { //Returns the probability of action leading to a win
        double wins = 0;
        for (int i = 0; i < NUM_ROLLOUTS_PER_ACTION; i++) {
            //action.applyActionToGameState(gameState);
            if (i%2==1) {
                wins += rollout(gameState, actionIndex, MAX_ROLLOUT_DEPTH_TURNS);
            }
            else {
                wins += rollout(gameState, actionIndex, MAX_ROLLOUT_DEPTH_TURNS - 1);
            }

        }
        return wins / NUM_ROLLOUTS_PER_ACTION;
    }

    public Action getBestActionFromGameState(GameState gameState) {
        GameState state = new GameState(gameState); //Copy the gameState so we don't affect the original
        ArrayList<Action> validActions = gameState.getValidActions();

        double bestScore = 0;
        int bestActionIndex = 0;
        Action bestAction = new Action();
        String bestActionString = "";

        for (int i = 0; i < validActions.size(); i++) {
            GameState stateCopy = new GameState(state); //Copy the gameState so we don't affect the original
            ArrayList<Action> realActions = stateCopy.getValidActions();
            Action realAction = realActions.get(i);
            String actionName = realAction.toString();
            System.out.println(realAction.toString() + " being evaluated...");
            double score = realAction.goodness * evaluateAction(stateCopy, i);
            System.out.println(actionName + " evaluates to: " + score);
            if (score > bestScore) {
                bestScore = score;
                bestAction = realAction;
                bestActionString = actionName;
                //bestActionIndex = i;
            }
        }

        System.out.println("Best action is: " + bestActionString + ", probability of win: " + String.valueOf(bestScore));
        return bestAction;
    }
}
