package sample;

public class Ironman extends Hero {
    //public Ironman(Node position) {
    //    super(position);
    //}

    public Ironman(Node position, String name) {
        super(position, name);
        this.maxClick = 6;
        this.speed = new int[]{8,7,7,6,6,6,5};
        this.attack = new int[]{11,11,11,10,10,10,9};
        this.defense = new int[]{18,18,18,17,17,17,17};
        this.damage = new int[]{4,3,3,3,2,2,2};
        this.canFly = true;
        this.range = 7;
    }
}
