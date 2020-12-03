package sample;

import java.util.Random;

public class BasicAttack extends Action{ //an action that has 1 hero attack another, simulating the dice rolls
    Hero attacker;
    Hero victim;
    public BasicAttack(Hero attack, Hero vic)
    {
        attacker = attack;
        victim = vic;
    }

    public void applyActionToGameState(GameState gameState) {
        /*/Useful note from rulebook:
        The Golden Rule of “Replace then Modify”
        To calculate a value, start with the printed value and then
        apply any replacements, first numbers and then those that
        multiply or divide, and then the sum of all modifiers.
        todo, implement this golden rule if the advanced modifiers are added
         */

        /*/
        STEP 2: CALCULATE ATTACK TOTAL
        In this step, the attacker calculates their attack total
        (attack value + attack roll).
        First, calculate the attacker’s attack value. (See p. 6 for
        Calculating Combat Values.) The attacker’s player then
        rolls 2d6. This is the attack roll. Only one attack roll is
        made, regardless of the number of targets in the attack.
        The attack roll result is then added to the attack value, and
        that sum is the attack total.
        After making the attack roll, but before calculating the
        attack total, players may use effects that allow the attack
        roll to be rerolled. Once all rerolls have been made, apply
        any other effects that change the attack roll or attack
        total. The attack roll and attack total are then finalized.
         */
        Random rand = new Random();
        int attackValue = attacker.attack[attacker.click];
        int roll1 = rand.nextInt(7);
        int roll2 = rand.nextInt(7);
        int attackRoll = roll1 + roll2;
        int attackTotal = attackValue + attackRoll;
        int damage = attacker.damage[attacker.click];

        //todo take into account evasion effects from the victim
        if (roll1 == 1 && roll2 == 1) { //Critical miss, attacker takes 1 unavoidable damage
            attacker.incrementClick();
        }
        else if (roll1 == 6 && roll2 == 6) { //Critical hit, increases damage dealt by 1
            damage++;
            victim.click += damage;
        }
        else if (attackTotal >= victim.defense[victim.click]) { //Hit
            victim.click += damage;
        }
        else { //Not a hit

        }
        if (attacker.tokens == 1) { //The attacker is pushing himself, so deal 1 click unavoidable damage
            attacker.incrementClick();
        }
        attacker.incrementTokens(); //increment the attacker's action tokens by 1 now that they've done the action.
        attacker.costedActions++;

    }

    public String toString() {
        return attacker.toString() + " attacks " + victim.toString();
    }
}
