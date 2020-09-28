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
import javafx.util.Pair;


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

    boolean showHoverCursor = true;
//    ImageView imageView = new ImageView( new Image( "https://upload.wikimedia.org/wikipedia/commons/c/c7/Pink_Cat_2.jpg",50,50,false,false));
//    ImageView thor = new ImageView( new Image( "file:thor.jpg",500,500,false,false));
    int rows = 16;
    int columns = 16;
    double width = 800;
    double height = 800;

    private Model model;

    String defaultMap = "0000000300000000000023030322000000030103000300000022402000224200030303000000130003000302222203000322030223000010030003300440222222220032244000000300032200000020030003000000030003230000002222400003030300300000000322432000000000030003003000000000000300300000";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        model = new Model();

        // create grid
        Grid grid = new Grid( columns, rows, width, height);

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
            grid.loadCells(defaultMap);
        });

        findPath.setOnAction(event -> {
            ArrayList<Pair<Integer, Integer>> path =  model.findPath();
            for (Pair<Integer,Integer> p: path) {
                grid.cells[p.getValue()][p.getKey()].setPath();
            }
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
            System.out.println(result);
            return result;
        }

        public void loadCells(String map) {
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

        public Cell(int column, int row) {


            this.column = column;
            this.row = row;
            this.wall = '0';

            getStyleClass().add("cell");

            setOpacity(0.9);
        }

        public void setPath() {
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

            model.selectNode(cell.column,cell.row);
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
            }


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
