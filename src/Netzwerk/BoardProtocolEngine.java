package Netzwerk;

import Model.Exceptions.GameException;
import Model.Exceptions.StatusException;
import Model.Spiellogik.Color;
import Model.Spiellogik.Figuren.Position;
import Model.Spiellogik.Figuren.iPiece;
import Model.Spiellogik.PlayerImpl;
import Model.Spiellogik.iBoard;
import Model.Spiellogik.iPlayer;

import javax.xml.crypto.Data;
import java.io.*;

public class BoardProtocolEngine implements iBoard {
    private iBoard board;
    private ByteArrayOutputStream baos;
    private ByteArrayInputStream bais;

    private final int WHITE = 1;
    private final int BLACK = 2;

    private final int METHOD_MOVE = 1;
    private final int METHOD_PICK_COLOR = 2;


    public BoardProtocolEngine(iBoard board, ByteArrayOutputStream baos, ByteArrayInputStream bais){
        this.board = board;
        this.baos = baos;
        this.bais = bais;
    }

    public void deserialize() throws GameException, StatusException{
        DataInputStream dais = new DataInputStream(bais);
        try {
            int method = dais.readInt();
            System.out.println("Current Method: " + method);
            switch (method){
                case METHOD_MOVE:
                    this.deserializeMove();
                    break;
                case METHOD_PICK_COLOR:
                    this.deserializePickColor();
                    break;
                default:
                    throw new GameException("Couldn't find Method");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initializeField() throws StatusException {

    }

    @Override
    public iPiece onField(int x, int y) {
        //Kann leer bleiben
        return null;
    }

    @Override
    public void move(iPiece piece, int x, int y) throws GameException, StatusException {
        baos.reset();
        DataOutputStream daos = new DataOutputStream(baos);
        try {
            System.out.println("Serializing method move: ");
            daos.writeInt(METHOD_MOVE);
            System.out.println("Writing " + METHOD_MOVE + " into the stream");
            daos.writeInt(piece.getPosition().getX());
            System.out.println("Writing " + piece.getPosition().getX() + " into the stream");
            daos.writeInt(piece.getPosition().getY());
            System.out.println("Writing " + piece.getPosition().getY() + " into the stream");
            daos.writeInt(x);
            System.out.println("Writing "+ x + " into the stream");
            daos.writeInt(y);
            System.out.println("Writing " + y + " into the stream");
            board.move(piece, x, y);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deserializeMove() throws GameException, StatusException {
        DataInputStream dais = new DataInputStream(bais);
        try {
            int currentPosX = dais.readInt();
            int currentPosY = dais.readInt();
            iPiece piece = board.onField(currentPosX, currentPosY);

            int changedPosX = dais.readInt();
            int changedPosY = dais.readInt();

            board.move(piece, changedPosX, changedPosY);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean checkForCheck(iPlayer player) {
        //Kann leer bleiben
        return false;
    }

    @Override
    public boolean checkMate(iPlayer player) {
        //Kann leer bleiben
        return false;
    }

    @Override
    public boolean checkStalemate(iPlayer player) {
        return false;
    }


    @Override
    public boolean checkValidMove(iPiece piece, int x, int y) throws GameException {
        //Kann leer bleiben

        return false;
    }

    @Override
    public boolean pickColor(String playerName, Color color) throws StatusException, GameException {
        baos.reset();
        DataOutputStream daos = new DataOutputStream(baos);
        try {
            daos.write(METHOD_PICK_COLOR);
            daos.writeUTF(playerName);
            switch (color) {
                case Black:
                    daos.write(BLACK);
                    break;
                case White:
                    daos.write(WHITE);
                    break;
                default:
                    throw new GameException("Color unknown: " + color);
            }
            board.pickColor(playerName, color);
        } catch (IOException io){
            throw new GameException("Serialization unsuccessful.");
        }
        baos.toByteArray();
        return true;
    }

    private void deserializePickColor(){
        DataInputStream dais = new DataInputStream(this.bais);
        try {
            System.out.println("Deserialising pick color");
           String playerName = dais.readUTF();
           Color color = null;
           int wantedColor  = dais.readInt();

           switch (wantedColor){
               case BLACK:
                   color = Color.Black;
                   break;
               case WHITE:
                   color = Color.White;
                   break;
               default:
                   throw new GameException("Couldn't find Color: " + color);
           }

           board.pickColor(playerName, color);
        } catch (IOException | GameException | StatusException io){
            io.printStackTrace();
        }
    }

    public ByteArrayOutputStream getBaos(){
        return baos;
    }

    public void updateBais(byte[] arr) throws GameException, StatusException {
        this.bais = new ByteArrayInputStream(arr);
        deserialize();
    }
}
