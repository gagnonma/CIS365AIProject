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



    public Hero() {
        this.x = 0;
        this.y = 0;
        this.click = 0;
        this.tokens = 0;
    }

    public Hero(Hero hero) { //Copy constructor to create a clone of a hero object
        this.x = hero.x;
        this.y = hero.y;
        this.click = hero.click;
        this.maxClick = hero.maxClick;
        this.tokens = hero.tokens;
        this.speed = hero.speed.clone();
        this.attack = hero.attack.clone();
        this.defense = hero.defense.clone();
        this.damage = hero.damage.clone();
        this.canFly = hero.canFly;
    }

    public Hero(int x, int y) {
        this.x = x;
        this.y = y;
        this.click = 0;
        this.tokens = 0;
    }

    public void setLocation(int newX, int newY) {
        this.x = newX;
        this.y = newY;
    }

    public boolean isKOd() {
        return this.click >= this.maxClick;
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
