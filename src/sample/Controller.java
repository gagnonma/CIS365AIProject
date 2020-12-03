package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.MenuButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.PickResult;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;


import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    StackPane root;
    @FXML
    MenuItem wallUp, wallDown, wallRight, wallLeft, wallRemove, water;
    @FXML
    MenuButton wall;
    @FXML
    Button save, load, start, goal, findPath, clearPath, thorMove, ironmanMove, capMove, enemyThorMove,
            enemyIronmanMove, enemyCapMove, thorMinus, ironmanMinus, capMinus, enemyThorMinus, enemyIronmanMinus,
            enemyCapMinus, thorPlus, ironmanPlus, capPlus, enemyThorPlus, enemyIronmanPlus, enemyCapPlus, thorTokenMinus,
            ironmanTokenMinus, capTokenMinus, enemyThorTokenMinus, enemyIronmanTokenMinus, enemyCapTokenMinus,
            thorTokenPlus, ironmanTokenPlus, capTokenPlus, enemyThorTokenPlus, enemyIronmanTokenPlus, enemyCapTokenPlus,
            getMove, precompute, endTurn, thorRemove, ironmanRemove, capRemove, enemyThorRemove, enemyIronmanRemove,
            enemyCapRemove;
    @FXML
    Text thorClick, ironmanClick, capClick, enemyThorClick, enemyIronmanClick, enemyCapClick, thorToken, ironmanToken,
            capToken, enemyThorToken, enemyIronmanToken, enemyCapToken;



    boolean showHoverCursor = true;
//    ImageView imageView = new ImageView( new Image( "https://upload.wikimedia.org/wikipedia/commons/c/c7/Pink_Cat_2.jpg",50,50,false,false));
//    ImageView thor = new ImageView( new Image( "file:thor.jpg",500,500,false,false));
    int rows = 16;
    int columns = 16;
    double width = 800;
    double height = 800;

    private Model model;
    Grid grid;

    String defaultMap = "0000000300000000000023030322000000030103000300000022402000224200030303000000130003000302222203000322030223000010030003300440222222220032244000000300032202220020030003000000030003230000002222400003030300300000000322432000000000030003003000000000000300300000";
    String defaultWaterMap = "0000111001110000000000100100000000000011110000000000000110000000000000011000000000000001000000000000001110000000000010100100000000001110010000000001100111000000000100001000000000010000010000000000000001000000000000000111000000000000000100000000000000000000";
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        model = new Model();

        // create grid
        grid = new Grid( columns, rows, width, height);

        MouseGestures mg = new MouseGestures();

        // fill grid
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {

                Cell cell = new Cell(column, row);

                mg.makePaintable(cell);

                grid.add(cell, column, row);
            }
        }

        root.getChildren().addAll(grid);

        grid.cells[model.thor.y][model.thor.x].setText("Thor", false);
        grid.cells[model.ironman.y][model.ironman.x].setText("Iron\nMan", false);
        grid.cells[model.captainAmerica.y][model.captainAmerica.x].setText("Cap", false);
        grid.cells[model.enemyThor.y][model.enemyThor.x].setText("Thor", true);
        grid.cells[model.enemyIronman.y][model.enemyIronman.x].setText("Iron\nMan", true);
        grid.cells[model.enemyCaptainAmerica.y][model.enemyCaptainAmerica.x].setText("Cap", true);

        grid.loadCells(defaultMap, defaultWaterMap);

        model.precomputeMovesMap();
        System.out.println("Done precomputing move map.");


        wallUp.setOnAction(event -> {
            wall.setText("Add Wall Up");
            model.setCurrentMode(Model.SelectMode.WALLUP);
        });

        wallDown.setOnAction(event -> {
            wall.setText("Add Wall Down");
            model.setCurrentMode(Model.SelectMode.WALLDOWN);
        });

        wallRight.setOnAction(event -> {
            wall.setText("Add Wall Right");
            model.setCurrentMode(Model.SelectMode.WALLRIGHT);
        });

        wallLeft.setOnAction(event -> {
            wall.setText("Add Wall Left");
            model.setCurrentMode(Model.SelectMode.WALLLEFT);
        });

        wallRemove.setOnAction(event -> {
            wall.setText("Remove Wall");
            model.setCurrentMode(Model.SelectMode.CLEAR);
        });

        water.setOnAction(event -> {
            wall.setText("Add Water");
            model.setCurrentMode(Model.SelectMode.WATER);
        });

        start.setOnAction(event -> {
            model.setCurrentMode(Model.SelectMode.START);
        });

        goal.setOnAction(event -> {
            model.setCurrentMode(Model.SelectMode.GOAL);
        });

        save.setOnAction(event -> {
           grid.saveCells();
        });

        load.setOnAction(event -> {
            grid.loadCells(defaultMap, defaultWaterMap);
        });

        findPath.setOnAction(event -> {
            ArrayList<sample.Node> path =  model.findPath(model.start, model.goal);
            for (sample.Node p: path) {
                grid.cells[p.y][p.x].setPath();
            }
        });

        clearPath.setOnAction(event -> {
            for (int r = 0; r < 16; r++){
                for (int c = 0; c < 16; c++){
                    grid.cells[r][c].getStyleClass().remove("path");
                }
            }
        });

        thorMove.setOnAction(event -> {
            model.setCurrentMode(Model.SelectMode.THOR);
        });

        ironmanMove.setOnAction(event -> {
            model.setCurrentMode(Model.SelectMode.IRONMAN);
        });

        capMove.setOnAction(event -> {
            model.setCurrentMode(Model.SelectMode.CAP_AMERICA);
        });

        enemyThorMove.setOnAction(event -> {
            model.setCurrentMode(Model.SelectMode.E_THOR);
        });

        enemyIronmanMove.setOnAction(event -> {
            model.setCurrentMode(Model.SelectMode.E_IRONMAN);
        });

        enemyCapMove.setOnAction(event -> {
            model.setCurrentMode(Model.SelectMode.E_CAP_AMERICA);
        });

        thorPlus.setOnAction(event -> {
            model.thor.incrementClick();
            thorClick.setText("" + model.thor.click);
        });

        ironmanPlus.setOnAction(event -> {
            model.ironman.incrementClick();
            ironmanClick.setText("" + model.ironman.click);
        });

        capPlus.setOnAction(event -> {
            model.captainAmerica.incrementClick();
            capClick.setText("" + model.captainAmerica.click);
        });

        enemyThorPlus.setOnAction(event -> {
            model.enemyThor.incrementClick();
            enemyThorClick.setText("" + model.enemyThor.click);
        });

        enemyIronmanPlus.setOnAction(event -> {
            model.enemyIronman.incrementClick();
            enemyIronmanClick.setText("" + model.enemyIronman.click);
        });

        enemyCapPlus.setOnAction(event -> {
            model.enemyCaptainAmerica.incrementClick();
            enemyCapClick.setText("" + model.enemyCaptainAmerica.click);
        });

        thorMinus.setOnAction(event -> {
            model.thor.decrementClick();
            thorClick.setText("" + model.thor.click);
        });

        ironmanMinus.setOnAction(event -> {
            model.ironman.decrementClick();
            ironmanClick.setText("" + model.ironman.click);
        });

        capMinus.setOnAction(event -> {
            model.captainAmerica.decrementClick();
            capClick.setText("" + model.captainAmerica.click);
        });

        enemyThorMinus.setOnAction(event -> {
            model.enemyThor.decrementClick();
            enemyThorClick.setText("" + model.enemyThor.click);
        });

        enemyIronmanMinus.setOnAction(event -> {
            model.enemyIronman.decrementClick();
            enemyIronmanClick.setText("" + model.enemyIronman.click);
        });

        enemyCapMinus.setOnAction(event -> {
            model.enemyCaptainAmerica.decrementClick();
            enemyCapClick.setText("" + model.enemyCaptainAmerica.click);
        });

        thorTokenPlus.setOnAction(event -> {
            model.thor.incrementTokens();
            thorToken.setText("" + model.thor.tokens);
        });

        ironmanTokenPlus.setOnAction(event -> {
            model.ironman.incrementTokens();
            ironmanToken.setText("" + model.ironman.tokens);
        });

        capTokenPlus.setOnAction(event -> {
            model.captainAmerica.incrementTokens();
            capToken.setText("" + model.captainAmerica.tokens);
        });

        enemyThorTokenPlus.setOnAction(event -> {
            model.enemyThor.incrementTokens();
            enemyThorToken.setText("" + model.enemyThor.tokens);
        });

        enemyIronmanTokenPlus.setOnAction(event -> {
            model.enemyIronman.incrementTokens();
            enemyIronmanToken.setText("" + model.enemyIronman.tokens);
        });

        enemyCapTokenPlus.setOnAction(event -> {
            model.enemyCaptainAmerica.incrementTokens();
            enemyCapToken.setText("" + model.enemyCaptainAmerica.tokens);
        });

        thorTokenMinus.setOnAction(event -> {
            model.thor.decrementTokens();
            thorToken.setText("" + model.thor.tokens);
        });

        ironmanTokenMinus.setOnAction(event -> {
            model.ironman.decrementTokens();
            ironmanToken.setText("" + model.ironman.tokens);
        });

        capTokenMinus.setOnAction(event -> {
            model.captainAmerica.decrementTokens();
            capToken.setText("" + model.captainAmerica.tokens);
        });

        enemyThorTokenMinus.setOnAction(event -> {
            model.enemyThor.decrementTokens();
            enemyThorToken.setText("" + model.enemyThor.tokens);
        });

        enemyIronmanTokenMinus.setOnAction(event -> {
            model.enemyIronman.decrementTokens();
            enemyIronmanToken.setText("" + model.enemyIronman.tokens);
        });

        enemyCapTokenMinus.setOnAction(event -> {
            model.enemyCaptainAmerica.decrementTokens();
            enemyCapToken.setText("" + model.enemyCaptainAmerica.tokens);
        });

        //todo ugly test
        getMove.setOnAction(event -> {
            try {
                model.getBestMove();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        precompute.setOnAction(event -> {
            model.precomputeMovesMap();
            System.out.println("Done precomputing move map.");
        });

        endTurn.setOnAction(event -> {
            model.thor.costedActions = 0;
            model.ironman.costedActions = 0;
            model.captainAmerica.costedActions = 0;
            model.enemyThor.costedActions = 0;
            model.enemyIronman.costedActions = 0;
            model.enemyCaptainAmerica.costedActions = 0;
        });

        thorRemove.setOnAction( event -> {
            grid.cells[model.thor.y][model.thor.x].setText("",false);
            model.removeHero(model.thor);
        });

        ironmanRemove.setOnAction( event -> {
            grid.cells[model.ironman.y][model.ironman.x].setText("",false);
            model.removeHero(model.ironman);
        });

        capRemove.setOnAction( event -> {
            grid.cells[model.captainAmerica.y][model.captainAmerica.x].setText("",false);
            model.removeHero(model.captainAmerica);
        });

        enemyThorRemove.setOnAction( event -> {
            grid.cells[model.enemyThor.y][model.enemyThor.x].setText("",false);
            model.removeHero(model.enemyThor);
        });

        enemyIronmanRemove.setOnAction( event -> {
            grid.cells[model.enemyIronman.y][model.enemyIronman.x].setText("",false);
            model.removeHero(model.enemyIronman);
        });

        enemyCapRemove.setOnAction( event -> {
            grid.cells[model.enemyCaptainAmerica.y][model.enemyCaptainAmerica.x].setText("",false);
            model.removeHero(model.enemyCaptainAmerica);
        });


    }

    private class Grid extends Pane {

        int rows;
        int columns;

        double width;
        double height;

        Cell[][] cells;

        public Grid( int columns, int rows, double width, double height) {

            this.columns = columns;
            this.rows = rows;
            this.width = width;
            this.height = height;

            cells = new Cell[rows][columns];

        }

        /**
         * Add cell to array and to the UI.
         */
        public void add(Cell cell, int column, int row) {

            cells[row][column] = cell;

            double w = width / columns;
            double h = height / rows;
            double x = w * column;
            double y = h * row;

            cell.setLayoutX(x);
            cell.setLayoutY(y);
            cell.setPrefWidth(w);
            cell.setPrefHeight(h);

            getChildren().add(cell);

        }

        public Cell getCell(int column, int row) {
            return cells[row][column];
        }

        public String saveCells() {
            String result = "";
            for( int row=0; row < rows; row++) {
                for( int col=0; col < columns; col++) {
                    result += cells[row][col].wall;
                }
            }
            String waterString = "";
            for( int row=0; row < rows; row++) {
                for( int col=0; col < columns; col++) {
                    if (cells[row][col].water){
                        waterString += "1";
                    }else {
                        waterString += "0";
                    }
                }
            }
            System.out.println(result);
            System.out.println(waterString);
            return result;
        }

        public void loadCells(String map, String waterMap) {
            String y = "";
            int i = 0;
            for( int row=0; row < rows; row++) {
                for( int col=0; col < columns; col++) {
                    int x = Character.getNumericValue(map.charAt(i));
                    y += "" + x;
                    switch (x) {
                        case 1:
                            model.addWall(col,row, Model.SelectMode.WALLUP);
                            cells[row][col].addWallUp();
                            break;
                        case 2:
                            model.addWall(col,row, Model.SelectMode.WALLDOWN);
                            cells[row][col].addWallDown();
                            break;
                        case 3:
                            model.addWall(col,row, Model.SelectMode.WALLRIGHT);
                            cells[row][col].addWallRight();
                            break;
                        case 4:
                            model.addWall(col,row, Model.SelectMode.WALLLEFT);
                            cells[row][col].addWallLeft();
                            break;
                        default:
                            break;
                    }
                    i++;
                }
            }
            i = 0;
            for( int row=0; row < rows; row++) {
                for (int col = 0; col < columns; col++) {
                    model.setCurrentMode(Model.SelectMode.WATER);
                    if (waterMap.charAt(i) == '1') {
                        model.selectNode(col,row);
                        cells[row][col].addWater();
                    }
                    i++;
                }
            }
            System.out.println(y);
        }


        /**
         * Unhighlight all cells
         */
        public void unhighlight() {
            for( int row=0; row < rows; row++) {
                for( int col=0; col < columns; col++) {
                    cells[row][col].unhighlight();
                }
            }
        }
    }

    private class Cell extends StackPane {

        int column;
        int row;
        char wall;
        boolean water;
        String prevText;
        String currText;
        Boolean isCurrTextEnemy;
        Boolean isPrevTextEnemy;

        public Cell(int column, int row) {


            this.column = column;
            this.row = row;
            this.wall = '0';
            this.prevText = "";
            this.currText = "";
            this.isPrevTextEnemy = false;
            this.isCurrTextEnemy  = false;
            getChildren().add(new Text(""));

            getStyleClass().add("cell");

            setOpacity(0.9);
        }

        public void setPath() {
            getStyleClass().remove("cell-highlight");
            getStyleClass().add("path");
        }

        public void addWallUp() {
            getStyleClass().removeAll("wallLeft", "wallRight", "wallDown");
            getStyleClass().add("wallUp");
            this.wall = '1';
        }

        public void addWallDown() {
            getStyleClass().removeAll("wallUp", "wallRight", "wallLeft");
            getStyleClass().add("wallDown");
            this.wall = '2';
        }

        public void addWallRight() {
            getStyleClass().removeAll("wallUp", "wallLeft", "wallDown");
            getStyleClass().add("wallRight");
            this.wall = '3';
        }

        public void addWallLeft() {
            getStyleClass().removeAll("wallUp", "wallRight", "wallDown");
            getStyleClass().add("wallLeft");
            this.wall = '4';
        }

        public void removeWall() {
            getStyleClass().removeAll("wallUp", "wallRight", "wallDown", "wallLeft");
            this.wall = '0';
        }

        public void addWater() {
            getStyleClass().add("cell-highlight");
            this.water = true;
        }

        public void setText(String s, Boolean isEnemy) {
            this.prevText = currText;
            this.isPrevTextEnemy = this.isCurrTextEnemy;
            this.currText = s;
            Text newText =  new Text(s);
            if (isEnemy) {
                this.isCurrTextEnemy = true;
                newText.setFill(Color.RED);
            }else {
                this.isCurrTextEnemy = false;
            }
            this.getChildren().set(0, newText);
        }



        public void setStart() {
            getStyleClass().add("start");
        }

        public void setGoal() {
            getStyleClass().add("goal");
        }

        public void highlight() {
            // ensure the style is only once in the style list
            getStyleClass().remove("cell-highlight");

            // add style
            getStyleClass().add("cell-highlight");
        }


        public void unhighlight() {
            getStyleClass().remove("cell-highlight");
        }

        public void hoverHighlight() {
            // ensure the style is only once in the style list
            getStyleClass().remove("cell-hover-highlight");

            // add style
            getStyleClass().add("cell-hover-highlight");
        }

        public void hoverUnhighlight() {
            getStyleClass().remove("cell-hover-highlight");
        }

        public String toString() {
            return this.column + "/" + this.row;
        }
    }

    public class MouseGestures {

        public void makePaintable( Node node) {


            // that's all there is needed for hovering, the other code is just for painting
            if( showHoverCursor) {
                node.hoverProperty().addListener(new ChangeListener<Boolean>(){

                    @Override
                    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {

                        //System.out.println( observable + ": " + newValue);

                        if( newValue) {
                            ((Cell) node).setText(model.map.get(((Cell) node).column).get(((Cell) node).row).toString().substring(11,model.map.get(((Cell) node).column).get(((Cell) node).row).toString().length()-2), false);
                            ((Cell) node).hoverHighlight();
                        } else {
                            ((Cell) node).setText(((Cell) node).prevText,((Cell) node).isPrevTextEnemy);
                            ((Cell) node).hoverUnhighlight();
                        }

//                        System.out.println( model.map.get(((Cell) node).column).get(((Cell) node).row));
//                        System.out.println(node + " : " +  model.map.get(((Cell) node).column).get(((Cell) node).row) + " : " + model.map.get(((Cell) node).column).get(((Cell) node).row).connectedNodes);
//                        for( String s: node.getStyleClass())
//                            System.out.println( node + ": " + s);
                    }

                });
            }

            node.setOnMousePressed( onMousePressedEventHandler);
//            node.setOnDragDetected( onDragDetectedEventHandler);
//            node.setOnMouseDragEntered(onMouseDragEnteredEventHandler);

        }

        EventHandler<MouseEvent> onMousePressedEventHandler = event -> {

            Cell cell = (Cell) event.getSource();

            switch (model.getCurrentMode()) {
                case WALLUP:
                    cell.addWallUp();
                    break;
                case WALLDOWN:
                    cell.addWallDown();
                    break;
                case WALLRIGHT:
                    cell.addWallRight();
                    break;
                case WALLLEFT:
                    cell.addWallLeft();
                    break;
                case START:
                    cell.setStart();
                    break;
                case GOAL:
                    cell.setGoal();
                    break;
                case CLEAR:
                    cell.removeWall();
//                    if( event.isPrimaryButtonDown()) {
//                        cell.highlight();
//                    } else if( event.isSecondaryButtonDown()) {
//                        cell.unhighlight();
//                    }
                    break;
                case THOR:
                    if (!model.thor.dead) {
                        grid.cells[model.thor.y][model.thor.x].setText("", false);
                    }
                    cell.setText("Thor",false);
                    cell.prevText = "Thor";
                    break;
                case IRONMAN:
                    if (!model.ironman.dead) {
                        grid.cells[model.ironman.y][model.ironman.x].setText("", false);
                    }
                    cell.setText("Iron\nMan",false);
                    cell.prevText = "Iron\nMan";
                    break;
                case CAP_AMERICA:
                    if (!model.captainAmerica.dead) {
                        grid.cells[model.captainAmerica.y][model.captainAmerica.x].setText("", false);
                    }
                    cell.setText("Cap",false);
                    cell.prevText = "Cap";
                    break;
                case E_THOR:
                    if (!model.enemyThor.dead) {
                        grid.cells[model.enemyThor.y][model.enemyThor.x].setText("", false);
                    }
                    cell.setText("Thor",true);
                    cell.prevText = "Thor";
                    cell.isPrevTextEnemy = true;
                    break;
                case E_IRONMAN:
                    if (!model.enemyIronman.dead) {
                        grid.cells[model.enemyIronman.y][model.enemyIronman.x].setText("", false);
                    }
                    cell.setText("Iron\nMan",true);
                    cell.prevText = "Iron\nMan";
                    cell.isPrevTextEnemy = true;
                    break;
                case E_CAP_AMERICA:
                    if (!model.enemyCaptainAmerica.dead) {
                        grid.cells[model.enemyCaptainAmerica.y][model.enemyCaptainAmerica.x].setText("", false);
                    }
                    cell.setText("Cap",true);
                    cell.prevText = "Cap";
                    cell.isPrevTextEnemy = true;
                    break;
                case WATER:
                    cell.addWater();
                    break;
                default:
                    break;
            }
            model.selectNode(cell.column,cell.row);
//            HashSet<sample.Node> available = model.getPrecomputedReachableNodes(model.thor);
//            for (sample.Node n : available){
//                grid.cells[n.y][n.x].setPath();
//            }
            //ArrayList<sample.Node> path =  model.getReachableNodes(model.thor);
            //for (sample.Node p: path) {
                //grid.cells[p.y][p.x].setPath();
            //}


        };

//        EventHandler<MouseEvent> onMouseDraggedEventHandler = event -> {
//
//            PickResult pickResult = event.getPickResult();
//            Node node = pickResult.getIntersectedNode();
//
//            if( node instanceof Cell) {
//
//                Cell cell = (Cell) node;
//
//                if( event.isPrimaryButtonDown()) {
//                    cell.highlight();
//                } else if( event.isSecondaryButtonDown()) {
//                    cell.unhighlight();
//                }
//
//            }
//
//        };
//
//        EventHandler<MouseEvent> onMouseReleasedEventHandler = event -> {
//        };
//
//        EventHandler<MouseEvent> onDragDetectedEventHandler = event -> {
//
//            Cell cell = (Cell) event.getSource();
//            cell.startFullDrag();
//
//        };
//
//        EventHandler<MouseEvent> onMouseDragEnteredEventHandler = event -> {
//
//            Cell cell = (Cell) event.getSource();
//
//            if( event.isPrimaryButtonDown()) {
//                cell.highlight();
//            } else if( event.isSecondaryButtonDown()) {
//                cell.unhighlight();
//            }
//
//        };

    }
}
