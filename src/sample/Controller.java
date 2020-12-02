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
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    StackPane root;
    @FXML
    MenuItem wallUp;
    @FXML
    MenuItem wallDown;
    @FXML
    MenuItem wallRight;
    @FXML
    MenuItem wallLeft;
    @FXML
    MenuItem wallRemove;
    @FXML
    MenuItem water;
    @FXML
    MenuButton wall;
    @FXML
    Button save;
    @FXML
    Button load;
    @FXML
    Button start;
    @FXML
    Button goal;
    @FXML
    Button findPath;
    @FXML
    Button thorMove;
    @FXML
    Button ironmanMove;
    @FXML
    Button capMove;
    @FXML
    Button enemyThorMove;
    @FXML
    Button enemyIronmanMove;
    @FXML
    Button enemyCapMove;
    @FXML
    Button thorMinus;
    @FXML
    Button ironmanMinus;
    @FXML
    Button capMinus;
    @FXML
    Button enemyThorMinus;
    @FXML
    Button enemyIronmanMinus;
    @FXML
    Button enemyCapMinus;
    @FXML
    Button thorPlus;
    @FXML
    Button ironmanPlus;
    @FXML
    Button capPlus;
    @FXML
    Button enemyThorPlus;
    @FXML
    Button enemyIronmanPlus;
    @FXML
    Button enemyCapPlus;
    @FXML
    Text thorClick;
    @FXML
    Text ironmanClick;
    @FXML
    Text capClick;
    @FXML
    Text enemyThorClick;
    @FXML
    Text enemyIronmanClick;
    @FXML
    Text enemyCapClick;
    @FXML
    Button thorTokenMinus;
    @FXML
    Button ironmanTokenMinus;
    @FXML
    Button capTokenMinus;
    @FXML
    Button enemyThorTokenMinus;
    @FXML
    Button enemyIronmanTokenMinus;
    @FXML
    Button enemyCapTokenMinus;
    @FXML
    Button thorTokenPlus;
    @FXML
    Button ironmanTokenPlus;
    @FXML
    Button capTokenPlus;
    @FXML
    Button enemyThorTokenPlus;
    @FXML
    Button enemyIronmanTokenPlus;
    @FXML
    Button enemyCapTokenPlus;
    @FXML
    Text thorToken;
    @FXML
    Text ironmanToken;
    @FXML
    Text capToken;
    @FXML
    Text enemyThorToken;
    @FXML
    Text enemyIronmanToken;
    @FXML
    Text enemyCapToken;
    @FXML
    Button thorAction;
    @FXML
    Button ironmanAction;
    @FXML
    Button capAction;


    boolean showHoverCursor = true;
//    ImageView imageView = new ImageView( new Image( "https://upload.wikimedia.org/wikipedia/commons/c/c7/Pink_Cat_2.jpg",50,50,false,false));
//    ImageView thor = new ImageView( new Image( "file:thor.jpg",500,500,false,false));
    int rows = 16;
    int columns = 16;
    double width = 800;
    double height = 800;

    private Model model;
    Grid grid;

    String defaultMap = "0000000300000000000023030322000000030103000300000022402000224200030303000000130003000302222203000322030223000010030003300440222222220032244000000300032200000020030003000000030003230000002222400003030300300000000322432000000000030003003000000000000300300000";
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

        public Cell(int column, int row) {


            this.column = column;
            this.row = row;
            this.wall = '0';
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
            getStyleClass().removeAll(getStyleClass());
            getStyleClass().add("cell");
            this.wall = '0';
        }

        public void addWater() {
            getStyleClass().add("cell-highlight");
            this.water = true;
        }

        public void setText(String s, Boolean isEnemy) {
            Text newText =  new Text(s);
            if (isEnemy)
                newText.setFill(Color.RED);
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

                        System.out.println( observable + ": " + newValue);

                        if( newValue) {
                            ((Cell) node).hoverHighlight();
                        } else {
                            ((Cell) node).hoverUnhighlight();
                        }

                        System.out.println(node + " : " +  model.map.get(((Cell) node).column).get(((Cell) node).row) + " : " + model.map.get(((Cell) node).column).get(((Cell) node).row).connectedNodes);
//                        for( String s: node.getStyleClass())
//                            System.out.println( node + ": " + s);
                    }

                });
            }

            node.setOnMousePressed( onMousePressedEventHandler);
            node.setOnDragDetected( onDragDetectedEventHandler);
            node.setOnMouseDragEntered(onMouseDragEnteredEventHandler);

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
                case THOR:
                    grid.cells[model.thor.y][model.thor.x].setText("",false);
                    cell.setText("Thor",false);
                    break;
                case IRONMAN:
                    grid.cells[model.ironman.y][model.ironman.x].setText("",false);
                    cell.setText("Iron\nMan",false);
                    break;
                case CAP_AMERICA:
                    grid.cells[model.captainAmerica.y][model.captainAmerica.x].setText("",false);
                    cell.setText("Cap",false);
                    break;
                case E_THOR:
                    grid.cells[model.enemyThor.y][model.enemyThor.x].setText("",false);
                    cell.setText("Thor",true);
                    break;
                case E_IRONMAN:
                    grid.cells[model.enemyIronman.y][model.enemyIronman.x].setText("",false);
                    cell.setText("Iron\nMan",true);
                    break;
                case E_CAP_AMERICA:
                    grid.cells[model.enemyCaptainAmerica.y][model.enemyCaptainAmerica.x].setText("",false);
                    cell.setText("Cap",true);
                    break;
                case WATER:
                    cell.addWater();
            }
            model.selectNode(cell.column,cell.row);
            //ArrayList<sample.Node> path =  model.getReachableNodes(model.thor);
            //for (sample.Node p: path) {
                //grid.cells[p.y][p.x].setPath();
            //}


        };

        EventHandler<MouseEvent> onMouseDraggedEventHandler = event -> {

            PickResult pickResult = event.getPickResult();
            Node node = pickResult.getIntersectedNode();

            if( node instanceof Cell) {

                Cell cell = (Cell) node;

                if( event.isPrimaryButtonDown()) {
                    cell.highlight();
                } else if( event.isSecondaryButtonDown()) {
                    cell.unhighlight();
                }

            }

        };

        EventHandler<MouseEvent> onMouseReleasedEventHandler = event -> {
        };

        EventHandler<MouseEvent> onDragDetectedEventHandler = event -> {

            Cell cell = (Cell) event.getSource();
            cell.startFullDrag();

        };

        EventHandler<MouseEvent> onMouseDragEnteredEventHandler = event -> {

            Cell cell = (Cell) event.getSource();

            if( event.isPrimaryButtonDown()) {
                cell.highlight();
            } else if( event.isSecondaryButtonDown()) {
                cell.unhighlight();
            }

        };

    }
}
