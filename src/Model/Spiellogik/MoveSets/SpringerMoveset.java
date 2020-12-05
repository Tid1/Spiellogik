package Model.Spiellogik.MoveSets;

import Model.Spiellogik.*;
import Model.Spiellogik.Figuren.*;

import java.util.LinkedList;
import java.util.List;

public class SpringerMoveset implements iMoveSet{
    private Color pieceColor;
    private Position currentPosition;
    private final int UPPER_BOUND = 8;
    private final int LOWER_BOUND = 0;

    public SpringerMoveset(Color pieceColor, Position currentPosition){
        this.pieceColor = pieceColor;
        this.currentPosition = currentPosition;

    }

    @Override
    public List<Position> moveSet(iBoard board) {
        return null;
    }

    //TODO macht das Sinn alles in diese Liste einzutragen und das Board filtert out of bounds sachen?
    private List<Position> move(Position position){
        List<Position> validMoves = new LinkedList<>();
        return validMoves;
    }

}
