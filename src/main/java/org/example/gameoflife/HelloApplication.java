package org.example.gameoflife;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Random;

public class HelloApplication extends Application {
    public static void main(String[] args) {
        launch();
    }

    // GLOBAL STUFF
    VBox root;
    boolean gridMatrix[][];
    GridPane grid;

    // Global parameters
    final int CELL_SIZE = 5;
    final int GRID_NxN = 100;
    final int cycleMs = 400;

    @Override
    public void start(Stage stage) throws IOException {
        root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));

        // Label
        Label label = new Label("Game of Life");
        label.setStyle("-fx-font-size: 20; -fx-font-weight: bold;");

        // Grid
        createGrid();

        // Set thread
        loop();

        root.getChildren().addAll(label, grid);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void loop(){
        Thread thread = new Thread(() -> {
            try {
                while (true) { // Loop indefinitely
                    Thread.sleep(cycleMs);
                    boolean[][] nextGridMatrix = new boolean[GRID_NxN][GRID_NxN];

                    for (int i = 0; i < gridMatrix.length; i++) {
                        for (int j = 0; j < gridMatrix[i].length; j++) {
                            boolean alive = gridMatrix[i][j];
                            int neighbors = countNeighbors(i, j);

                            if (alive) {
                                nextGridMatrix[i][j] = neighbors == 2 || neighbors == 3;
                            } else {
                                nextGridMatrix[i][j] = neighbors == 3;
                            }
                        }
                    }

                    gridMatrix = nextGridMatrix;

                    // Update the UI on the JavaFX Application Thread
                    Platform.runLater(this::refreshGridPane);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread.setDaemon(true); // Set the thread as a daemon thread
        thread.start();
    }

    // **** There's something wrong here
    public int countNeighbors(int x, int y){
        int counter = 0;
        for (int i = x-1; i <= x+1; i++) {
            if(i < 0 || i >= GRID_NxN){
                continue;
            }
            for (int j = y-1; j <= y+1; j++) {
                if(i == x && j == y){
                    continue;
                }
                if(j < 0 || j >= GRID_NxN){
                    continue;
                }
                if(gridMatrix[i][j])
                    counter++;
            }
        }
        return counter;
    }

    public void createGrid(){
        gridMatrix = new boolean[GRID_NxN][GRID_NxN];
        grid = new GridPane();
        setGridmatrixRandomly();
        refreshGridPane();
    }

    public void refreshGridPane(){
        grid.getChildren().clear();
        for (int i = 0; i < gridMatrix.length; i++) {
            for (int j = 0; j < gridMatrix[i].length; j++) {
                if(gridMatrix[i][j])
                    grid.add(getWhitePane(), j, i);
                else
                    grid.add(getBlackPane(), j, i);
            }
        }
    }

    public void setGridmatrixRandomly(){
        Random random = new Random();
        for (int i = 0; i < gridMatrix.length; i++) {
            for (int j = 0; j < gridMatrix[i].length; j++) {
                if(random.nextInt(2) == 0)
                    gridMatrix[i][j] = true;
            }
        }
    }

    public Pane getWhitePane(){
        Pane pane = new Pane();
        pane.setBorder(Border.stroke(Color.LIGHTGRAY));
        pane.setPrefSize(CELL_SIZE, CELL_SIZE);
        pane.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
        return pane;
    }

    public Pane getBlackPane(){
        Pane pane = new Pane();
        pane.setBorder(Border.stroke(Color.GRAY));
        pane.setPrefSize(CELL_SIZE, CELL_SIZE);
        pane.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
        return pane;
    }
}