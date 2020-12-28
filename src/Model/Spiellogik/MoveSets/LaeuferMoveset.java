package Model.Spiellogik.MoveSets;

import Model.Spiellogik.BoardImpl;
import Model.Spiellogik.Color;
import Model.Spiellogik.Figuren.Position;
import Model.Spiellogik.iBoard;
import javafx.geometry.Pos;

import java.util.LinkedList;
import java.util.List;

public class LaeuferMoveset implements iMoveSet{
    private Color pieceColor;
    private Position currentPosition;

    public LaeuferMoveset(Color pieceColor, Position currentPosition) {
        this.pieceColor = pieceColor;
        this.currentPosition = currentPosition;
    }

    @Override
    public List<Position> moveSet(iBoard board) {
        return move(board);
    }

    //TODO JOSHUA NACH IMPLEMENTIERUNG FRAGEN
    private List<Position> move(iBoard board){
        List<Position> validMoves = MoveSetAssist.getDiagonalMoveset((BoardImpl) board, pieceColor, currentPosition);
        return validMoves;
    }
}
