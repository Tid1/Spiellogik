package Model.Spiellogik;

import Model.Exceptions.GameException;
import Model.Exceptions.StatusException;
import Model.Spiellogik.*;
import Model.Spiellogik.Figuren.*;
import Model.Spiellogik.MoveSets.MoveSetAssist;
import javafx.geometry.Pos;

import java.util.*;

public class BoardImpl implements iBoard {
    private Map<iPlayer, List<iPiece>> map = new HashMap<>();
    private Status status = Status.START;
    private final int BOUNDS = 8;
    private final int UPPERBOUNDS = 8;
    private final int LOWERBOUNDS = 1;
    private final int PLAYER_LIMIT = 2;
    private final int FIRST_PLAYER = 0;
    private int checkCount = 0;
    private List<Position> posiblePositions = new LinkedList<>();
    private iPiece checkingPiece;

    @Override
    public void initializeField() throws StatusException {
        if (this.status != Status.INIT_FIELD){
            throw new StatusException("Can only be called while game is starting!");
        }

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
                whitePieces.add(new Laeufer(Color.White, new Position(i, 1)));
                blackPieces.add(new Laeufer(Color.Black, new Position(i, 8)));
            } else if (i == 3 || i == 6) {
                whitePieces.add(new Springer(Color.White, new Position(i, 1)));
                blackPieces.add(new Springer(Color.Black, new Position(i, 8)));
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
        if (checkValidMove(piece, x, y)) {
            iPiece pieceOnField = onField(x, y);
            if (pieceOnField != null) {
                if (pieceOnField.getColor() == piece.getColor()) {
                    throw new GameException("Pieces are the of the same color");
                }
                pieceOnField.setCaputred(true);
            }
            if (piece.getType() == Typ.KOENIG) {
                if (piece.getPosition().getX()-x == 2) {
                    onField(1, piece.getPosition().getY()).setPosition(4, piece.getPosition().getY());
                }
                if (piece.getPosition().getX()-x == -2) {
                    onField(8, piece.getPosition().getY()).setPosition(6, piece.getPosition().getY());
                }
            }
            piece.setPosition(x, y);
            changeTurns();
        }
        throw new GameException("Illegal Move");
    }

    @Override
    public boolean checkForCheck(iPlayer player) {
        List<iPiece> pieceList = map.get(player);
        for (iPiece piece : pieceList) {
            if (piece.getType() == Typ.KOENIG) {
                if (MoveSetAssist.countCheck(this, player.getColor(), piece.getPosition())>0) {
                    return true;
                }
                break;
            }
        }
        return false;
    }

    @Override
    public boolean checkMate(iPlayer player) {
        List<iPiece> pieceList = map.get(player);
        if (checkForCheck(player)) {
            for (iPiece piece : pieceList) {
                if (piece.getMoveset().moveSet(this) != null) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean checkStalemate(iPlayer player) {
        List<iPiece> pieceList = map.get(player);
        if (!checkForCheck(player)) {
            for (iPiece piece : pieceList) {
                if (piece.getMoveset().moveSet(this) != null) {
                    return false;
                }
            }
        }
        return true;
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
                        }
                    }
                }
            }
        } else {
            this.status = Status.TURN_WHITE;
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

    public List<Position> getPosiblePositions() {
        return posiblePositions;
    }

    public void setCheckingPiece(iPiece piece) {
        this.checkingPiece = piece;
    }
    public iPiece getCheckingPiece() {
        return checkingPiece;
    }
}
