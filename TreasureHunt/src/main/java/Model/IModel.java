package Model;


import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;

import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Observer;

/**
 * An interface which represents a model in the MVVM structure
 */
public interface IModel {

    /**
     * saves the current maze to the given file
     * @param filetosave The file that the maze will be saved to
     * @throws IOException In case that the file wasnt found
     */
    void saveMaze(File filetosave) throws IOException;

    /**
     * Loads the maze to the model
     * @param maze The maze to load
     */
    void loadMaze(Maze maze);

    /**
     * Generates maze with the given size
     * @param rows the number of rows in the maze
     * @param cols the number of cols in the maze
     */
    void generateMaze(int rows,int cols); // generate a maze

    /**
     * returns the models maze
     * @return The models maze
     */
    Maze getMaze(); //will return the maze that the model creates

    /**
     * Solves the models maze
     */
    void solveMaze(); //the model already have the maze as a data member

    /**
     * returns the mazess solution
     * @return
     */
    Solution getSolution();

    /**
     * Update the player location in the maze according to the direction
     * @param direction The moving direction of the player
     */
    void updatePlayerLocation(MovementDirection direction); //tells when im done with the movement (check if possible, check if finished maze)

    /**
     * Returns the player row index
     * @return the player row index
     */
    int getPlayerRow();

    /**
     * Returns the player column index
     * @return the player column index
     */
    int getPlayerCol();

    /**
     * assigns an observer to the model
     * @param o The observer of the model
     */
    void assignObserver(Observer o);

    /**
     * Updates the player location to the given indexes
     * @param newplayerRow The new row index of the player
     * @param newplayerCol The new column index of the player
     */
    void updateLocation(int newplayerRow, int newplayerCol);

    /**
     * Returns true only if the maze was solved already by the player
     * @return True - if the maze was solved, otherwise - false
     */
    boolean isSolved();

    /**
     * Stops the model run
     */
    void stop();
}
