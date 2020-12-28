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
        return move(board);
    }

    private List<Position> move(iBoard board){
        List<Position> validMoves = MoveSetAssist.getHorizontalVerticalMoveset((BoardImpl) board, pieceColor, currentPosition);
        return validMoves;
    }

}
