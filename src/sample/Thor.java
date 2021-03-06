package sample;

public class Thor extends Hero {
    public Thor(Node position, String name) {
        super(position, name);
        this.maxClick = 8;
        this.speed = new int[]{10,10,10,10,10,10,9,9,9};
        this.attack = new int[]{11,11,11,10,10,10,9,9,9};
        this.defense = new int[]{18,17,17,17,17,17,17,17,16};
        this.damage = new int[]{4,4,3,3,3,3,3,3,3};
        this.canFly = true;
        this.range = 6;
    }
}
