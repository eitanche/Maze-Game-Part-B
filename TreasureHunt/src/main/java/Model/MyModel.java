package Model;

import Client.Client;
import Server.*;
import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.channels.ClosedByInterruptException;
import java.util.Observable;
import java.util.Observer;

/**
 * Represents the model in the MVVM architecture
 */
public class MyModel extends Observable implements IModel{
    private Maze maze;
    private Solution solution;
    private int playerRow;
    private int playerCol;
    private Server generator;
    private Server solver;
    private boolean isSolved;

    /**
     * Constructs a new myModel objecct
     */
    public MyModel() {
        //creates the model's maze generating and solving servers
        generator = new Server(5400,1000,new ServerStrategyGenerateMaze());
        generator.start();
        solver = new Server(5401,1000,new ServerStrategySolveSearchProblem());
        solver.start();
    }

    /**
     * Generates maze with the given size
     * @param rows the number of rows in the maze
     * @param cols the number of cols in the maze
     */
    @Override
    public void generateMaze(int rows, int cols)  {
        //Create a new client to request from the server a new maze
        ClientStrategyGenerateMaze clientStrategyGenerateMaze = new ClientStrategyGenerateMaze(rows,cols);
        Client client = null;
        try {
            client = new Client(InetAddress.getLocalHost(),5400,clientStrategyGenerateMaze);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        client.communicateWithServer();
        isSolved=false;
        this.solution=null;
        maze = clientStrategyGenerateMaze.getMaze();
        playerRow = maze.getStartPosition().getRowIndex();
        playerCol = maze.getStartPosition().getColumnIndex();
        //notify the observer that a new maze created
        setChanged();
        notifyObservers("maze generated");
    }

    /**
     * saves the current maze to the given file
     * @param filetosave The file that the maze will be saved to
     * @throws IOException In case that the file wasnt found
     */
    public void saveMaze(File filetosave) throws IOException {
        //write the maze objects to the file
        ObjectOutputStream ob = new ObjectOutputStream(new FileOutputStream(filetosave));
        ob.writeObject(maze);
        ob.flush();
        ob.close();
    }
    /**
     * Loads the maze to the model
     * @param maze The maze to load
     */
    public void loadMaze(Maze maze) {
        this.maze=maze;
        this.solution=null;
        isSolved = false;
        playerRow = maze.getStartPosition().getRowIndex(); // strat pos
        playerCol = maze.getStartPosition().getColumnIndex();
        //notify the observerss that the maze is changed
        setChanged();
        notifyObservers("maze generated");
    }

    /**
     * Stops the model run
     */
    public void stop() { ////////////////////////maybe need to change servers stop
        //stop the servers
        generator.stop();
        solver.stop();
    }
    /**
     * returns the models maze
     * @return The models maze
     */
    @Override
    public Maze getMaze() {
        return maze;
    }

    /**
     * Solves the models maze
     */
    @Override
    public void solveMaze()  {
        //create a client to communicate with the solving maze server
        ClientStrategySolveMaze solver = new ClientStrategySolveMaze(maze);
        Client client = null;
        try {
            client = new Client(InetAddress.getLocalHost(),5401,solver);
        } catch (UnknownHostException e) {

        }
        client.communicateWithServer();

        solution = solver.getSolution();
        //notify the observers that the maze was solved
        setChanged();
        notifyObservers("maze solved");
    }

    /**
     * returns the mazess solution
     * @return
     */
    @Override
    public Solution getSolution() {
        return solution;
    }

    /**
     * Returns the player row index
     * @return the player row index
     */
    @Override
    public int getPlayerRow() {
        return playerRow;
    }

    /**
     * Returns the player column index
     * @return the player column index
     */
    @Override
    public int getPlayerCol() {
        return playerCol;
    }

    /**
     * assigns an observer to the model
     * @param o The observer of the model
     */
    @Override
    public void assignObserver(Observer o) {
        this.addObserver(o);

    }

    /**
     * Update the player location in the maze according to the direction
     * @param direction The moving direction of the player
     */
    @Override
    public void updatePlayerLocation(MovementDirection direction) {

        switch (direction){
            case Up:
                updateLocation(playerRow-1,playerCol);
                break;
            case UPRight:
                updateLocationDiagonal(playerRow-1,playerCol+1);
                break;
            case Right:
                updateLocation(playerRow,playerCol+1);
                break;
            case DownRight:
                updateLocationDiagonal(playerRow+1,playerCol+1);
                break;
            case Down:
                updateLocation(playerRow+1,playerCol);
                break;
            case DownLeft:
                updateLocationDiagonal(playerRow+1,playerCol-1);
                break;
            case Left:
                updateLocation(playerRow,playerCol-1);
                break;
            case UPLeft:
                updateLocationDiagonal(playerRow-1,playerCol-1);
                break;
            default:
                break;
        }

    }

    /**
     * Updates the player location to the new indexes, only if possibles - diagonal movement
     * @param newplayerRow The new row index
     * @param newplayerCol The new column index
     */
    private void updateLocationDiagonal(int newplayerRow, int newplayerCol) {
        //Checks if the player can move diagonally - if so update its location
        if ((maze.PositionInMaze(newplayerRow,playerCol) && maze.getPositionValue(newplayerRow,playerCol)==0)||
                (maze.PositionInMaze(playerRow,newplayerCol) && maze.getPositionValue(playerRow,newplayerCol)==0))
            updateLocation(newplayerRow,newplayerCol);

    }
    /**
     * Updates the player location to the given indexes
     * @param newplayerRow The new row index of the player
     * @param newplayerCol The new column index of the player
     */
    public void updateLocation(int newplayerRow, int newplayerCol) {
        //checks if the new position is valid and movable
        if(!(maze.PositionInMaze(newplayerRow,newplayerCol) && maze.getPositionValue(newplayerRow,newplayerCol) == 0))
            return;
        playerRow = newplayerRow;
        playerCol = newplayerCol;
        //checks if the new position is the goal position - update the observer accordingly
        if (maze.getGoalPosition().getRowIndex() == playerRow && maze.getGoalPosition().getColumnIndex()==playerCol) {
            isSolved=true;
            setChanged();
            notifyObservers("goal reached");
        }
        else{
            setChanged();
            notifyObservers("player moved");
        }
    }

    /**
     * Returns true only if the maze was solved already by the player
     * @return True - if the maze was solved, otherwise - false
     */
    public boolean isSolved() {
        return isSolved;
    }
}
