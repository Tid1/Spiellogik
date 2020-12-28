package Model.Spiellogik;

import Model.Exceptions.GameException;
import Model.Exceptions.StatusException;
import Model.Spiellogik.*;
import Model.Spiellogik.Figuren.Position;
import Model.Spiellogik.Figuren.iPiece;
import javafx.geometry.Pos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BoardImpl implements iBoard {
    private Map<iPlayer, List<iPiece>> map = new HashMap<>();
    private Status status = Status.START;
    private final int BOUNDS = 8;
    private final int UPPERBOUNDS = 8;
    private final int LOWERBOUNDS = 1;
    private final int PLAYER_LIMIT = 2;
    private final int FIRST_PLAYER = 0;
    private int[][] field;

    @Override
    public void initializeField(iPlayer player1, iPlayer player2) throws StatusException {
        if (this.status != Status.INIT_FIELD){
            throw new StatusException("Can only be called while game is starting!");
        }

        field = new int[BOUNDS][BOUNDS];
        status = Status.TURN_WHITE;
        //TODO Spieler ihrer Feldseite zuteilen
    }

    @Override
    public iPiece onField(int x, int y) {
        for (Map.Entry<iPlayer, List<iPiece>> entry : map.entrySet()){
            List<iPiece> list = entry.getValue();
            for (iPiece piece : list){
                Position position = piece.getPosition();
                if (position.getX() == x && position.getY() == y){
                    return piece;
                }
            }
        }
        return null;
    }

    @Override
    public void move(iPiece piece, int x, int y) throws GameException, StatusException {
        if (this.status == Status.TURN_BLACK && piece.getColor() != Color.Black ||
            this.status == Status.TURN_WHITE && piece.getColor() != Color.White){
            throw new StatusException("Wrong players turn");
        }
        if (checkValidMove(piece, x, y)) {
            iPiece pieceOnField = onField(x, y);
            if (pieceOnField != null) {
                if (pieceOnField.getColor() == piece.getColor()) {
                    throw new GameException("Pieces are the of the same color");
                }
                pieceOnField.setCaputred(true);
            }
            changeTurns();
            piece.setPosition(x, y);
        }
        throw new GameException("Illegal Move");
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
        if (x >= BOUNDS || y >= BOUNDS || x < 0 || y < 0){
            throw new GameException("Out of Bounds!");
        }
        List<Position> movsets = piece.getMoveset().moveSet(this);
        for (Position move : movsets){
            if (move.getX() == x && move.getY() == y){
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean pickColor(String playerName, Color color) throws StatusException {
        if(this.status != Status.START && this.status != Status.ONE_PICKED){
            throw new StatusException("Wrong Status!");
        }

        if (map != null && map.size() >= PLAYER_LIMIT){
            throw new IllegalStateException("Already 2 present");
        }

        if (this.status == Status.START){
            map.put(new PlayerImpl(color, playerName), new ArrayList<>());//TODO Figurenliste erzeugen
            status = Status.ONE_PICKED;
            return true;
        }


        iPlayer playerInMap = (iPlayer) map.keySet().toArray()[FIRST_PLAYER];
        Color playerInMapColor = playerInMap.getColor();

        if (playerInMapColor == color){
            switch (color){
                case Black:
                    map.put(new PlayerImpl(Color.White, playerName), new ArrayList<>());
                    status = Status.INIT_FIELD;
                    return true;
                case White:
                    map.put(new PlayerImpl(Color.Black, playerName), new ArrayList<>());
                    status = Status.INIT_FIELD;
                    return true;
            }
            return false;
        } else {
            map.put(new PlayerImpl(color, playerName), new ArrayList<>());
            status = Status.INIT_FIELD;
            return true;
        }

    }

    private void changeTurns() throws StatusException {
        if (this.status != Status.TURN_BLACK && this.status != Status.TURN_WHITE){
            throw new StatusException("Cant Swap turns");
        }
        if (this.status == Status.TURN_WHITE){
            this.status = Status.TURN_BLACK;
        }
        this.status = Status.TURN_WHITE;
    }
    public int getUPPERBOUNDS() {
        return UPPERBOUNDS;
    }
    public int getLOWERBOUNDS() {
        return LOWERBOUNDS;
    }
}
