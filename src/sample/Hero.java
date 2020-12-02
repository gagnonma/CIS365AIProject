package sample;

public class Hero implements  Cloneable{
    public int x;
    public int y;
    public int click;
    public int maxClick;
    public int tokens;
    public int[] speed;
    public int[] attack;
    public int[] defense;
    public int[] damage;
    public boolean canFly;
    public Node node;
    public int costedActions;
    public int range;
    String name;


    public Hero(Node position, String name) {
        this.node = position;
        this.x = position.x;
        this.y = position.y;
        this.click = 0;
        this.tokens = 0;
        this.costedActions = 0;
        this.name = name;
    }

    public Hero(Hero hero) { //Copy constructor to create a clone of a hero object
        this.x = hero.x;
        this.y = hero.y;
        this.node = hero.node;
        this.click = hero.click;
        this.maxClick = hero.maxClick;
        this.tokens = hero.tokens;
        this.speed = hero.speed.clone();
        this.attack = hero.attack.clone();
        this.defense = hero.defense.clone();
        this.damage = hero.damage.clone();
        this.canFly = hero.canFly;
        this.node = hero.node;
        this.costedActions = hero.costedActions;
        this.range = hero.range;
        this.name = hero.name;
    }

    public String toString() {
        return name;
    }

    /*/public Hero(int x, int y) {
        this.x = x;
        this.y = y;
        this.click = 0;
        this.tokens = 0;
    }/*/

    public void setLocation(int newX, int newY) {
        this.x = newX;
        this.y = newY;
        //this.node = map.get(this.x).get(this.y);
    }

    public boolean isKOd() {
        return this.click > this.maxClick;
    }

    public void incrementClick() {
        this.click++;
    }

    public void decrementClick() {
        this.click--;
    }

    public void incrementTokens() {this.tokens++;}

    public void decrementTokens() {this.tokens--;}

    public Object clone() throws CloneNotSupportedException{
        return super.clone();
    }

}
