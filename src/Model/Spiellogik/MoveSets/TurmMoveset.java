package Model.Spiellogik.MoveSets;

import Model.Spiellogik.BoardImpl;
import Model.Spiellogik.Color;
import Model.Spiellogik.Figuren.Position;
import Model.Spiellogik.iBoard;
import javafx.geometry.Pos;

import java.util.LinkedList;
import java.util.List;

public class TurmMoveset implements iMoveSet{
    private Color pieceColor;
    private Position currentPosition;

    public TurmMoveset(Color color, Position currentPosition){
        this.pieceColor = color;
        this.currentPosition = currentPosition;
    }

    @Override
    public List<Position> moveSet(iBoard board) {
        return move((BoardImpl) board);
    }

    private List<Position> move(BoardImpl board){
        if (board.getCheckCount() >=2) {
            return new LinkedList<>();
        }
        if (board.onField(currentPosition.getX(), currentPosition.getY()).isPinend()) {
            return new LinkedList<>();
        }
        List<Position> validMoves = MoveSetAssist.getHorizontalVerticalMoveset(board, pieceColor, currentPosition);
        if (board.getCheckCount() == 1) {
            validMoves = MoveSetAssist.getCheckedValidMoves(validMoves, board);
        }
        return validMoves;
    }

}
