package sample;

import javafx.util.Pair;

import java.lang.reflect.Array;
import java.util.*;

public class Model {
    String intToLetter = "abcdefghijklmnop";

    enum SelectMode {
            WALLUP, WALLDOWN, WALLRIGHT, WALLLEFT, START, GOAL, CLEAR,
        THOR, IRONMAN, CAP_AMERICA, E_THOR, E_IRONMAN, E_CAP_AMERICA, WATER
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

    GameState mainGameState;
    MonteCarloSimulator monteCarloSimulator;
    HashMap<Node, HashMap<Integer, HashSet<Node>>> precomputedMap;


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


        thor = new Thor(map.get(0).get(0), "Friendly Thor");
        map.get(0).get(0).occupant = thor;
        enemyThor = new Thor(map.get(15).get(15), "Enemy Thor");
        map.get(15).get(15).occupant = enemyThor;
        ironman = new Ironman(map.get(0).get(1), "Friendly Ironman");
        map.get(0).get(1).occupant = ironman;
        enemyIronman =  new Ironman(map.get(15).get(14), "Enemy Ironman");
        map.get(15).get(14).occupant = enemyIronman;
        captainAmerica = new CaptainAmerica(map.get(1).get(0), "Friendly Captain America");
        map.get(1).get(0).occupant = captainAmerica;
        enemyCaptainAmerica = new CaptainAmerica(map.get(14).get(15), "Enemy Captain America");
        map.get(14).get(15).occupant = enemyCaptainAmerica;


        monteCarloSimulator = new MonteCarloSimulator();

    }

    public void getBestMove() {
        ArrayList<Hero> friendlies = new ArrayList<Hero>();
        friendlies.add(thor);
        friendlies.add(ironman);
        friendlies.add(captainAmerica);
        ArrayList<Hero> baddies = new ArrayList<Hero>();
        baddies.add(enemyThor);
        baddies.add(enemyIronman);
        baddies.add(enemyCaptainAmerica);
        mainGameState = new GameState(friendlies, baddies, this);
        monteCarloSimulator.getBestActionFromGameState(mainGameState);
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
                deleteWall(x,y);
                break;
            case WATER:
                map.get(x).get(y).isWater = true;
                break;
            case THOR:
                moveHero(thor, x, y);
                break;
            case IRONMAN:
                moveHero(ironman, x, y);
                break;
            case CAP_AMERICA:
                moveHero(captainAmerica, x, y);
                break;
            case E_THOR:
                moveHero(enemyThor, x, y);
                break;
            case E_IRONMAN:
                moveHero(enemyIronman, x, y);
                break;
            case E_CAP_AMERICA:
                moveHero(enemyCaptainAmerica, x, y);

                break;
            default:
                addWall(x,y,currentMode);
        }
    }

    public void moveHero(Hero myHero, int x, int y) {
        map.get(myHero.x).get(myHero.y).occupant = null;
        myHero.setLocation(x,y);
        map.get(myHero.x).get(myHero.y).occupant = myHero;
    }

    public ArrayList<Node> findPath(Node start, Node goal) {
        ArrayList<Node> queue = new ArrayList<>();
        queue.add(start);
        ArrayList<Node> visited = new ArrayList<>();
        ArrayList<Node> inspected = new ArrayList<>();
        inspected.add(start);
        while (!queue.isEmpty()) {
            Node currentNode = queue.remove(0);
            visited.add(currentNode);
            if (currentNode == goal) {
                return calcPath(start, currentNode);
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

    public boolean inRange(Node start, Node goal, int range) {
        double deltaX = goal.x - start.x;
        double deltaY = goal.y - start.y;
        double incrementX = deltaX / 10;
        double incrementY = deltaY / 10;
        if ( ! (calcDistance(start, goal) <= (double) range) ) {
            return false;
        }
        ArrayList<Node> inLine = new ArrayList<>();
        inLine.add(start);
        for (int i = 0; i < 10; i++) {
            Node temp = map.get((int) (start.x + incrementX*i)).get((int) (start.y + incrementY*i));
            if (!inLine.contains(temp)) {
                inLine.add(temp);
            }
        }
        for (int i = 0; i < inLine.size()-1; i++){
            if (!inLine.get(i).connectedNodes.contains(inLine.get(i+1))){
                return false;
            }
        }
        return true;
    }

    private double calcDistance (Node curr, Node target) {
        return Math.sqrt(Math.pow(curr.x - target.x, 2) + Math.pow(curr.y - target.y, 2));
    }

    private ArrayList<Node> calcPath(Node start, Node currentNode) {
        ArrayList<Node> path = new ArrayList<>();
        Node temp = currentNode;
        while (temp != start) {
            path.add(temp);
            temp = temp.parent;
        }
        path.add(start);

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

    public void deleteWall(int x, int y) {
        map.get(x).get(y).wallStatus = Node.WallStatus.NOWALL;
        //For each node surrounding the selected node including the selected node
        for (int c = x-1; c <= x+1; c++) {
            for (int r = y - 1; r <= y + 1; r++) {
                //check all of its surrounding nodes to see if it is connected
                for (int c2 = c-1; c2 <= c+1; c2++) {
                    for (int r2 = r - 1; r2 <= r + 1; r2++) {
                        if (c > -1 && c2 > -1 && r > -1 && r2 > -1 && c < 16 && c2 < 16 && r < 16 && r2 < 16){
                            Node temp = map.get(c).get(r);
                            Node neighbor = map.get(c2).get(r);
                            if (!temp.connectedNodes.contains(neighbor)){
                                if (isConnected(temp, neighbor)){
                                    temp.addNeighbor(neighbor);
                                }
                            }
                        }
                    }
                }
            }
        }
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

    public void precomputeMovesMap() {
        ArrayList<Node> allNodes = new ArrayList<Node>();
        for (int i = 0; i<map.size(); i++) {
            for (int j = 0; j < map.get(0).size(); j++) {
                allNodes.add(map.get(i).get(j));
            }
        }
        HashMap<Node, HashMap<Integer, HashSet<Node>>> out = new HashMap<Node, HashMap<Integer, HashSet<Node>>>();
        for (Node node : allNodes) {
            HashMap<Integer, HashSet<Node>> value = new HashMap<Integer, HashSet<Node>>();
            for (int speed = 2; speed < 13; speed++) {
                value.put(speed, getReachableNodes(node, speed));
            }
            out.put(node, value);
        }
        System.out.println(out);
        precomputedMap = out;
        //return out;
    }

    public HashSet<Node> getPrecomputedReachableNodes(Hero hero) {
        return precomputedMap.get(hero.node).get(hero.speed[hero.click]);
    }

    public HashSet<Node> getReachableNodes(Node node, int speed) {
        int distance = speed;
        if (node.isWater)
            distance/=2;
        HashSet<Node> output = new HashSet<Node>();
        HashSet<Node> lastLayer = new HashSet<Node>();
        ;
        output.add(node);
        lastLayer.add(node);

        for (int i = 0; i < distance; i++) {
            HashSet<Node> currentLayer = new HashSet<Node>();
            for (Node nodeInner : lastLayer) {
                for (Node child : nodeInner.connectedNodes) {
                    if (!output.contains(child)) {
                        if (child.isWater && !nodeInner.isWater && !node.isWater) {
                            output.add(child);
                        }
                        else {
                            currentLayer.add(child);
                        }
                    }
                }
            }

            for (Node nodeInner : currentLayer) {
                output.add(nodeInner);
            }
            lastLayer = currentLayer;
        }
        return output;
    }

    public HashSet<Node> getReachableNodes(Hero myHero){
        int distance = myHero.speed[myHero.click];
        HashSet<Node> output = new HashSet<Node>();
        HashSet<Node> lastLayer = new HashSet<Node>();;
        output.add(myHero.node);
        lastLayer.add(myHero.node);

        for (int i = 0; i < distance; i++) {
            HashSet<Node> currentLayer = new HashSet<Node>();
            for (Node node : lastLayer) {
                for (Node child : node.connectedNodes) {
                    if (!output.contains(child)) {
                        if (child.isWater)
                        currentLayer.add(child);
                    }
                }
            }

            for (Node node : currentLayer) {
                output.add(node);
            }
            lastLayer = currentLayer;
        }
        return output;
        /*/ArrayList<Node> result = new ArrayList<>();
        return myHero.node.connectedNodes;

        int mySpeed = myHero.speed[myHero.click];
        Node goal;
        Node start = map.get(myHero.x).get(myHero.y);
        if (!myHero.canFly && start.isWater) {
            mySpeed /= 2;
        }
        int startY = myHero.y - mySpeed;
        int endY = myHero.y + mySpeed;
        int startX = myHero.x - mySpeed;
        int endX = myHero.x + mySpeed;
        for (int y = startY; y < endY; y++) {
            for (int x = startX; x < endX; x++) {
                if (x >= 0 && x <= 15 && y >=0 && y <= 15) {
                    goal = map.get(x).get(y);
                    ArrayList<Node> temp = findPath(start,goal);
                    if (temp != null) {
                        if (temp.size() <= mySpeed && goal.occupant == null) {
                            if (myHero.canFly || ((!start.isWater && goal.isWater && getNumOfWaterInPath(temp) == 1) ||
                                    (!start.isWater && !goal.isWater && getNumOfWaterInPath(temp) == 0) || start.isWater))
                                result.add(map.get(x).get(y));
                        }
                    }
                }
            }
        }
        return result;/*/
    }

    public int getNumOfWaterInPath(ArrayList<Node> path) {
        int count = 0;
        for (Node p: path) {
            if (p.isWater)
                count++;
        }
        return count;
    }

    public SelectMode getCurrentMode() {
        return currentMode;
    }

    public void setCurrentMode(SelectMode currentMode) {
        this.currentMode = currentMode;
    }
}
