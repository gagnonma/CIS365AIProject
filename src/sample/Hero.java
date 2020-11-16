package sample;

public class Hero {
    public int x;
    public int y;
    public int click;

    public Hero() {
        this.x = 0;
        this.y = 0;
        this.click = 0;
    }

    public Hero(int x, int y) {
        this.x = x;
        this.y = y;
        this.click = 0;
    }

    public void setLocation(int newX, int newY) {
        this.x = newX;
        this.y = newY;
    }

    public void incrementClick() {
        this.click++;
    }

    public void decrementClick() {
        this.click--;
    }



}
