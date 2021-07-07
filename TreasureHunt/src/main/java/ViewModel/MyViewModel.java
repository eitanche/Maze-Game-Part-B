package ViewModel;
import Model.IModel;
import Model.MovementDirection;
import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;
import javafx.scene.input.KeyEvent;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.UnknownHostException;
import java.util.Observable;
import java.util.Observer;

/**
 * this class implements View Model from mvvm structure
 */
public class MyViewModel extends Observable implements Observer {
    private IModel model;

    /**
     * constructor
     * @param model model of the ViewModel
     */
    public MyViewModel(IModel model) {
        this.model = model;
        this.model.assignObserver(this); // i am one of the observer of the model
    }

    /**
     * update observers
     * @param observable
     * @param o observers
     */
    @Override
    public void update(Observable observable, Object o) {
        setChanged();
        notifyObservers(o);
    }

    /**
     * saves the maze into *.maze file
     * @param filetosave name of file
     * @throws IOException
     */
    public void saveMaze(File filetosave) throws IOException {
        model.saveMaze(filetosave);
    }

    /**
     * generate Maze function
     * @param rows number of maze rows
     * @param cols number of maze columns
     * @throws IllegalArgumentException if the rows and cols is smaller than 1
     */
    public void generateMaze(int rows,int cols) throws IllegalArgumentException { ////!!!UnknownHostException {
        if (rows <1 || cols<1){
            throw new IllegalArgumentException();
        }
        model.generateMaze(rows, cols);
    }


    /**
     * asks from the model the maze
     * @return model Maze
     */
    public Maze getMaze(){
        return model.getMaze();
    }

    /**
     * solves the maze
     */
    public void solveMaze() {
        model.solveMaze();
    }


    /**
     * @return Solution of the maze
     */
    public Solution getSolution(){
        return model.getSolution();
    }

    /**
     * moves the player
     * @param keyEvent the key that the user pressed
     */
    public void movePlayer(KeyEvent keyEvent){
        MovementDirection direction;
        switch (keyEvent.getCode()){
            case NUMPAD1:
                direction = MovementDirection.DownLeft;
                break;
            case NUMPAD2:
            case DOWN:
                direction = MovementDirection.Down;
                break;
            case NUMPAD3:
                direction = MovementDirection.DownRight;
                break;
            case NUMPAD4:
            case LEFT:
                direction = MovementDirection.Left;
                break;
            case NUMPAD6:
            case RIGHT:
                direction = MovementDirection.Right;
                break;
            case NUMPAD7:
                direction = MovementDirection.UPLeft;
                break;
            case NUMPAD8:
            case UP:
                direction = MovementDirection.Up;
                break;
            case NUMPAD9:
                direction = MovementDirection.UPRight;
                break;
            default:
                return;

        }
        if (model.isSolved())
            throw new IllegalStateException();
        model.updatePlayerLocation(direction);
    }

    /**
     * @return player row in the model
     */
    public int getPlayerRow(){
        return model.getPlayerRow();
    }

    /**
     * @return player column in the model
     */
    public int getPlayerCol(){
        return model.getPlayerCol();
    }

    /**
     * load the maze
     * @param chosen chosen file
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws IllegalArgumentException
     */
    public void loadMaze(File chosen) throws IOException, ClassNotFoundException, IllegalArgumentException {

        ObjectInputStream reader = new ObjectInputStream(new FileInputStream(chosen));

        Object obj = reader.readObject();
        if (!(obj instanceof Maze))
            throw new IllegalArgumentException();
        else
            model.loadMaze((Maze)obj);

    }

    /**
     * set player location in the model
     * @param i i in maze
     * @param j j in maze
     */
    public void setPlayerLoc(int i, int j) {
        if (model.isSolved())
            throw new IllegalStateException();
        int y = model.getPlayerRow();
        int x = model.getPlayerCol();
        Maze maze = model.getMaze();

        int disX=Math.abs(x-j);
        int disY=Math.abs(y-i);

        try {
            if (disX + disY == 1)
                model.updateLocation(i, j);
            else if (disX == 1 && disY == 1 && (maze.getPositionValue(y, j) == 0 || maze.getPositionValue(i, x) == 0))
                model.updateLocation(i, j);
        }
        catch (IndexOutOfBoundsException e) {}


    }

    /**
     * stops the model Servers
     */
    public void stop() {
        model.stop();
    }
}
