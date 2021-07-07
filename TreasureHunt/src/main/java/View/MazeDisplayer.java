package View;

import algorithms.mazeGenerators.Maze;
import algorithms.search.AState;
import algorithms.search.MazeState;
import algorithms.search.Solution;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Random;

/**
 * Displays the maze
 */

public class MazeDisplayer extends Canvas {

    private Maze maze;
    private Solution solution;

    private boolean[][] randomAddings;
    // player position:
    private int playerRow;
    private int playerCol;
    // wall and player images:
    StringProperty imageFileNameWall = new SimpleStringProperty();
    StringProperty imageFileNamePlayer = new SimpleStringProperty();
    StringProperty imageFileNameSea = new SimpleStringProperty();
    StringProperty imageFileNameSolution = new SimpleStringProperty();
    StringProperty imageFileNameWallAdding = new SimpleStringProperty();
    StringProperty imageFileNameGoal = new SimpleStringProperty();

    private double cellWidth,cellHeight;
    private Image goalImage, solutionImage,wallImage,playerImage, seaImage, wallAddingImage;

    /**
     * @return wall image
     */
    public String getImageFileNameWall() {
        return imageFileNameWall.get();
    }

    /**
     * @return player image
     */
    public String getImageFileNamePlayer() {
        return imageFileNamePlayer.get();
    }

    /**
     * @return goal image
     */
    public String getImageFileNameGoal() {
        return imageFileNameGoal.get();
    }

    /**
     * @return decorate wall image
     */
    public String getImageFileNameWallAdding() {
        return imageFileNameWallAdding.get();
    }

    /**
     * @return possible player location image
     */
    public String getImageFileNameSea() {
        return imageFileNameSea.get();
    }

    /**
     * @return rum bottler that show the user way from start pos to goal pos
     */
    public String getImageFileNameSolution() {
        return imageFileNameSolution.get();
    }

    /**
     * @param imageFileNameGoal
     */
    public void setImageFileNameGoal(String imageFileNameGoal) {
        this.imageFileNameGoal.set(imageFileNameGoal);
    }

    /**
     * @param imageFileNameWall
     */
    public void setImageFileNameWall(String imageFileNameWall) {
        this.imageFileNameWall.set(imageFileNameWall);
    }

    /**
     * @param imageFileNamePlayer
     */
    public void setImageFileNamePlayer(String imageFileNamePlayer) {
        this.imageFileNamePlayer.set(imageFileNamePlayer);
    }

    /**
     * @param imageFileNameSea
     */
    public void setImageFileNameSea(String imageFileNameSea) {
        this.imageFileNameSea.set(imageFileNameSea);
    }

    /**
     * @param imageFileNameSolution
     */
    public void setImageFileNameSolution(String imageFileNameSolution) {
        this.imageFileNameSolution.set(imageFileNameSolution);
    }

    /**
     * @param imageFileNameWallAdding
     */
    public void setImageFileNameWallAdding(String imageFileNameWallAdding) {
        this.imageFileNameWallAdding.set(imageFileNameWallAdding);
    }

    /**
     * @return player row
     */
    public int getPlayerRow() {
        return playerRow;
    }

    /**
     * @return player col
     */
    public int getPlayerCol() {
        return playerCol;
    }

    /**
     * loads the picture from resources file
     */
    public void loadPictures() {
        try{
            goalImage = new Image(new FileInputStream(getImageFileNameGoal()));
            seaImage = new Image(new FileInputStream(getImageFileNameSea()));
            wallImage = new Image(new FileInputStream(getImageFileNameWall()));
            playerImage = new Image(new FileInputStream(getImageFileNamePlayer()));
            wallAddingImage = new Image(new FileInputStream(getImageFileNameWallAdding()));
            solutionImage = new Image(new FileInputStream(getImageFileNameSolution()));
        } catch (FileNotFoundException e) {
        }
    }

    /**
     * set the player new position and draw the movement
     * @param row
     * @param col
     */
    public void setPlayerPosition(int row, int col) {
        drawOldPlayer(getGraphicsContext2D());
        this.playerRow = row;
        this.playerCol = col;
        drawOldPlayer(getGraphicsContext2D());
        drawPlayer(getGraphicsContext2D());
    }

    /**
     * draws the maze
     * @param maze model maze
     */
    public void drawMaze(Maze maze) {
        this.maze = maze;
        this.solution=null;
        this.playerRow = maze.getStartPosition().getRowIndex();
        this.playerCol = maze.getStartPosition().getColumnIndex();
        //walls decorator
        Random rnd  = new Random();
        this.randomAddings = new boolean[maze.getRows()][maze.getCols()];
        for (int i =0;i<maze.getRows();i++)
            for (int j = 0 ; j<maze.getCols();j++)
                randomAddings[i][j]=(rnd.nextInt(9)==0);

        draw();
    }

    /**
     * draw maze
     */
    public void draw() {
        if(maze != null){
            loadPictures();
            double canvasHeight = getHeight();
            double canvasWidth = getWidth();
            int rows = maze.getRows();
            int cols = maze.getCols();
            //calculate cell sizes
            cellHeight = canvasHeight / rows;
            cellWidth = canvasWidth / cols;

            GraphicsContext graphicsContext = getGraphicsContext2D();
            //clear the canvas:
            graphicsContext.clearRect(0, 0, canvasWidth, canvasHeight);

            drawMazeWalls(graphicsContext, rows, cols);
            if (solution != null)
                drawMazeSolution(graphicsContext);

            drawMazeGoal(graphicsContext);
            drawPlayer(graphicsContext);
        }
    }

    /**
     * draws maze goal
     * @param graphicsContext of the MazeDisplayer
     */
    private void drawMazeGoal(GraphicsContext graphicsContext) {
        double x = maze.getGoalPosition().getColumnIndex() * cellWidth +cellWidth/6;
        double y = maze.getGoalPosition().getRowIndex() * cellHeight + cellHeight/6;
        graphicsContext.setFill(Color.GREEN);
        if (goalImage == null) {
            graphicsContext.setFill(Color.GREEN);
            graphicsContext.fillRect(x, y, cellWidth, cellHeight);
        }
        else
            graphicsContext.drawImage(goalImage, x, y, cellWidth/3*2, cellHeight/3*2);
    }

    /**
     * draws maze solution on the sea
     * @param graphicsContext of the MazeDisplayer
     */
    private void drawMazeSolution(GraphicsContext graphicsContext) {
        for (int i = 0; i <solution.getSolutionPath().size()-1;i++){
            AState s = solution.getSolutionPath().get(i);
            if (!( ((MazeState)s).getPosition().getColumnIndex()==playerCol && ((MazeState)s).getPosition().getRowIndex()==playerRow)) {
                double x = ((MazeState) s).getPosition().getColumnIndex() * cellWidth;
                double y = ((MazeState) s).getPosition().getRowIndex() * cellHeight;
                if (solutionImage!=null)
                    graphicsContext.drawImage(solutionImage, x, y, cellWidth, cellHeight);
                else {
                    graphicsContext.setFill(Color.RED);
                    graphicsContext.fillRect(x, y, cellWidth, cellHeight);
                }
            }
        }
    }

    /**
     * draws maze walls
     * @param graphicsContext of the MazeDisplayer
     * @param rows number of maze rows
     * @param cols number of maze columns
     */
    private void drawMazeWalls(GraphicsContext graphicsContext, int rows, int cols) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {

                    double x = j * cellWidth;
                    double y = i * cellHeight;
                if(maze.getPositionValue(i,j) == 1) {
                    if (wallImage == null) {
                        graphicsContext.setFill(Color.BROWN);
                        graphicsContext.fillRect(x, y, cellWidth, cellHeight);
                    } else {
                        graphicsContext.drawImage(wallImage, x, y, cellWidth, cellHeight);
                        if (wallAddingImage != null && randomAddings[i][j])
                            graphicsContext.drawImage(wallAddingImage, x, y, cellWidth, cellHeight);
                    }
                }
                else{
                    if (seaImage == null) {
                        graphicsContext.setFill(Color.BLUE);
                        graphicsContext.fillRect(x, y, cellWidth, cellHeight);
                    } else
                        graphicsContext.drawImage(seaImage,x,y,cellWidth,cellHeight);
                }
            }
        }
    }

    /**
     * deletes the player from it previous location
     * @param graphicsContext of the MazeDisplayer
     */
    private void drawOldPlayer(GraphicsContext graphicsContext) {
        double x = getPlayerCol() * cellWidth;
        double y = getPlayerRow() * cellHeight;
        if(seaImage == null) {
            graphicsContext.setFill(Color.BLUE);
            graphicsContext.fillRect(x, y, cellWidth, cellHeight);
        }
        else
            graphicsContext.drawImage(seaImage, x, y, cellWidth, cellHeight);
    }

    /**
     * draw player on it new location
     * @param graphicsContext
     */
    private void drawPlayer(GraphicsContext graphicsContext) {
        double x = getPlayerCol() * cellWidth;
        double y = getPlayerRow() * cellHeight;

        if(playerImage == null) {
            graphicsContext.setFill(Color.WHITE);
            graphicsContext.fillRect(x, y, cellWidth, cellHeight);
        }
        else
            graphicsContext.drawImage(playerImage, x, y, cellWidth, cellHeight);
    }

    /**
     * set solution value
     * @param solution maze solution from starting goal
     */
    public void setSolution(Solution solution) {
        this.solution = solution;
        drawMazeSolution(getGraphicsContext2D());
    }
}
