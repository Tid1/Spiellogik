package Model.Spiellogik;

import Model.Exceptions.GameException;
import Model.Exceptions.StatusException;
import Model.Spiellogik.*;
import Model.Spiellogik.Figuren.*;
import Model.Spiellogik.MoveSets.MoveSetAssist;
import javafx.geometry.Pos;
import sun.misc.Launcher;
import sun.security.provider.ConfigFile;

import java.lang.reflect.MalformedParameterizedTypeException;
import java.util.*;

public class BoardImpl implements iBoard {
    static final long serialVersionUID = 30L;
    private Map<iPlayer, List<iPiece>> map = new HashMap<>();
    private boolean gameEnded = false;
    private Status status = Status.START;
    private final int BOUNDS = 8;
    private final int UPPERBOUNDS = 8;
    private final int LOWERBOUNDS = 1;
    private final int PLAYER_LIMIT = 2;
    private final int FIRST_PLAYER = 0;
    private int checkCount = 0;
    private List<Position> posiblePositions = new LinkedList<>();
    private iPiece checkingPiece;
    private iPiece enPassantPieceW, enPassantPieceB;

    @Override
    public void initializeField() throws StatusException {
        if (this.status != Status.INIT_FIELD){
            throw new StatusException("Can only be called while game is starting!");
        }

        gameEnded = false;
        status = Status.TURN_WHITE;

        List<iPlayer> playerList = new ArrayList<>(map.keySet());
        if (playerList.get(0).getColor() == Color.White) {
            initializeFieldHelper(playerList.get(0), playerList.get(1));
        } else {
            initializeFieldHelper(playerList.get(1), playerList.get(0));
        }
    }

    private void initializeFieldHelper(iPlayer white, iPlayer black) {
        List<iPiece> whitePieces = map.get(white);
        List<iPiece> blackPieces = map.get(black);
        for (int i=1; i<=8; i++) {
            whitePieces.add(new Bauer(Color.White, new Position(i, 2)));
            blackPieces.add(new Bauer(Color.Black, new Position(i, 7)));

            if (i == 1 || i == 8) {
                whitePieces.add(new Turm(Color.White, new Position(i, 1)));
                blackPieces.add(new Turm(Color.Black, new Position(i, 8)));
            } else if (i == 2 || i == 7) {
                whitePieces.add(new Springer(Color.White, new Position(i, 1)));
                blackPieces.add(new Springer(Color.Black, new Position(i, 8)));
            } else if (i == 3 || i == 6) {
                whitePieces.add(new Laeufer(Color.White, new Position(i, 1)));
                blackPieces.add(new Laeufer(Color.Black, new Position(i, 8)));
            } else if (i == 4) {
                whitePieces.add(new Dame(Color.White, new Position(i, 1)));
                blackPieces.add(new Dame(Color.Black, new Position(i, 8)));
            } else if (i == 5) {
                whitePieces.add(new Koenig(Color.White, new Position(i, 1)));
                blackPieces.add(new Koenig(Color.Black, new Position(i, 8)));
            }
        }
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

        if (this.status == Status.END || this.status == Status.STALEMATE){
            throw new StatusException("Game already ended!");
        }
        if (checkValidMove(piece, x, y)) {
            iPiece pieceOnField = onField(x, y);
            boolean deleteEnPassant = false;
            if (pieceOnField != null) {
                if (pieceOnField.getColor() == piece.getColor()) {
                    throw new GameException("Pieces are the of the same color");
                }
                pieceOnField.setCaputred(true);
            }
            if (piece.getType() == Typ.KOENIG) { //Rochade
                if (piece.getPosition().getX()-x == 2) {
                    onField(1, piece.getPosition().getY()).setPosition(4, piece.getPosition().getY());
                }
                if (piece.getPosition().getX()-x == -2) {
                    onField(8, piece.getPosition().getY()).setPosition(6, piece.getPosition().getY());
                }
            }

            if (piece.getType() == Typ.BAUER){ //Transformation zu Dame
                List<iPiece> pieces;
                if (piece.getColor() == Color.Black && y == LOWERBOUNDS){
                    for (Map.Entry<iPlayer, List<iPiece>> entry : map.entrySet()){
                        if (entry.getKey().getColor() == Color.Black){
                            pieces = entry.getValue();
                            pieces.remove(piece);
                            pieces.add(new Dame(Color.Black, new Position(x, y)));
                        }
                    }
                } else if (y == UPPERBOUNDS){
                    for (Map.Entry<iPlayer, List<iPiece>> entry : map.entrySet()){
                        if (entry.getKey().getColor() == Color.White){
                            pieces = entry.getValue();
                            pieces.remove(piece);
                            pieces.add(new Dame(Color.White, new Position(x, y)));
                        }
                    }
                    piece = new Dame(Color.White, new Position(x, y));
                }

                //set en passant
                if (piece.getColor() == Color.White && piece.getPosition().getY()-y==-2) {
                    setEnPassantPieceW(piece);
                } else if (piece.getColor() == Color.Black && piece.getPosition().getY()-y==2) {
                    setEnPassantPieceB(piece);
                }

                //delete en passant
                if (piece.getColor() == Color.White) {
                    iPiece enPassant = getEnPassantPieceB();
                    if (enPassant != null) {
                        if (x == enPassant.getPosition().getX() && y-1 == enPassant.getPosition().getY()) {
                            deleteEnPassant = true;
                        }
                    }
                } else {
                    iPiece enPassant = getEnPassantPieceW();
                    if (enPassant != null) {
                        if (x == enPassant.getPosition().getX() && y+1 == enPassant.getPosition().getY()) {
                            deleteEnPassant = true;
                        }
                    }
                }
            }

            if (onField(x, y) != null || deleteEnPassant){
                for (Map.Entry<iPlayer, List<iPiece>> entry : map.entrySet()){
                    if (entry.getKey().getColor() != piece.getColor()){
                        List<iPiece> deletePiece = entry.getValue();
                        if (deleteEnPassant) {
                            if (getStatus() == Status.TURN_WHITE) {
                                deletePiece.remove(getEnPassantPieceB());
                            } else {
                                deletePiece.remove(getEnPassantPieceW());
                            }
                        }
                        for (iPiece pieceToDelete : deletePiece){
                            if (pieceToDelete == onField(x, y)){
                                deletePiece.remove(pieceToDelete);
                                break;
                            }
                        }
                    }
                }
            }
            piece.setPosition(x, y);
            changeTurns();
            return;
        }
        throw new GameException("Illegal Move");
    }

    @Override
    public boolean checkForCheck(iPlayer player) {
        List<iPiece> pieceList = map.get(player);
        for (iPiece piece : pieceList) {
            if (piece.getType() == Typ.KOENIG) {
                if (MoveSetAssist.countCheck(this, player.getColor(), piece.getPosition())>0) {
                    player.setChecked(true);
                    return true;
                }
                break;
            }
        }
        player.setChecked(false);
        return false;
    }

    @Override
    public boolean checkMate(iPlayer player) {
        List<iPiece> pieceList = map.get(player);
        if (checkForCheck(player)) {
            for (iPiece piece : pieceList) {
                if (piece.getMoveset().moveSet(this).size() != 0) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean checkStalemate(iPlayer player) {
        List<iPiece> pieceList = map.get(player);
        if (!checkForCheck(player)) {
            for (iPiece piece : pieceList) {
                if (piece.getMoveset().moveSet(this).size() != 0) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean checkValidMove(iPiece piece, int x, int y) throws GameException {
        if (x > UPPERBOUNDS || y > UPPERBOUNDS || x < LOWERBOUNDS || y < LOWERBOUNDS){
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
        if (this.status == Status.END || this.status == Status.STALEMATE){
            throw new StatusException("Game ended");
        }
        if (this.status != Status.TURN_BLACK && this.status != Status.TURN_WHITE){
            throw new StatusException("Cant Swap turns");
        }
        if (this.status == Status.TURN_WHITE){
            this.status = Status.TURN_BLACK;
            setEnPassantPieceB(null);
            for (Map.Entry<iPlayer, List<iPiece>> entry : map.entrySet()){
                if (entry.getKey().getColor() == Color.Black) {
                    List<iPiece> list = entry.getValue();
                    for (iPiece piece : list){
                        if (piece.getType() == Typ.KOENIG) {
                            this.checkCount = MoveSetAssist.countCheck(this, Color.Black, piece.getPosition());
                            if (this.checkCount == 1) {
                                // TODO: Liste erstellen PosiblePositions
                                this.posiblePositions = MoveSetAssist.getPosiblePositions(this, piece.getPosition());
                            }
                            break;
                        }
                    }
                }
            }
        } else {
            this.status = Status.TURN_WHITE;
            setEnPassantPieceW(null);
            for (Map.Entry<iPlayer, List<iPiece>> entry : map.entrySet()){
                if (entry.getKey().getColor() == Color.White) {
                    List<iPiece> list = entry.getValue();
                    for (iPiece piece : list){
                        if (piece.getType() == Typ.KOENIG) {
                            this.checkCount = MoveSetAssist.countCheck(this, Color.White, piece.getPosition());
                            if (this.checkCount == 1) {
                                // TODO: Liste erstellen PosiblePositions
                                this.posiblePositions = MoveSetAssist.getPosiblePositions(this, piece.getPosition());
                            }
                        }
                    }
                }
            }
        }
        iPlayer currentTurn = null;
        List<iPlayer> players = new ArrayList<>(map.keySet());

        for (iPlayer player : players){
            if (status == Status.TURN_BLACK && player.getColor() == Color.Black){
                currentTurn = player;
            } else if (status == Status.TURN_WHITE && player.getColor() == Color.White){
                currentTurn = player;
            }
        }

        MoveSetAssist.setPinned(this);

        if (checkMate(currentTurn)){
            status = Status.END;
        }

        else if (checkStalemate(currentTurn)){
            status = Status.STALEMATE;
        }
        triggerGameEnd();
    }

    public void triggerGameEnd(){
        if (this.status == Status.STALEMATE || this.status == Status.END){
            gameEnded = true;
        }
    }

    public Status getStatus(){
        return status;
    }
    public boolean getGameEnd(){
        return gameEnded;
    }

    public void surrender(){
        this.gameEnded = true;
    }

    public int getUPPERBOUNDS() {
        return UPPERBOUNDS;
    }
    public int getLOWERBOUNDS() {
        return LOWERBOUNDS;
    }

    public int getCheckCount() {
        return checkCount;
    }

    public iPiece getEnPassantPieceW() {
        return enPassantPieceW;
    }
    public void setEnPassantPieceW(iPiece piece) {
        enPassantPieceW = piece;
    }
    public iPiece getEnPassantPieceB() {
        return enPassantPieceB;
    }
    public void setEnPassantPieceB(iPiece piece) {
        enPassantPieceB = piece;
    }

    public List<Position> getPosiblePositions() {
        return posiblePositions;
    }

    public void setCheckingPiece(iPiece piece) {
        this.checkingPiece = piece;
    }
    public iPiece getCheckingPiece() {
        return checkingPiece;
    }

    public Map<iPlayer, List<iPiece>> getMap(){
        return map;
    }
}