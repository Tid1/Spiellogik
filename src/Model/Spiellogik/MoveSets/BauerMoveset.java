package Model.Spiellogik.MoveSets;

import Model.Spiellogik.Color;
import Model.Spiellogik.Figuren.Bauer;
import Model.Spiellogik.Figuren.Position;
import Model.Spiellogik.Figuren.iPiece;
import javafx.geometry.Pos;

import java.util.ArrayList;
import java.util.List;

public class BauerMoveset implements iMoveSet{
    boolean hasMoved = false;
    Color pieceColor;

    public BauerMoveset(Color color){
        this.pieceColor = color;
    }

    @Override
    public List<Position> moveSet(Position position) {
        return null;
    }

    public List<Position> move(Position currentPosition){
        List<Position> validPositions = new ArrayList<>();
        if (this.firstMove()){
           //TODO welche Dimension bei first move? new Position(x+2, y) oder new Position(x, y+2)?
        }
    }

    public boolean firstMove(){
        this.hasMoved = true;
        return true;
    }
}
