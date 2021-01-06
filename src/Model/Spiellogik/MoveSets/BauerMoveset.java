package Model.Spiellogik.MoveSets;

import Model.Spiellogik.BoardImpl;
import Model.Spiellogik.Color;

import Model.Spiellogik.Figuren.Position;
import Model.Spiellogik.Figuren.iPiece;
import Model.Spiellogik.iBoard;

import java.util.LinkedList;
import java.util.List;

public class BauerMoveset implements iMoveSet{
    Color pieceColor;
    private Position currentPosition;
    private final int UPPER_BOUNDS = 8;
    private final int LOWER_BOUNDS = 1;

    private final int WHITE_PAWN_START = 2;
    private final int BLACK_PAWN_START = 7;

    private final int SINGLE_MOVE = 1;
    private final int DOUBLE_MOVE = 2;

    public BauerMoveset(Color color, Position position){
        this.pieceColor = color;
        this.currentPosition = position;
    }

    @Override
    public List<Position> moveSet(iBoard board) {
        return move((BoardImpl) board);
    }

    private List<Position> move(BoardImpl board) {
        List<Position> validPostitions = new LinkedList<>();
        if (board.getCheckCount() >=2) {
            return new LinkedList<>();
        }
        switch (pieceColor){
            case White:
                validPostitions = moveWhite(board);
                break;
            case Black:
                validPostitions = moveBlack(board);
                break;
            default:
                break;
        }
        if (board.getCheckCount() == 1) {
            validPostitions = MoveSetAssist.getCheckedValidMoves(validPostitions, board);
        }
        return validPostitions;
    }

    private List<Position> moveBlack(BoardImpl board){
        List<Position> validPositions = new LinkedList<>();
        if(this.pieceColor == Color.Black){
            if (currentPosition.getY()-SINGLE_MOVE >= LOWER_BOUNDS && currentPosition.getY()-SINGLE_MOVE < UPPER_BOUNDS){
                if (board.onField(currentPosition.getX(), currentPosition.getY()- SINGLE_MOVE) == null){
                    validPositions.add(new Position(currentPosition.getX(), currentPosition.getY()-SINGLE_MOVE));
                }
            }
            if (currentPosition.getY() == BLACK_PAWN_START){
                if (board.onField(currentPosition.getX(), currentPosition.getY()- DOUBLE_MOVE) == null){
                    validPositions.add(new Position(currentPosition.getX(), currentPosition.getY()-DOUBLE_MOVE));
                }
            }
            if (currentPosition.getX() - SINGLE_MOVE >= LOWER_BOUNDS){
                iPiece pieceOnField = board.onField(currentPosition.getX()-SINGLE_MOVE, currentPosition.getY()-SINGLE_MOVE);
                if (pieceOnField != null && pieceOnField.getColor() == Color.White){
                    validPositions.add(new Position(currentPosition.getX()-SINGLE_MOVE, currentPosition.getY()-SINGLE_MOVE));
                }
            }
            if (currentPosition.getX() + SINGLE_MOVE < UPPER_BOUNDS){
                iPiece pieceOnField = board.onField(currentPosition.getX()+SINGLE_MOVE, currentPosition.getY()-SINGLE_MOVE);
                if (pieceOnField != null && pieceOnField.getColor() == Color.White){
                    validPositions.add(new Position(currentPosition.getX()+SINGLE_MOVE, currentPosition.getY()-SINGLE_MOVE));
                }
            }
        }
        return validPositions;
    }

    private List<Position> moveWhite(BoardImpl board) {
        List<Position> validPositions = new LinkedList<>();
        if (this.pieceColor == Color.White) {
            if (currentPosition.getY()+SINGLE_MOVE >= LOWER_BOUNDS && currentPosition.getY()+SINGLE_MOVE < UPPER_BOUNDS){
                if (board.onField(currentPosition.getX(), currentPosition.getY()+SINGLE_MOVE) == null){
                    validPositions.add(new Position(currentPosition.getX(), currentPosition.getY()+SINGLE_MOVE));
                }
            }
            if (currentPosition.getY() == WHITE_PAWN_START){
                if (board.onField(currentPosition.getX(), currentPosition.getY()+DOUBLE_MOVE) == null){
                    validPositions.add(new Position(currentPosition.getX(), currentPosition.getY()+DOUBLE_MOVE));
                }
            }
            if (currentPosition.getX() - SINGLE_MOVE >= LOWER_BOUNDS){
                iPiece pieceOnField = board.onField(currentPosition.getX()-SINGLE_MOVE, currentPosition.getY()+SINGLE_MOVE);
                if (pieceOnField != null && pieceOnField.getColor() == Color.White){
                    validPositions.add(new Position(currentPosition.getX()-SINGLE_MOVE, currentPosition.getY()+SINGLE_MOVE));
                }
            }
            if (currentPosition.getX() + SINGLE_MOVE < UPPER_BOUNDS){
                iPiece pieceOnField = board.onField(currentPosition.getX()+SINGLE_MOVE, currentPosition.getY()+SINGLE_MOVE);
                if (pieceOnField != null && pieceOnField.getColor() == Color.White){
                    validPositions.add(new Position(currentPosition.getX()+SINGLE_MOVE, currentPosition.getY()+SINGLE_MOVE));
                }
            }
        }
        return validPositions;
    }

}
