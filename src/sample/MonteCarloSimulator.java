package sample;
import java.util.ArrayList;
import java.util.Random;

public class MonteCarloSimulator {
    public int MAX_ROLLOUT_DEPTH_TURNS = 50; //The max rollout depth in turns. The turn counter increments any time a "Pass" action is used.
    public int NUM_ROLLOUTS_PER_ACTION = 16641; //how many rollouts we perform on each valid action from the base game state

    public MonteCarloSimulator() {

    }

    public double rollout(GameState gameState, Action action) { //Returns the value of one rollout for an action - 0 is a loss, 1 is a win, 0.5 is a tie.
        Random rand = new Random();
        GameState state = new GameState(gameState); //Copy the gameState so we don't affect the original
        action.applyActionToGameState(gameState); //Perform the first, chosen action
        double evaluation = state.evaluateTerminalState(MAX_ROLLOUT_DEPTH_TURNS);
        while(evaluation == -1) { //While we haven't reached a terminal state
            ArrayList<Action> validActions = state.getValidActions();
            //Pick a random action from the valid ones, todo, perhaps bias these to be more realistic (ie, weight attacks more than moves)
            Action chosenAction = validActions.get(rand.nextInt(validActions.size()));
            chosenAction.applyActionToGameState(state); //Apply the action to the game state

            evaluation = state.evaluateTerminalState(MAX_ROLLOUT_DEPTH_TURNS);
        }

        return evaluation;
    }

    public double evaluateAction(GameState gameState, Action action) { //Returns the probability of action leading to a win
        double wins = 0;
        for (int i = 0; i < NUM_ROLLOUTS_PER_ACTION; i++) {
            wins += rollout(gameState, action);
        }
        return wins / NUM_ROLLOUTS_PER_ACTION;
    }

    public Action getBestActionFromGameState(GameState gameState) {
        ArrayList<Action> validActions = gameState.getValidActions();
        double bestScore = 0;
        Action bestAction = new Action();
        for (Action action : validActions) {
            double score = evaluateAction(gameState, action);
            if (score > bestScore) {
                bestScore = score;
                bestAction = action;
            }
        }

        return bestAction;
    }
}
