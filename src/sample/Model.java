package sample;

import javafx.util.Pair;

import java.util.*;

public class Model {
    String intToLetter = "abcdefghijklmnop";

    enum SelectMode {
            WALLUP, WALLDOWN, WALLRIGHT, WALLLEFT, START, GOAL, CLEAR,
        THOR, IRONMAN, CAP_AMERICA, E_THOR, E_IRONMAN, E_CAP_AMERICA
    }

    private SelectMode currentMode;
    ArrayList<ArrayList<Node>> map;
    HashMap<String,Node> nodesByName;
    Node start;
    Node goal;
    Hero thor;
    Hero ironman;
    Hero captainAmerica;
    Hero enemyThor;
    Hero enemyIronman;
    Hero enemyCaptainAmerica;


    /**
     *
     */


    public Model () {
        setCurrentMode(SelectMode.CLEAR);
        map = new ArrayList<>();
        for (int i = 0; i < 16; i++) { //Columns
            map.add(new ArrayList<>());
            for (int j = 0; j < 16; j++) { //Rows
                String name = "" + intToLetter.charAt(i) + (j + 1);
                map.get(i).add(new Node(name, i, j));
//                nodesByName.put(name, map.get(i).get(j));
            }
        }
        initializeNeighbors();

        thor = new Thor(0,0);
        enemyThor = new Thor(15,15);
        ironman = new Ironman(0,1);
        enemyIronman = new Ironman(15,14);
        captainAmerica = new CaptainAmerica(1,0);
        enemyCaptainAmerica = new CaptainAmerica(14,15);
    }

    public void selectNode(int x, int y) {
        switch (currentMode) {
            case START:
                start = map.get(x).get(y);
                break;
            case GOAL:
                goal = map.get(x).get(y);
                break;
            case CLEAR:
                break;
            case THOR:
                thor.setLocation(x,y);
                break;
            case IRONMAN:
                ironman.setLocation(x,y);
                break;
            case CAP_AMERICA:
                captainAmerica.setLocation(x,y);
                break;
            case E_THOR:
                enemyThor.setLocation(x,y);
                break;
            case E_IRONMAN:
                enemyIronman.setLocation(x,y);
                break;
            case E_CAP_AMERICA:
                enemyCaptainAmerica.setLocation(x,y);
                break;
            default:
                addWall(x,y,currentMode);
        }
    }


    public ArrayList<Pair<Integer, Integer>> findPath() {
        ArrayList<Node> queue = new ArrayList<>();
        queue.add(start);
        ArrayList<Node> visited = new ArrayList<>();
        ArrayList<Node> inspected = new ArrayList<>();
        inspected.add(start);
        while (!queue.isEmpty()) {
            Node currentNode = queue.remove(0);
            visited.add(currentNode);
            if (currentNode == goal) {
                return calcPath(currentNode);
            }
            for (Node neighbor: currentNode.connectedNodes) {
                if (!inspected.contains(neighbor)) {
                    neighbor.parent = currentNode;
                    neighbor.g = currentNode.g + 1;
                    neighbor.h = calcDistance(neighbor,goal);
                    neighbor.f = neighbor.g + neighbor.h;
                    inspected.add(neighbor);
                    if (!visited.contains(neighbor)){
                        queue.add(neighbor);
                    }
                }
            }
            queue.sort(Comparator.comparingDouble((Node n) -> n.f));
        }


        return null;
    }

    private double calcDistance (Node curr, Node target) {
        return Math.sqrt(Math.pow(curr.x - target.x, 2) + Math.pow(curr.y - target.y, 2));
    }

    private ArrayList<Pair<Integer, Integer>> calcPath(Node currentNode) {
        ArrayList<Pair<Integer, Integer>> path = new ArrayList<>();
        Node temp = currentNode;
        while (temp != start) {
            path.add(new Pair<>(temp.x,temp.y));
            temp = temp.parent;
        }
        path.add(new Pair<>(start.x, start.y));

        return path;
    }

    public boolean isConnected(Node current, Node neighbor) {
        int xDiff = neighbor.x - current.x;
        int yDiff = neighbor.y - current.y;
        if (xDiff == 1 && yDiff == 0) { //right
            if (current.wallStatus == Node.WallStatus.RIGHT || neighbor.wallStatus == Node.WallStatus.LEFT) {
                return false;
            }
            return true;
        }
        if (xDiff == 1 && yDiff == 1) { //bottomRight
            if ((current.wallStatus == Node.WallStatus.RIGHT && neighbor.wallStatus == Node.WallStatus.LEFT) || //right wall
                    (map.get(current.x+1).get(current.y).wallStatus == Node.WallStatus.LEFT && neighbor.wallStatus == Node.WallStatus.LEFT) || //right wall
                    (map.get(current.x).get(current.y+1).wallStatus == Node.WallStatus.RIGHT && current.wallStatus == Node.WallStatus.RIGHT) || //right wall
                    (map.get(current.x+1).get(current.y).wallStatus == Node.WallStatus.LEFT && map.get(current.x).get(current.y+1).wallStatus == Node.WallStatus.RIGHT) || //right wall
                    (map.get(current.x+1).get(current.y).wallStatus == Node.WallStatus.DOWN && neighbor.wallStatus == Node.WallStatus.LEFT) || //corner away
                    (map.get(current.x).get(current.y+1).wallStatus == Node.WallStatus.RIGHT && neighbor.wallStatus == Node.WallStatus.TOP) || //corner away
                    (map.get(current.x+1).get(current.y).wallStatus == Node.WallStatus.DOWN && map.get(current.x).get(current.y+1).wallStatus == Node.WallStatus.RIGHT) || //corner away
                    (map.get(current.x+1).get(current.y).wallStatus == Node.WallStatus.LEFT && current.wallStatus == Node.WallStatus.DOWN) || //corner in
                    (map.get(current.x).get(current.y+1).wallStatus == Node.WallStatus.TOP && current.wallStatus == Node.WallStatus.RIGHT) || //corner in
                    (map.get(current.x+1).get(current.y).wallStatus == Node.WallStatus.LEFT && map.get(current.x).get(current.y+1).wallStatus == Node.WallStatus.TOP) || //corner In
                    (current.wallStatus == Node.WallStatus.DOWN && neighbor.wallStatus == Node.WallStatus.TOP) || //wall down
                    (map.get(current.x+1).get(current.y).wallStatus == Node.WallStatus.DOWN && current.wallStatus == Node.WallStatus.DOWN) || //WALL DOWN
                    (map.get(current.x).get(current.y+1).wallStatus == Node.WallStatus.TOP && neighbor.wallStatus == Node.WallStatus.TOP) || //wall down
                    (map.get(current.x+1).get(current.y).wallStatus == Node.WallStatus.DOWN && map.get(current.x).get(current.y+1).wallStatus == Node.WallStatus.TOP)) { //wall down
                return false;
            }
            return true;
        }
        if (xDiff == 0 && yDiff == 1) { //bottom
            if (current.wallStatus == Node.WallStatus.DOWN || neighbor.wallStatus == Node.WallStatus.TOP) {
                return false;
            }
            return true;
        }
        if (xDiff == -1 && yDiff == 1) { //bottomLeft
            if ((current.wallStatus == Node.WallStatus.LEFT && neighbor.wallStatus == Node.WallStatus.RIGHT) || //left wall
                    (map.get(current.x-1).get(current.y).wallStatus == Node.WallStatus.RIGHT && neighbor.wallStatus == Node.WallStatus.RIGHT) || //left wall
                    (map.get(current.x).get(current.y+1).wallStatus == Node.WallStatus.LEFT && current.wallStatus == Node.WallStatus.LEFT) || //left wall
                    (map.get(current.x-1).get(current.y).wallStatus == Node.WallStatus.RIGHT && map.get(current.x).get(current.y+1).wallStatus == Node.WallStatus.LEFT) || //left wall
                    (map.get(current.x-1).get(current.y).wallStatus == Node.WallStatus.DOWN && neighbor.wallStatus == Node.WallStatus.RIGHT) || //corner away
                    (map.get(current.x).get(current.y+1).wallStatus == Node.WallStatus.LEFT && neighbor.wallStatus == Node.WallStatus.TOP) || //corner away
                    (map.get(current.x-1).get(current.y).wallStatus == Node.WallStatus.DOWN && map.get(current.x).get(current.y+1).wallStatus == Node.WallStatus.LEFT) || //corner away
                    (map.get(current.x-1).get(current.y).wallStatus == Node.WallStatus.RIGHT && current.wallStatus == Node.WallStatus.DOWN) || //corner in
                    (map.get(current.x).get(current.y+1).wallStatus == Node.WallStatus.TOP && current.wallStatus == Node.WallStatus.LEFT) || //corner in
                    (map.get(current.x-1).get(current.y).wallStatus == Node.WallStatus.RIGHT && map.get(current.x).get(current.y+1).wallStatus == Node.WallStatus.TOP) || //corner In
                    (current.wallStatus == Node.WallStatus.DOWN && neighbor.wallStatus == Node.WallStatus.TOP) || //wall down
                    (map.get(current.x-1).get(current.y).wallStatus == Node.WallStatus.DOWN && current.wallStatus == Node.WallStatus.DOWN) || //WALL DOWN
                    (map.get(current.x).get(current.y+1).wallStatus == Node.WallStatus.TOP && neighbor.wallStatus == Node.WallStatus.TOP) || //wall down
                    (map.get(current.x-1).get(current.y).wallStatus == Node.WallStatus.DOWN && map.get(current.x).get(current.y+1).wallStatus == Node.WallStatus.TOP)) { //wall down
                return false;
            }
            return true;
        }
        if (xDiff == -1 && yDiff == 0) { //Left
            if (current.wallStatus == Node.WallStatus.LEFT || neighbor.wallStatus == Node.WallStatus.RIGHT) {
                return false;
            }
            return true;
        }
        if (xDiff == -1 && yDiff == -1) { //topLeft
            if ((current.wallStatus == Node.WallStatus.LEFT && neighbor.wallStatus == Node.WallStatus.RIGHT) || //left wall
                    (map.get(current.x-1).get(current.y).wallStatus == Node.WallStatus.RIGHT && neighbor.wallStatus == Node.WallStatus.RIGHT) || //left wall
                    (map.get(current.x).get(current.y-1).wallStatus == Node.WallStatus.LEFT && current.wallStatus == Node.WallStatus.LEFT) || //left wall
                    (map.get(current.x-1).get(current.y).wallStatus == Node.WallStatus.RIGHT && map.get(current.x).get(current.y-1).wallStatus == Node.WallStatus.LEFT) || //left wall
                    (map.get(current.x-1).get(current.y).wallStatus == Node.WallStatus.TOP && neighbor.wallStatus == Node.WallStatus.RIGHT) || //corner away
                    (map.get(current.x).get(current.y-1).wallStatus == Node.WallStatus.LEFT && neighbor.wallStatus == Node.WallStatus.DOWN) || //corner away
                    (map.get(current.x-1).get(current.y).wallStatus == Node.WallStatus.TOP && map.get(current.x).get(current.y-1).wallStatus == Node.WallStatus.LEFT) || //corner away
                    (map.get(current.x-1).get(current.y).wallStatus == Node.WallStatus.RIGHT && current.wallStatus == Node.WallStatus.TOP) || //corner in
                    (map.get(current.x).get(current.y-1).wallStatus == Node.WallStatus.DOWN && current.wallStatus == Node.WallStatus.LEFT) || //corner in
                    (map.get(current.x-1).get(current.y).wallStatus == Node.WallStatus.RIGHT && map.get(current.x).get(current.y-1).wallStatus == Node.WallStatus.DOWN) || //corner In
                    (current.wallStatus == Node.WallStatus.TOP && neighbor.wallStatus == Node.WallStatus.DOWN) || //wall top
                    (map.get(current.x-1).get(current.y).wallStatus == Node.WallStatus.TOP && current.wallStatus == Node.WallStatus.TOP) || //WALL top
                    (map.get(current.x).get(current.y-1).wallStatus == Node.WallStatus.DOWN && neighbor.wallStatus == Node.WallStatus.DOWN) || //wall top
                    (map.get(current.x-1).get(current.y).wallStatus == Node.WallStatus.TOP && map.get(current.x).get(current.y-1).wallStatus == Node.WallStatus.DOWN)) { //wall top
                return false;
            }
            return true;
        }
        if (xDiff == 0 && yDiff == -1) { //Top
            if (current.wallStatus == Node.WallStatus.TOP || neighbor.wallStatus == Node.WallStatus.DOWN) {
                return false;
            }
            return true;
        }
        if (xDiff == 1 && yDiff == -1) { //topRight
            if ((current.wallStatus == Node.WallStatus.RIGHT && neighbor.wallStatus == Node.WallStatus.LEFT) || //right wall
                    (map.get(current.x+1).get(current.y).wallStatus == Node.WallStatus.LEFT && neighbor.wallStatus == Node.WallStatus.LEFT) || //right wall
                    (map.get(current.x).get(current.y-1).wallStatus == Node.WallStatus.RIGHT && current.wallStatus == Node.WallStatus.RIGHT) || //right wall
                    (map.get(current.x+1).get(current.y).wallStatus == Node.WallStatus.LEFT && map.get(current.x).get(current.y-1).wallStatus == Node.WallStatus.RIGHT) || //right wall
                    (map.get(current.x+1).get(current.y).wallStatus == Node.WallStatus.TOP && neighbor.wallStatus == Node.WallStatus.LEFT) || //corner away
                    (map.get(current.x).get(current.y-1).wallStatus == Node.WallStatus.RIGHT && neighbor.wallStatus == Node.WallStatus.DOWN) || //corner away
                    (map.get(current.x+1).get(current.y).wallStatus == Node.WallStatus.TOP && map.get(current.x).get(current.y-1).wallStatus == Node.WallStatus.RIGHT) || //corner away
                    (map.get(current.x+1).get(current.y).wallStatus == Node.WallStatus.LEFT && current.wallStatus == Node.WallStatus.TOP) || //corner in
                    (map.get(current.x).get(current.y-1).wallStatus == Node.WallStatus.DOWN && current.wallStatus == Node.WallStatus.RIGHT) || //corner in
                    (map.get(current.x+1).get(current.y).wallStatus == Node.WallStatus.LEFT && map.get(current.x).get(current.y-1).wallStatus == Node.WallStatus.DOWN) || //corner In
                    (current.wallStatus == Node.WallStatus.TOP && neighbor.wallStatus == Node.WallStatus.DOWN) || //wall top
                    (map.get(current.x+1).get(current.y).wallStatus == Node.WallStatus.TOP && current.wallStatus == Node.WallStatus.TOP) || //WALL top
                    (map.get(current.x).get(current.y-1).wallStatus == Node.WallStatus.DOWN && neighbor.wallStatus == Node.WallStatus.DOWN) || //wall top
                    (map.get(current.x+1).get(current.y).wallStatus == Node.WallStatus.TOP && map.get(current.x).get(current.y-1).wallStatus == Node.WallStatus.DOWN)) { //wall top
                return false;
            }
            return true;
        }

        return false;
    }



    public void addWall(int x, int y, SelectMode direction) {
        switch (direction){
            case WALLUP:
                map.get(x).get(y).wallStatus = Node.WallStatus.TOP;
                break;
            case WALLDOWN:
                map.get(x).get(y).wallStatus = Node.WallStatus.DOWN;
                break;
            case WALLRIGHT:
                map.get(x).get(y).wallStatus = Node.WallStatus.RIGHT;
                break;
            case WALLLEFT:
                map.get(x).get(y).wallStatus = Node.WallStatus.LEFT;
                break;
        }
        for (Node n: map.get(x).get(y).connectedNodes) {
            n.connectedNodes.removeIf(n2 -> !isConnected(n, n2));

        }
        map.get(x).get(y).connectedNodes.removeIf(neighbor -> !isConnected(map.get(x).get(y), neighbor));
    }


    public void initializeNeighbors() {
        for (int x = 0; x < 16; x++) {
            for (int y = 0; y < 16; y++) {
                if (x > 0) {
                    map.get(x).get(y).addNeighbor(map.get(x-1).get(y)); //Left
                    if (y > 0) {
                        map.get(x).get(y).addNeighbor(map.get(x).get(y-1)); //top
                        map.get(x).get(y).addNeighbor(map.get(x-1).get(y-1)); //topLeft
                    }
                    if (y < 15) {
                        map.get(x).get(y).addNeighbor(map.get(x).get(y+1)); //bottom
                        map.get(x).get(y).addNeighbor(map.get(x-1).get(y+1)); //bottomLeft
                    }
                    if (x < 15) {
                        map.get(x).get(y).addNeighbor(map.get(x+1).get(y)); //right
                        if (y > 0) {
                            map.get(x).get(y).addNeighbor(map.get(x+1).get(y-1)); //topRight
                        }
                        if (y < 15) {
                            map.get(x).get(y).addNeighbor(map.get(x+1).get(y+1)); //bottomRight
                        }
                    }
                } else {
                    map.get(x).get(y).addNeighbor(map.get(x+1).get(y)); //right
                    if (y > 0) {
                        map.get(x).get(y).addNeighbor(map.get(x).get(y-1)); //top
                        map.get(x).get(y).addNeighbor(map.get(x+1).get(y-1)); //topRight

                    }
                    if (y < 15) {
                        map.get(x).get(y).addNeighbor(map.get(x).get(y+1)); //bottom
                        map.get(x).get(y).addNeighbor(map.get(x+1).get(y+1)); //bottomRight

                    }
                }
            }
        }
    }


    public SelectMode getCurrentMode() {
        return currentMode;
    }

    public void setCurrentMode(SelectMode currentMode) {
        this.currentMode = currentMode;
    }
}
