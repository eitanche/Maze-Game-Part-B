package Model;

import Client.IClientStrategy;
import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

/**
 * A class which represents the client strategy which askes from the server a solution for the maze
 */
public class ClientStrategySolveMaze implements IClientStrategy {

    private Maze maze;
    private Solution solution;

    /**
     * Constructs a strategy object with a given maze
     * @param maze The maze the client wants to solve
     */
    public ClientStrategySolveMaze(Maze maze) {
        this.maze = maze;
    }

    /**
     * Recieves the servers input and output stream, sends it a maze and gets from it the solution
     * @param inFromServer The input stream from the server
     * @param outToServer The output stream to the server
     */
    @Override
    public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
        try {
            ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
            toServer.writeObject(maze);
            toServer.flush();
            ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
            solution = (Solution) fromServer.readObject();
            fromServer.close();
            toServer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the mazes solution
     * @return The mazes solution
     */
    public Solution getSolution(){
        return solution;
    }
}
