package Model.Spiellogik.MoveSets;

import Model.Spiellogik.BoardImpl;
import Model.Spiellogik.Color;
import Model.Spiellogik.Figuren.*;
import Model.Spiellogik.iBoard;

import java.util.LinkedList;
import java.util.List;

public class KoenigMoveset implements iMoveSet{
    private Color pieceColor;
    private Position currentPosition;

    public KoenigMoveset(Color pieceColor, Position currentPosition) {
        this.pieceColor = pieceColor;
        this.currentPosition = currentPosition;
    }

    @Override
    public List<Position> moveSet(iBoard board) {
        return move((BoardImpl) board);
    }

    private List<Position> move(BoardImpl board){
        List<Position> validMoves = new LinkedList<Position>();
        iPiece currentPiece = board.onField(currentPosition.getX(), currentPosition.getY());
        Position tempPosition;
        if (currentPosition.getX()+1 <= board.getUPPERBOUNDS()) {
            if (currentPosition.getY()+1 <= board.getUPPERBOUNDS()) {
                tempPosition = new Position(currentPosition.getX()+1, currentPosition.getY()+1);
                addVaiableField(board, pieceColor, validMoves, tempPosition);
            }
            tempPosition = new Position(currentPosition.getX()+1, currentPosition.getY());
            addVaiableField(board, pieceColor, validMoves, tempPosition);
            if (currentPosition.getY()-1 >= board.getLOWERBOUNDS()) {
                tempPosition = new Position(currentPosition.getX()+1, currentPosition.getY()-1);
                addVaiableField(board, pieceColor, validMoves, tempPosition);
            }
        }
        if (currentPosition.getX()-1 >= board.getLOWERBOUNDS()) {
            if (currentPosition.getY()+1 <= board.getUPPERBOUNDS()) {
                tempPosition = new Position(currentPosition.getX()-1, currentPosition.getY()+1);
                addVaiableField(board, pieceColor, validMoves, tempPosition);
            }
            tempPosition = new Position(currentPosition.getX()-1, currentPosition.getY());
            addVaiableField(board, pieceColor, validMoves, tempPosition);
            if (currentPosition.getY()-1 >= board.getLOWERBOUNDS()) {
                tempPosition = new Position(currentPosition.getX()-1, currentPosition.getY()-1);
                addVaiableField(board, pieceColor, validMoves, tempPosition);
            }
        }
        if (currentPosition.getY()+1 <= board.getUPPERBOUNDS()) {
            tempPosition = new Position(currentPosition.getX(), currentPosition.getY()+1);
            addVaiableField(board, pieceColor, validMoves, tempPosition);
        }
        if (currentPosition.getY()-1 >= board.getLOWERBOUNDS()) {
            tempPosition = new Position(currentPosition.getX(), currentPosition.getY()-1);
            addVaiableField(board, pieceColor, validMoves, tempPosition);
        }

        if (currentPiece.getType() == Typ.KOENIG) {
            Koenig kingPiece = (Koenig) currentPiece;
            if (!kingPiece.hasMoved()) {
                iPiece tempPiece = board.onField(1, currentPosition.getY());
                if (tempPiece != null && tempPiece.getType() == Typ.TURM) {
                    Turm rookPiece = (Turm) tempPiece;
                    if (!rookPiece.hasMoved()) {
                        boolean rochade = true;
                        for (int i=1; i<=3; i++) {
                            if (board.onField(currentPosition.getX()-i, currentPosition.getY()) != null
                                    || MoveSetAssist.countCheck(board, pieceColor, new Position(currentPosition.getX()-i, currentPosition.getY()))>0) {
                                rochade = false;
                            }
                        }
                        if (rochade) {
                            validMoves.add(new Position(currentPosition.getX()-2, currentPosition.getY()));
                        }
                    }
                }
                tempPiece = board.onField(8, currentPosition.getY());
                if (tempPiece != null && tempPiece.getType() == Typ.TURM) {
                    Turm rookPiece = (Turm) tempPiece;
                    if (!rookPiece.hasMoved()) {
                        boolean rochade = true;
                        for (int i=1; i<=2; i++) {
                            if (board.onField(currentPosition.getX()+i, currentPosition.getY()) != null
                                    || MoveSetAssist.countCheck(board, pieceColor, new Position(currentPosition.getX()+i, currentPosition.getY()))>0) {
                                rochade = false;
                            }
                        }
                        if (rochade) {
                            validMoves.add(new Position(currentPosition.getX()+2, currentPosition.getY()));
                        }
                    }
                }

            }
        }

        return validMoves;
    }
    private void addVaiableField(BoardImpl board, Color pieceColor, List<Position> validMoves, Position tempPosition){
        if (MoveSetAssist.countCheck(board, pieceColor, tempPosition)<=0) {
            if (board.onField(tempPosition.getX(), tempPosition.getY()) == null
                    || board.onField(tempPosition.getX(), tempPosition.getY()).getColor() != pieceColor) {
                validMoves.add(tempPosition);
            }
        }
    }
}
