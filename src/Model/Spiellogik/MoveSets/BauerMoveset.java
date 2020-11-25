package Model.Spiellogik.MoveSets;

import Model.Spiellogik.Color;

import Model.Spiellogik.Figuren.Position;
import sun.awt.image.ImageWatched;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class BauerMoveset implements iMoveSet{
    boolean hasMoved = false;
    Color pieceColor;
    boolean test = false;
    private final int UPPER_BOUNDS = 8;
    private final int LOWER_BOUNDS = 0;

    public BauerMoveset(Color color){
        this.pieceColor = color;
    }

    @Override
    public List<Position> moveSet(Position position) {
        List<Position> validPostitions = new LinkedList<>();
        switch (pieceColor){
            case White:
                validPostitions = moveWhite(position);
                break;
            case Black:
                validPostitions = moveBlack(position);
                break;
            default:
                break;
        }
        return validPostitions;
    }

    private List<Position> moveBlack(Position currentPosition){
        List<Position> validPositions = new LinkedList<>();
        if(this.pieceColor == Color.Black){
            if (this.firstMove()){
                if (currentPosition.getY()-2 >= LOWER_BOUNDS && currentPosition.getY()-2 < UPPER_BOUNDS){
                    validPositions.add(new Position(currentPosition.getX(), currentPosition.getY()-2));
                    hasMoved = false;
                }
            }
            if (currentPosition.getY()-1 >= LOWER_BOUNDS && currentPosition.getY()-1 < UPPER_BOUNDS){
                validPositions.add(new Position(currentPosition.getX(), currentPosition.getY()-1));
            }
        }
        return validPositions;
    }

    private List<Position> moveWhite(Position currentPosition){
        List<Position> validPositions = new LinkedList<>();
        if (this.pieceColor == Color.White){
            if (this.firstMove()){
                if (currentPosition.getY()+2 >= LOWER_BOUNDS && currentPosition.getY()+2 < UPPER_BOUNDS){
                    validPositions.add(new Position(currentPosition.getX(), currentPosition.getY()+2));
                    hasMoved = false;
                }
            }
            if (currentPosition.getY()+1 >= LOWER_BOUNDS && currentPosition.getY()+1 < UPPER_BOUNDS){
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
