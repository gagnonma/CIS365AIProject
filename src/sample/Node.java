package sample;

import java.util.ArrayList;

public class Node {
    enum WallStatus {
        NOWALL, TOP, DOWN, RIGHT, LEFT
    }
    WallStatus wallStatus;
    ArrayList<Node> connectedNodes;
    String name;
    int x;
    int y;
    double g;
    double h;
    double f;
    Node parent;
    boolean isWater;
    boolean occupied;
    Hero occupant;


    public Node(String name, int x, int y) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.connectedNodes = new ArrayList<>();
        this.wallStatus = WallStatus.NOWALL;
        this.g = 0;
        this.h = 0;
        this.f = 0;
        this.isWater = false;
        this.occupied = false;
        this.occupant = null;

    }

    public void addNeighbor(Node neighbor) {
        this.connectedNodes.add(neighbor);
    }


    @Override
    public String toString() {
        return "Node{" +
                "name='" + name + '\'' +
                '}';
    }
}
