package sample;
import java.util.ArrayList;
import java.util.Random;

public class MonteCarloSimulator {
    public int MAX_ROLLOUT_DEPTH_TURNS = 10; //The max rollout depth in turns. The turn counter increments any time a "Pass" action is used.
    public int NUM_ROLLOUTS_PER_ACTION = 500; //how many rollouts we perform on each valid action from the base game state

    public MonteCarloSimulator() {

    }

    public double rollout(GameState gameState) { //Returns the value of one rollout for an action - 0 is a loss, 1 is a win, 0.5 is a tie.
        GameState cloneState = new GameState(gameState);
        Random rand = new Random();
        double evaluation = cloneState.evaluateTerminalState(MAX_ROLLOUT_DEPTH_TURNS);
        while(evaluation == -1) { //While we haven't reached a terminal state
            ArrayList<Action> validActions = cloneState.getValidActions();
            /*/System.out.println("Valid actions at this point in the rollout");
            for (Action a : validActions) {
                System.out.println(a.toString());
            }
            System.out.println("----------------");/*/
            //Pick a random action from the valid ones, todo, perhaps bias these to be more realistic (ie, weight attacks more than moves)
            Action chosenAction = validActions.get(rand.nextInt(validActions.size()));
            //System.out.println(chosenAction.toString());
            chosenAction.applyActionToGameState(cloneState); //Apply the action to the game state
            //System.out.println(chosenAction.toString());
            evaluation = cloneState.evaluateTerminalState(MAX_ROLLOUT_DEPTH_TURNS);
        }

        return evaluation;
    }

    public double evaluateAction(GameState gameState, Action action) { //Returns the probability of action leading to a win
        double wins = 0;
        action.applyActionToGameState(gameState);
        for (int i = 0; i < NUM_ROLLOUTS_PER_ACTION; i++) {
            wins += rollout(gameState);
        }
        return wins / NUM_ROLLOUTS_PER_ACTION;
    }

    public Action getBestActionFromGameState(GameState gameState) {
        //GameState state = new GameState(gameState); //Copy the gameState so we don't affect the original
        ArrayList<Action> validActions = gameState.getValidActions();
//        System.out.println(validActions);
        System.out.println(gameState.getActionableHeroes());
        double bestScore = 0;
        int bestActionIndex = 0;
        Action bestAction = new Action();

        for (int i = 0; i < validActions.size(); i++) {
            GameState stateCopy = new GameState(gameState); //Copy the gameState so we don't affect the original
            //System.out.println("Turn number " + stateCopy.turnCounter);
            ArrayList<Action> realActions = stateCopy.getValidActions();
            Action realAction = realActions.get(i);
            double score = evaluateAction(stateCopy, realAction);
            //System.out.println(realAction.toString() + " evaluates to: " + score);
            if (score > bestScore) {
                bestScore = score;
                bestActionIndex = i;
            }
        }

        bestAction = validActions.get(bestActionIndex);

        /*for (Action action : validActions) {
            double score = evaluateAction(state, action);
            //todo debug info, remove?
            System.out.println("Evaluation of action: " + action.toString() + " = " + String.valueOf(score));
            //
            if (score > bestScore) {
                bestScore = score;
                bestAction = action;
            }
        }/*/
        //todo debug info, remove?
        System.out.println("Best action is: " + bestAction.toString() + ", probability of win: " + String.valueOf(bestScore));
        //
        return bestAction;
    }
}
