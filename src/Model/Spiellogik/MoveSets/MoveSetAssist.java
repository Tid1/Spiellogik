package Model.Spiellogik.MoveSets;

import Model.Spiellogik.*;
import Model.Spiellogik.Figuren.Position;

import java.util.LinkedList;
import java.util.List;

public class MoveSetAssist {
    static List<Position> getDiagonalMoveset(BoardImpl board, Color picecolor, Position currentPosition) {
        List<Position> validMoves = new LinkedList<>();
        boolean tr=true, tl=true, br=true, bl=true;
        for (int i = 1; i<=board.getUPPERBOUNDS(); i++){
            if (tr) {
                if (currentPosition.getX()+i>board.getUPPERBOUNDS() || currentPosition.getY()+i>board.getUPPERBOUNDS()) {
                    tr=false;
                } else {
                    if (board.onField(currentPosition.getX()+i, currentPosition.getY()+i) == null) {
                        validMoves.add(new Position(currentPosition.getX()+i, currentPosition.getY()+i));
                    } else if (board.onField(currentPosition.getX()+i, currentPosition.getY()+i).getColor() !=picecolor) {
                        validMoves.add(new Position(currentPosition.getX()+i, currentPosition.getY()+i));
                        tr=false;
                    } else {
                        tr=false;
                    }
                }
            }
            if (tl) {
                if (currentPosition.getX()-i>board.getLOWERBOUNDS() || currentPosition.getY()+i>board.getUPPERBOUNDS()) {
                    tl=false;
                } else {
                    if (board.onField(currentPosition.getX()-i, currentPosition.getY()+i) == null) {
                        validMoves.add(new Position(currentPosition.getX()-i, currentPosition.getY()+i));
                    } else if (board.onField(currentPosition.getX()-i, currentPosition.getY()+i).getColor() !=picecolor) {
                        validMoves.add(new Position(currentPosition.getX()-i, currentPosition.getY()+i));
                        tl=false;
                    } else {
                        tl=false;
                    }
                }
            }
            if (br) {
                if (currentPosition.getX()+i>board.getUPPERBOUNDS() || currentPosition.getY()-i>board.getLOWERBOUNDS()) {
                    br=false;
                } else {
                    if (board.onField(currentPosition.getX()+i, currentPosition.getY()-i) == null) {
                        validMoves.add(new Position(currentPosition.getX()+i, currentPosition.getY()-i));
                    } else if (board.onField(currentPosition.getX()+i, currentPosition.getY()-i).getColor() !=picecolor) {
                        validMoves.add(new Position(currentPosition.getX()+i, currentPosition.getY()-i));
                        br=false;
                    } else {
                        br=false;
                    }
                }
            }
            if (bl) {
                if (currentPosition.getX()-i>board.getLOWERBOUNDS() || currentPosition.getY()-i>board.getLOWERBOUNDS()) {
                    bl=false;
                } else {
                    if (board.onField(currentPosition.getX()-i, currentPosition.getY()-i) == null) {
                        validMoves.add(new Position(currentPosition.getX()-i, currentPosition.getY()-i));
                    } else if (board.onField(currentPosition.getX()-i, currentPosition.getY()-i).getColor() !=picecolor) {
                        validMoves.add(new Position(currentPosition.getX()-i, currentPosition.getY()-i));
                        bl=false;
                    } else {
                        bl=false;
                    }
                }
            }
            if (!tr && !tl && !br && !bl) {
                break;
            }
        }
        return validMoves;
    }

    static List<Position> getHorizontalVerticalMoveset(BoardImpl board, Color picecolor, Position currentPosition) {
        List<Position> validMoves = new LinkedList<>();
        boolean left=true, right=true, top=true, bottom=true;
        for (int i = 1; i<=board.getUPPERBOUNDS(); i++){
            if (top) {
                if (currentPosition.getY()+i>board.getUPPERBOUNDS()) {
                    top=false;
                } else {
                    if (board.onField(currentPosition.getX(), currentPosition.getY()+i) == null) {
                        validMoves.add(new Position(currentPosition.getX(), currentPosition.getY()+i));
                    } else if (board.onField(currentPosition.getX(), currentPosition.getY()+i).getColor() !=picecolor) {
                        validMoves.add(new Position(currentPosition.getX(), currentPosition.getY()+i));
                        top=false;
                    } else {
                        top=false;
                    }
                }
            }
            if (right) {
                if (currentPosition.getX()+i>board.getLOWERBOUNDS()) {
                    right=false;
                } else {
                    if (board.onField(currentPosition.getX()+i, currentPosition.getY()) == null) {
                        validMoves.add(new Position(currentPosition.getX()+i, currentPosition.getY()));
                    } else if (board.onField(currentPosition.getX()+i, currentPosition.getY()+i).getColor() !=picecolor) {
                        validMoves.add(new Position(currentPosition.getX()+i, currentPosition.getY()));
                        right=false;
                    } else {
                        right=false;
                    }
                }
            }
            if (bottom) {
                if (currentPosition.getY()-i>board.getLOWERBOUNDS()) {
                    bottom=false;
                } else {
                    if (board.onField(currentPosition.getX(), currentPosition.getY()-i) == null) {
                        validMoves.add(new Position(currentPosition.getX(), currentPosition.getY()-i));
                    } else if (board.onField(currentPosition.getX(), currentPosition.getY()-i).getColor() !=picecolor) {
                        validMoves.add(new Position(currentPosition.getX(), currentPosition.getY()-i));
                        bottom=false;
                    } else {
                        bottom=false;
                    }
                }
            }
            if (left) {
                if (currentPosition.getX()-i>board.getLOWERBOUNDS()) {
                    left=false;
                } else {
                    if (board.onField(currentPosition.getX()-i, currentPosition.getY()) == null) {
                        validMoves.add(new Position(currentPosition.getX()-i, currentPosition.getY()));
                    } else if (board.onField(currentPosition.getX()-i, currentPosition.getY()).getColor() !=picecolor) {
                        validMoves.add(new Position(currentPosition.getX()-i, currentPosition.getY()));
                        left=false;
                    } else {
                        left=false;
                    }
                }
            }
            if (!top && !right && !bottom && !left) {
                break;
            }
        }
        return validMoves;
    }
}
