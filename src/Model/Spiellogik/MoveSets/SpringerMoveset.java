package Model.Spiellogik.MoveSets;

import Model.Spiellogik.*;
import Model.Spiellogik.Figuren.*;

import java.util.LinkedList;
import java.util.List;

public class SpringerMoveset implements iMoveSet{
    private Color pieceColor;
    private Position currentPosition;
    private final int NOTMAGICONE = 1;
    private final int NOTMAGICTWO = 2;

    public SpringerMoveset(Color pieceColor, Position currentPosition){
        this.pieceColor = pieceColor;
        this.currentPosition = currentPosition;

    }

    @Override
    public List<Position> moveSet(iBoard board) {
        return move((BoardImpl) board);
    }

    //TODO macht das Sinn alles in diese Liste einzutragen und das Board filtert out of bounds sachen?
    private List<Position> move(BoardImpl board){
        List<Position> validMoves = new LinkedList<>();

        if (board.getCheckCount() >=2) {
            return new LinkedList<>();
        }
        springerMove(board, validMoves, NOTMAGICTWO, NOTMAGICONE);
        springerMove(board, validMoves, NOTMAGICONE, NOTMAGICTWO);
        if (board.getCheckCount() == 1) {
            validMoves = MoveSetAssist.getCheckedValidMoves(validMoves, board);
        }
        return validMoves;
    }

    private void springerMove(BoardImpl board, List<Position> validMoves, int notmagicone, int notmagictwo) {
        iPiece tempPiece = null;
        if (currentPosition.getX()+notmagicone <= board.getUPPERBOUNDS() && currentPosition.getY()+notmagictwo <= board.getUPPERBOUNDS()) {
            tempPiece = board.onField(currentPosition.getX()+notmagicone, currentPosition.getY()+notmagictwo);
            if (checkField(tempPiece)) {
                validMoves.add(new Position(currentPosition.getX()+ notmagicone, currentPosition.getY()+ notmagictwo));
            }
        }
        if (currentPosition.getX()+notmagicone <= board.getUPPERBOUNDS() && currentPosition.getY()-notmagictwo >= board.getLOWERBOUNDS()) {
            tempPiece = board.onField(currentPosition.getX()+ notmagicone, currentPosition.getY()- notmagictwo);
            if (checkField(tempPiece)) {
                validMoves.add(new Position(currentPosition.getX()+ notmagicone, currentPosition.getY()- notmagictwo));
            }
        }
        if (currentPosition.getX()-notmagicone >= board.getLOWERBOUNDS() && currentPosition.getY()+notmagictwo <= board.getUPPERBOUNDS()) {
            tempPiece = board.onField(currentPosition.getX()- notmagicone, currentPosition.getY()+ notmagictwo);
            if (checkField(tempPiece)) {
                validMoves.add(new Position(currentPosition.getX()- notmagicone, currentPosition.getY()+ notmagictwo));
            }
        }
        if (currentPosition.getX()-notmagicone >= board.getLOWERBOUNDS() && currentPosition.getY()-notmagictwo >= board.getLOWERBOUNDS()) {
            tempPiece = board.onField(currentPosition.getX()- notmagicone, currentPosition.getY()- notmagictwo);
            if (checkField(tempPiece)) {
                validMoves.add(new Position(currentPosition.getX()- notmagicone, currentPosition.getY()- notmagictwo));
            }
        }
    }

    private boolean checkField(iPiece tempPiece) {
        if (tempPiece == null || tempPiece.getColor()!=pieceColor) {
            return true;
        }
        return false;
    }

}
