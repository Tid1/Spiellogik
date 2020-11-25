package Netzwerk;

import Model.Exceptions.GameException;
import Model.Exceptions.StatusException;
import Model.Spiellogik.Color;
import Model.Spiellogik.Figuren.Position;
import Model.Spiellogik.Figuren.iPiece;
import Model.Spiellogik.iBoard;
import Model.Spiellogik.iPlayer;

import java.io.ByteArrayOutputStream;

public class BoardSender implements iBoard {
    private iBoard board;
    private ByteArrayOutputStream baos;

    private final int BAUER  = 1;
    private  final int DAME = 2;
    private final int KOENIG  = 3;
    private final int LAUEFER = 4;
    private final int SPRINGER = 5;
    private final int TURM = 6;



    public BoardSender(iBoard board, ByteArrayOutputStream baos){
        this.board = board;
        this.baos = baos;
    }

    @Override
    public void initializeField(iPlayer player1, iPlayer player2) throws StatusException {

    }

    @Override
    public iPiece onField(int x, int y) {
        return null;
    }

    @Override
    public void move(iPiece piece, int x, int y) throws GameException, StatusException {

    }

    @Override
    public boolean checkForCheck(iPlayer player) {
        return false;
    }

    @Override
    public boolean checkMate(iPlayer player) {
        return false;
    }

    @Override
    public boolean checkStalemate() {
        return false;
    }

    @Override
    public boolean checkValidMove(iPiece piece, int x, int y) throws GameException {
        return false;
    }

    @Override
    public boolean pickColor(String playerName, Color color) throws StatusException {
        return false;
    }

    @Override
    public void changeTurns() throws StatusException {

    }
}
