package Model.Spiellogik.MoveSets;

import Model.Spiellogik.Color;
import Model.Spiellogik.Figuren.Position;
import javafx.geometry.Pos;

import java.util.LinkedList;
import java.util.List;

public class LaeuferMoveset implements iMoveSet{
    private Color pieceColor;
    private final int UPPER_BOUND = 8;
    private final int LOWER_BOUND = 0;

    public LaeuferMoveset(Color pieceColor) {
        this.pieceColor = pieceColor;
    }

    @Override
    public List<Position> moveSet(Position position) {
        return move(position);
    }

    //TODO JOSHUA NACH IMPLEMENTIERUNG FRAGEN
    private List<Position> move(Position currentPosition){
        List<Position> validMoves = new LinkedList<>();
        for (int i = currentPosition.getX(); i < UPPER_BOUND; i++){
            validMoves.add(new Position(i, currentPosition.getY()));
            validMoves.add(new Position(currentPosition.getX(), i));
        }
        for (int i = currentPosition.getX(); i >= LOWER_BOUND; i--){
            validMoves.add(new Position(i, currentPosition.getY()));
            validMoves.add(new Position(currentPosition.getX(), i));
        }
        return validMoves;
    }
}
