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

    private final int SINGLE_MOVE = 1;
    private final int DOUBLE_MOVE = 2;

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
                if (currentPosition.getY()-DOUBLE_MOVE >= LOWER_BOUNDS && currentPosition.getY()-DOUBLE_MOVE < UPPER_BOUNDS){
                    validPositions.add(new Position(currentPosition.getX(), currentPosition.getY()-DOUBLE_MOVE));
                    hasMoved = false;
                }
            }
            if (currentPosition.getY()-SINGLE_MOVE >= LOWER_BOUNDS && currentPosition.getY()-SINGLE_MOVE < UPPER_BOUNDS){
                validPositions.add(new Position(currentPosition.getX(), currentPosition.getY()-SINGLE_MOVE));
            }
        }
        return validPositions;
    }

    private List<Position> moveWhite(Position currentPosition){
        List<Position> validPositions = new LinkedList<>();
        if (this.pieceColor == Color.White){
            if (this.firstMove()){
                if (currentPosition.getY()+DOUBLE_MOVE >= LOWER_BOUNDS && currentPosition.getY()+DOUBLE_MOVE < UPPER_BOUNDS){
                    validPositions.add(new Position(currentPosition.getX(), currentPosition.getY()+DOUBLE_MOVE));
                    hasMoved = false;
                }
            }
            if (currentPosition.getY()+SINGLE_MOVE >= LOWER_BOUNDS && currentPosition.getY()+SINGLE_MOVE < UPPER_BOUNDS){
                validPositions.add(new Position((currentPosition.getX()), currentPosition.getY()+SINGLE_MOVE));
            }
        }
        return validPositions;
    }

    public boolean firstMove(){
        this.hasMoved = true;
        return true;
    }
}
