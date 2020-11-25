package Model.Spiellogik.MoveSets;

import Model.Spiellogik.Color;
import Model.Spiellogik.Figuren.Position;
import Model.Spiellogik.Figuren.Springer;
import javafx.geometry.Pos;

import java.util.LinkedList;
import java.util.List;

public class SpringerMoveset implements iMoveSet{
    private Color pieceColor;
    private final int UPPER_BOUND = 8;
    private final int LOWER_BOUND = 0;

    public SpringerMoveset(Color pieceColor){
        this.pieceColor = pieceColor;
    }

    @Override
    public List<Position> moveSet(Position position) {
        return null;
    }

    //TODO macht das Sinn alles in diese Liste einzutragen und das Board filtert out of bounds sachen?
    private List<Position> move(Position position){
        List<Position> validMoves = new LinkedList<>();
        return validMoves;
    }

}
