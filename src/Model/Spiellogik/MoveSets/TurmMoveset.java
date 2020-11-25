package Model.Spiellogik.MoveSets;

import Model.Spiellogik.Color;
import Model.Spiellogik.Figuren.Position;

import java.util.LinkedList;
import java.util.List;

public class TurmMoveset implements iMoveSet{
    private Color pieceColor;
    private final int UPPER_BOUNDS = 8;
    private final int LOWER_BOUNDS = 0;

    public TurmMoveset(Color color){
        this.pieceColor = color;
    }

    @Override
    public List<Position> moveSet(Position position) {
        return move(position);
    }

    private List<Position> move(Position currentPosition){
        List<Position> validMoves = new LinkedList<>();
        for (int i = currentPosition.getX(); i <= UPPER_BOUNDS; i++){
            validMoves.add(new Position(i, currentPosition.getY()));
        }

        for (int i = currentPosition.getX(); i >= LOWER_BOUNDS; i--){
            validMoves.add(new Position(i, currentPosition.getY()));
        }

        for (int i = currentPosition.getY(); i <= UPPER_BOUNDS; i++){
            validMoves.add(new Position(currentPosition.getX(), i));
        }

        for (int i = currentPosition.getY(); i >= LOWER_BOUNDS; i--){
            validMoves.add(new Position(currentPosition.getX(), i));
        }
        return validMoves;
    }

}
