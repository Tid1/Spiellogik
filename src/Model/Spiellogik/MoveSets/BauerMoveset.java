package Model.Spiellogik.MoveSets;

import Model.Spiellogik.Color;
import Model.Spiellogik.Figuren.Bauer;
import Model.Spiellogik.Figuren.Position;
import Model.Spiellogik.Figuren.iPiece;
import javafx.geometry.Pos;

import javax.print.DocFlavor;
import java.util.ArrayList;
import java.util.List;

public class BauerMoveset implements iMoveSet{
    boolean hasMoved = false;
    Color pieceColor;
    boolean test = false;
    private final int BOUNDS = 8;

    public BauerMoveset(Color color){
        this.pieceColor = color;
    }

    @Override
    public List<Position> moveSet(Position position) {
        return null;
    }

    private List<Position> moveBlack(Position currentPosition){
        List<Position> validPositions = new ArrayList<>();
        if(this.pieceColor == Color.Black){
            if (this.firstMove()){
                if (currentPosition.getY()-2 > 0 && currentPosition.getY()-2 < BOUNDS){
                    validPositions.add(new Position(currentPosition.getX(), currentPosition.getY()-2));
                    hasMoved = false;
                }
            }
            if (currentPosition.getY()-1 > 0 && currentPosition.getY()-1 < BOUNDS){
                validPositions.add(new Position(currentPosition.getX(), currentPosition.getY()-1));
            }
        }
        return validPositions;
    }

    private List<Position> moveWhite(Position currentPosition){
        List<Position> validPositions = new ArrayList<>();
        if (this.pieceColor == Color.White){
            if (this.firstMove()){
                if (currentPosition.getY()+2 > 0 && currentPosition.getY()+2 < BOUNDS){
                    validPositions.add(new Position(currentPosition.getX(), currentPosition.getY()+2));
                    hasMoved = false;
                }
            }
            if (currentPosition.getY()+1 > 0 && currentPosition.getY()+1 < BOUNDS){
                validPositions.add(new Position((currentPosition.getX()), currentPosition.getY()+1));
            }
        }
        return validPositions;
    }

    public boolean firstMove(){
        this.hasMoved = true;
        return true;
    }
}
