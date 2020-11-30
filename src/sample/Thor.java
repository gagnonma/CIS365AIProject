package sample;

public class Thor extends Hero {
    public Thor() {
        super();
    }

    public Thor(int x, int y) {
        super(x,y);
        this.maxClick = 9;
        this.speed = new int[]{10,10,10,10,10,10,9,9,9};
        this.attack = new int[]{11,11,11,10,10,10,9,9,9};
        this.defense = new int[]{18,17,17,17,17,17,17,17,16};
        this.damage = new int[]{4,4,3,3,3,3,3,3,3};
        this.canFly = false;
    }
}
