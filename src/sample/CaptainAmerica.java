package sample;

public class CaptainAmerica extends Hero {

    public CaptainAmerica() {
        super();
    }

    public CaptainAmerica(int x, int y) {
        super(x,y);
        this.maxClick = 6;
        this.speed = new int[]{8, 7, 7, 6, 6, 5};
        this.attack = new int[]{11,10,10,9,9,9};
        this.defense = new int[]{17,17,17,16,16,17};
        this.damage = new int[]{3,3,3,2,2,2};
        this.canFly = false;
    }
}
