package Model;

import Client.IClientStrategy;
import IO.MyDecompressorInputStream;
import algorithms.mazeGenerators.Maze;

import java.io.*;

/**
 * This class represents the client strategy which communicates with the maze generating server
 */
public class ClientStrategyGenerateMaze implements IClientStrategy {

    private int rows;
    private int cols;
    private Maze maze;

    /**
     * constructs a strategy object, with the wanted sizes of the maze
     * @param rows the number of rows in the maze
     * @param cols the number of cols in the maze
     */
    public ClientStrategyGenerateMaze(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
    }

    /**
     * Recieves the server's in and out streams, sends it the sizes of the maze and recieves the generated maze
     * @param inFromServer The input stream from the server
     * @param outToServer The output stream to the server
     */
    @Override
    public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
        try {
            ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
            //write to the server the wanted size
            int[] rowCol ={rows,cols};
            toServer.writeObject(rowCol);
            toServer.flush();

            ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
            byte[] compressedMaze = (byte[]) fromServer.readObject(); //read generated maze (compressed with
            //MyCompressor) from server - decompress it

            ByteArrayInputStream byteIn = new ByteArrayInputStream(compressedMaze);
            InputStream is = new MyDecompressorInputStream(byteIn);
            byte[] decompressedMaze = new byte[1500000];
            is.read(decompressedMaze); //Fill decompressedMaze
            maze = new Maze (decompressedMaze);
            is.close();
            fromServer.close();
            toServer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the maze generated by the server
     * @return the maze generated by the server
     */
    public Maze getMaze(){
        return maze;
    }


}