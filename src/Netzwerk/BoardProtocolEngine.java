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

    private void deserialize() throws GameException, StatusException{
        DataInputStream dais = new DataInputStream(bais);
        try {
            int method = dais.readInt();
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
    public void initializeField(iPlayer player1, iPlayer player2) throws StatusException {
        //Kann leer bleiben, wird nicht gebraucht
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
            daos.writeInt(METHOD_MOVE);
            daos.writeInt(piece.getPosition().getX());
            daos.writeInt(piece.getPosition().getY());
            daos.writeInt(x);
            daos.writeInt(y);
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
    public boolean checkStalemate() {
        //Kann leer bleiben
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
        } catch (IOException io){
            throw new GameException("Serialization unsuccessful.");
        }
        baos.toByteArray();
        return true;
    }

    private void deserializePickColor(){
        DataInputStream dais = new DataInputStream(this.bais);
        try {
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

}
