package Model.Spiellogik.MoveSets;

import Model.Spiellogik.*;
import Model.Spiellogik.Figuren.Position;

import java.util.LinkedList;
import java.util.List;

public class MoveSetAssist {
    static List<Position> getDiagonalMoveset(BoardImpl board, Color picecolor, Position currentPosition) {
        List<Position> validMoves = new LinkedList<>();
        for (int i = currentPosition.getX(); i < board.getUPPERBOUNDS(); i++){
            validMoves.add(new Position(i, currentPosition.getY()));
            validMoves.add(new Position(currentPosition.getX(), i));
        }

        return validMoves;
    }
}
