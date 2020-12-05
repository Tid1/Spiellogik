package Model.Spiellogik.MoveSets;

import Model.Spiellogik.Color;
import Model.Spiellogik.Figuren.Position;
import Model.Spiellogik.iBoard;
import javafx.geometry.Pos;

import java.util.LinkedList;
import java.util.List;

public class TurmMoveset implements iMoveSet{
    private Color pieceColor;
    private Position currentPosition;
    private final int UPPER_BOUNDS = 8;
    private final int LOWER_BOUNDS = 0;

    public TurmMoveset(Color color, Position currentPosition){
        this.pieceColor = color;
        this.currentPosition = currentPosition;
    }

    @Override
    public List<Position> moveSet(iBoard board) {
        return move(board);
    }

    private List<Position> move(iBoard board){
        List<Position> validMoves = new LinkedList<>();
        for (int i = currentPosition.getX(); i < UPPER_BOUNDS; i++){
            validMoves.add(new Position(i, currentPosition.getY()));
        }

        for (int i = currentPosition.getX(); i >= LOWER_BOUNDS; i--){
            validMoves.add(new Position(i, currentPosition.getY()));
        }

        for (int i = currentPosition.getY(); i < UPPER_BOUNDS; i++){
            validMoves.add(new Position(currentPosition.getX(), i));
        }

        for (int i = currentPosition.getY(); i >= LOWER_BOUNDS; i--){
            validMoves.add(new Position(currentPosition.getX(), i));
        }
        return validMoves;
    }

}
