package sample;

public class CaptainAmerica extends Hero {

    public CaptainAmerica(Node position, String name) {
        super(position,name);
        this.maxClick = 5;
        this.speed = new int[]{8, 7, 7, 6, 6, 5};
        this.attack = new int[]{11,10,10,9,9,9};
        this.defense = new int[]{17,17,17,16,16,17};
        this.damage = new int[]{3,3,3,2,2,2};
        this.canFly = false;
        this.range = 5;
    }
}
