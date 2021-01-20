package Model.Spiellogik.MoveSets;

import Model.Spiellogik.*;
import Model.Spiellogik.Figuren.Position;
import Model.Spiellogik.Figuren.Typ;
import Model.Spiellogik.Figuren.iPiece;
import javafx.geometry.Pos;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MoveSetAssist {
    static int counter;
    static List<Position> getDiagonalMoveset(BoardImpl board, Color picecolor, Position currentPosition) {
        List<Position> validMoves = new LinkedList<>();
        boolean tr=true, tl=true, br=true, bl=true;
        for (int i = 1; i<=board.getUPPERBOUNDS(); i++){
            if (tr) {
                if (currentPosition.getX()+i > board.getUPPERBOUNDS() || currentPosition.getY()+i > board.getUPPERBOUNDS()) {
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
                if (currentPosition.getX()-i < board.getLOWERBOUNDS() || currentPosition.getY()+i > board.getUPPERBOUNDS()) {
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
                if (currentPosition.getX()+i>board.getUPPERBOUNDS() || currentPosition.getY()-i<board.getLOWERBOUNDS()) {
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
                if (currentPosition.getX()-i<board.getLOWERBOUNDS() || currentPosition.getY()-i<board.getLOWERBOUNDS()) {
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
                if (currentPosition.getY()+i > board.getUPPERBOUNDS()) {
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
                if (currentPosition.getX()+i > board.getUPPERBOUNDS()) {
                    right=false;
                } else {
                    if (board.onField(currentPosition.getX()+i, currentPosition.getY()) == null) {
                        validMoves.add(new Position(currentPosition.getX()+i, currentPosition.getY()));
                    } else if (board.onField(currentPosition.getX()+i, currentPosition.getY()).getColor() !=picecolor) {
                        validMoves.add(new Position(currentPosition.getX()+i, currentPosition.getY()));
                        right=false;
                    } else {
                        right=false;
                    }
                }
            }
            if (bottom) {
                if (currentPosition.getY()-i  < board.getLOWERBOUNDS()) {
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
                if (currentPosition.getX()-i < board.getLOWERBOUNDS()) {
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
    public static int countCheck(BoardImpl board, Color color, Position currentPosition) {
        boolean[] positionBooleanArray = new boolean[8];
        counter = 0;
        for (int i=0; i<8; i++) {
            positionBooleanArray[i] = true;
        }
        board.setCheckingPiece(null);
        for (int i=0; i<8; i++) {
            for (int j=1; j<=board.getUPPERBOUNDS(); j++) {
                iPiece tempPiece = null;
                switch (i) {
                    case 0: //direction: top-left
                        if (positionBooleanArray[i]) {
                            if (currentPosition.getX()-j >= board.getLOWERBOUNDS() && currentPosition.getY()+j <= board.getUPPERBOUNDS()) {
                                tempPiece = board.onField(currentPosition.getX()-j, currentPosition.getY()+j);
                            } else {
                                positionBooleanArray[i] = false;
                                break;
                            }
                        }
                        break;
                    case 1: //direction: top
                        if (positionBooleanArray[i]) {
                            if (currentPosition.getY()+j <= board.getUPPERBOUNDS()) {
                                tempPiece = board.onField(currentPosition.getX(), currentPosition.getY()+j);
                            } else {
                                positionBooleanArray[i] = false;
                                break;
                            }
                        }
                        break;
                    case 2: //direction: top-right
                        if (positionBooleanArray[i]) {
                            if (currentPosition.getX()+j <= board.getUPPERBOUNDS() && currentPosition.getY()+j <= board.getUPPERBOUNDS()) {
                                tempPiece = board.onField(currentPosition.getX()+j, currentPosition.getY()+j);
                            } else {
                                positionBooleanArray[i] = false;
                            }
                        }
                        break;
                    case 3: //direction: right
                        if (positionBooleanArray[i]) {
                            if (currentPosition.getX()+j <= board.getUPPERBOUNDS()) {
                                tempPiece = board.onField(currentPosition.getX()+j, currentPosition.getY());
                            } else {
                                positionBooleanArray[i] = false;
                            }
                        }
                        break;
                    case 4: //direction: bottom-right
                        if (positionBooleanArray[i]) {
                            if (currentPosition.getX()+j <= board.getUPPERBOUNDS() && currentPosition.getY()-j >= board.getLOWERBOUNDS()) {
                                tempPiece = board.onField(currentPosition.getX()+j, currentPosition.getY()-j);
                            } else {
                                positionBooleanArray[i] = false;
                            }
                        }
                        break;
                    case 5: //direction: bottom
                        if (positionBooleanArray[i]) {
                            if (currentPosition.getY()-j >= board.getLOWERBOUNDS()) {
                                tempPiece = board.onField(currentPosition.getX(), currentPosition.getY()-j);
                            } else {
                                positionBooleanArray[i] = false;
                            }
                        }
                        break;
                    case 6: //direction: bottom-left
                        if (positionBooleanArray[i]) {
                            if (currentPosition.getX()-j >= board.getLOWERBOUNDS() && currentPosition.getY()-j >= board.getLOWERBOUNDS()) {
                                tempPiece = board.onField(currentPosition.getX()-j, currentPosition.getY()-j);
                            } else {
                                positionBooleanArray[i] = false;
                            }
                        }
                        break;
                    case 7: //direction: left
                        if (positionBooleanArray[i]) {
                            if (currentPosition.getX()-j >= board.getLOWERBOUNDS()) {
                                tempPiece = board.onField(currentPosition.getX()-j, currentPosition.getY());
                            } else {
                                positionBooleanArray[i] = false;
                            }
                        }
                        break;
                }
                if (tempPiece != null && ((tempPiece.getType() == Typ.KOENIG && tempPiece.getColor() != color) || (tempPiece.getType() != Typ.KOENIG))) {
                    positionBooleanArray[i] = false;
                    if (tempPiece.getColor()!=color) {
                        if (j==1) {
                            if (tempPiece.getType() == Typ.KOENIG) {
                                board.setCheckingPiece(tempPiece);
                                counter++;
                            }
                        }
                        if (i%2 ==0) {
                            if (tempPiece.getType() == Typ.LAEUFER || tempPiece.getType() == Typ.DAME) {
                                board.setCheckingPiece(tempPiece);
                                counter++;
                            }
                            if (j==1) {
                                if (tempPiece.getType() == Typ.BAUER) {
                                    if (color==Color.White && (i==0 || i==2)) {
                                        board.setCheckingPiece(tempPiece);
                                        counter++;
                                    } else if (i==4 || i==6) {
                                        board.setCheckingPiece(tempPiece);
                                        counter++;
                                    }
                                }
                            }
                        } else {
                            if (tempPiece.getType() == Typ.TURM || tempPiece.getType() == Typ.DAME) {
                                board.setCheckingPiece(tempPiece);
                                counter++;
                            }
                        }
                    }
                }
            }
        }

        tempMethod(board, color, currentPosition, 1, 2);

        tempMethod(board, color, currentPosition,  2, 1);

        return counter;
    }

    private static void tempMethod(BoardImpl board, Color color, Position currentPosition, int x, int y) {
        iPiece tempPiece = null;
        if (currentPosition.getX()+x <= board.getUPPERBOUNDS() && currentPosition.getY()+y <= board.getUPPERBOUNDS()) {
            tempPiece = board.onField(currentPosition.getX()+x, currentPosition.getY()+y);
            if (checkField(tempPiece, color) && tempPiece.getType() == Typ.SPRINGER) {
                counter++;
                board.setCheckingPiece(tempPiece);
            }
        }
        if (currentPosition.getX()+x <= board.getUPPERBOUNDS() && currentPosition.getY()-y >= board.getLOWERBOUNDS()) {
            tempPiece = board.onField(currentPosition.getX()+ x, currentPosition.getY()- y);
            if (checkField(tempPiece, color) && tempPiece.getType() == Typ.SPRINGER) {
                counter++;
                board.setCheckingPiece(tempPiece);
            }
        }
        if (currentPosition.getX()-x >= board.getLOWERBOUNDS() && currentPosition.getY()+y <= board.getUPPERBOUNDS()) {
            tempPiece = board.onField(currentPosition.getX()- x, currentPosition.getY()+ y);
            if (checkField(tempPiece, color) && tempPiece.getType() == Typ.SPRINGER) {
                counter++;
                board.setCheckingPiece(tempPiece);
            }
        }
        if (currentPosition.getX()-x >= board.getLOWERBOUNDS() && currentPosition.getY()-y >= board.getLOWERBOUNDS()) {
            tempPiece = board.onField(currentPosition.getX()- x, currentPosition.getY()- y);
            if (checkField(tempPiece, color) && tempPiece.getType() == Typ.SPRINGER) {
                counter++;
                board.setCheckingPiece(tempPiece);
            }
        }
    }

    private static boolean checkField(iPiece tempPiece, Color color) {
        if (tempPiece == null) {
            return false;
        } else if (tempPiece.getColor()!=color) {
            return true;
        }
        return false;
    }

    public static List<Position> getPosiblePositions(BoardImpl board, Position currentPosition) {
        List<Position> posiblePositions = new LinkedList<>();
        iPiece checkingPiece = board.getCheckingPiece();
        int i=0;
        if (checkingPiece.getType() == Typ.SPRINGER || checkingPiece.getType() == Typ.BAUER) {
            posiblePositions.add(checkingPiece.getPosition());
        } else if (checkingPiece.getType() == Typ.LAEUFER) {
            getPosibleDiagonalPositions(currentPosition, posiblePositions, checkingPiece, i);
        } else if (checkingPiece.getType() == Typ.TURM) {
            getPosibleHorizontalVerticalPositions(currentPosition, posiblePositions, checkingPiece, i);

        } else if (checkingPiece.getType() == Typ.DAME) {
            if (currentPosition.getX() == checkingPiece.getPosition().getX() || currentPosition.getY() == checkingPiece.getPosition().getY()) {
                getPosibleHorizontalVerticalPositions(currentPosition, posiblePositions, checkingPiece, i);
            } else {
                getPosibleDiagonalPositions(currentPosition, posiblePositions, checkingPiece, i);
            }
        }
        return posiblePositions;
    }

    private static void getPosibleHorizontalVerticalPositions(Position currentPosition, List<Position> posiblePositions, iPiece checkingPiece, int i) {
        if (currentPosition.getX() == checkingPiece.getPosition().getX()) {
            if (currentPosition.getY()<checkingPiece.getPosition().getY()) {
                while (!(checkingPiece.getPosition().getY()-i == currentPosition.getY())) {
                    posiblePositions.add(new Position(checkingPiece.getPosition().getX(), checkingPiece.getPosition().getY()-i));
                    i++;
                }
            } else {
                while (!(checkingPiece.getPosition().getY()+i == currentPosition.getY())) {
                    posiblePositions.add(new Position(checkingPiece.getPosition().getX(), checkingPiece.getPosition().getY()+i));
                    i++;
                }
            }
        } else {
            if (currentPosition.getX()<checkingPiece.getPosition().getX()) {
                while (!(checkingPiece.getPosition().getX()-i == currentPosition.getX())) {
                    posiblePositions.add(new Position(checkingPiece.getPosition().getX()-i, checkingPiece.getPosition().getY()));
                    i++;
                }
            } else {
                while (!(checkingPiece.getPosition().getY()+i == currentPosition.getY())) {
                    posiblePositions.add(new Position(checkingPiece.getPosition().getX()+i, checkingPiece.getPosition().getY()));
                    i++;
                }
            }
        }
    }

    private static void getPosibleDiagonalPositions(Position currentPosition, List<Position> posiblePositions, iPiece checkingPiece, int i) {
        if (currentPosition.getX()<checkingPiece.getPosition().getX()) { // Koenig links vom Dame
            if (currentPosition.getY()<checkingPiece.getPosition().getY()) { // Koenig unterhalb Dame
                while (!(checkingPiece.getPosition().getX()-i == currentPosition.getX()
                        && checkingPiece.getPosition().getY()-i == currentPosition.getY())) {
                    posiblePositions.add(new Position(checkingPiece.getPosition().getX()-i, checkingPiece.getPosition().getY()-i));
                    i++;
                }
            } else { // Koenig oberhalb Dame
                while (!(checkingPiece.getPosition().getX()-i == currentPosition.getX()
                        && checkingPiece.getPosition().getY()+i == currentPosition.getY())) {
                    posiblePositions.add(new Position(checkingPiece.getPosition().getX()-i, checkingPiece.getPosition().getY()+i));
                    i++;
                }
            }
        } else { // Koenig rechts vom Dame
            if (currentPosition.getY()<checkingPiece.getPosition().getY()) { // Koenig unterhalb Dame
                while (!(checkingPiece.getPosition().getX()+i == currentPosition.getX()
                        && checkingPiece.getPosition().getY()-i == currentPosition.getY())) {
                    posiblePositions.add(new Position(checkingPiece.getPosition().getX()+i, checkingPiece.getPosition().getY()-i));
                    i++;
                }
            } else { // Koenig oberhalb Dame
                while (!(checkingPiece.getPosition().getX()+i == currentPosition.getX()
                        && checkingPiece.getPosition().getY()+i == currentPosition.getY())) {
                    posiblePositions.add(new Position(checkingPiece.getPosition().getX()+i, checkingPiece.getPosition().getY()+i));
                    i++;
                }
            }
        }
    }

    public static List<Position> getCheckedValidMoves(List<Position> validMoves, BoardImpl board) {
        List<Position> posiblePositions = board.getPosiblePositions();
        List<Position> returnList = new LinkedList<>();
        for (Position valid : validMoves) {
            for (Position posible : posiblePositions) {
                if (valid.getX() == posible.getX() && valid.getY() == posible.getY()) {
                    returnList.add(posible);
                }
            }
        }
        return returnList;
    }

    public static void setPinned(BoardImpl board) {
        boolean[] positionBooleanArray = new boolean[8];
        iPiece friendlyPiece = null;
        Color color;
        iPiece king = null;
        if (board.getStatus() == Status.TURN_WHITE) {
            color = Color.White;
        } else {
            color = Color.Black;
        }

        Map<iPlayer, List<iPiece>> map = board.getMap();

        for (Map.Entry<iPlayer, List<iPiece>> entry : map.entrySet()) {
            if (entry.getKey().getColor() == color) {
                List<iPiece> temp = entry.getValue();
                for (iPiece piece : temp) {
                    if (piece.getType() == Typ.KOENIG) {
                        king = piece;
                    }
                    piece.setPinned(false);
                }
            }
        }

        if (king != null) {
            for (int i=0; i<8; i++) {
                positionBooleanArray[i] = true;
            }
            for (int i=0; i<8; i++) {
                friendlyPiece = null;
                for (int j = 1; j <= board.getUPPERBOUNDS(); j++) {
                    iPiece tempPiece = null;
                    switch (i) {
                        case 0: //direction: top-left
                            if (positionBooleanArray[i]) {
                                if (king.getPosition().getX() - j >= board.getLOWERBOUNDS() && king.getPosition().getY() + j <= board.getUPPERBOUNDS()) {
                                    tempPiece = board.onField(king.getPosition().getX() - j, king.getPosition().getY() + j);
                                } else {
                                    positionBooleanArray[i] = false;
                                    break;
                                }
                            }
                            break;
                        case 1: //direction: top
                            if (positionBooleanArray[i]) {
                                if (king.getPosition().getY() + j <= board.getUPPERBOUNDS()) {
                                    tempPiece = board.onField(king.getPosition().getX(), king.getPosition().getY() + j);
                                } else {
                                    positionBooleanArray[i] = false;
                                    break;
                                }
                            }
                            break;
                        case 2: //direction: top-right
                            if (positionBooleanArray[i]) {
                                if (king.getPosition().getX() + j <= board.getUPPERBOUNDS() && king.getPosition().getY() + j <= board.getUPPERBOUNDS()) {
                                    tempPiece = board.onField(king.getPosition().getX() + j, king.getPosition().getY() + j);
                                } else {
                                    positionBooleanArray[i] = false;
                                }
                            }
                            break;
                        case 3: //direction: right
                            if (positionBooleanArray[i]) {
                                if (king.getPosition().getX() + j <= board.getUPPERBOUNDS()) {
                                    tempPiece = board.onField(king.getPosition().getX() + j, king.getPosition().getY());
                                } else {
                                    positionBooleanArray[i] = false;
                                }
                            }
                            break;
                        case 4: //direction: bottom-right
                            if (positionBooleanArray[i]) {
                                if (king.getPosition().getX() + j <= board.getUPPERBOUNDS() && king.getPosition().getY() - j >= board.getLOWERBOUNDS()) {
                                    tempPiece = board.onField(king.getPosition().getX() + j, king.getPosition().getY() - j);
                                } else {
                                    positionBooleanArray[i] = false;
                                }
                            }
                            break;
                        case 5: //direction: bottom
                            if (positionBooleanArray[i]) {
                                if (king.getPosition().getY() - j >= board.getLOWERBOUNDS()) {
                                    tempPiece = board.onField(king.getPosition().getX(), king.getPosition().getY() - j);
                                } else {
                                    positionBooleanArray[i] = false;
                                }
                            }
                            break;
                        case 6: //direction: bottom-left
                            if (positionBooleanArray[i]) {
                                if (king.getPosition().getX() - j >= board.getLOWERBOUNDS() && king.getPosition().getY() - j >= board.getLOWERBOUNDS()) {
                                    tempPiece = board.onField(king.getPosition().getX() - j, king.getPosition().getY() - j);
                                } else {
                                    positionBooleanArray[i] = false;
                                }
                            }
                            break;
                        case 7: //direction: left
                            if (positionBooleanArray[i]) {
                                if (king.getPosition().getX() - j >= board.getLOWERBOUNDS()) {
                                    tempPiece = board.onField(king.getPosition().getX() - j, king.getPosition().getY());
                                } else {
                                    positionBooleanArray[i] = false;
                                }
                            }
                            break;
                    }
                    if (tempPiece != null) {
                        if (tempPiece.getColor()==color) {
                            if (friendlyPiece == null) {
                                friendlyPiece = tempPiece;
                            } else {
                                positionBooleanArray[i] = false;
                            }
                        } else {
                            if (friendlyPiece != null) {
                                if (i%2 == 0) {
                                    if (tempPiece.getType() == Typ.LAEUFER || tempPiece.getType() == Typ.DAME) {
                                        friendlyPiece.setPinned(true);
                                    }
                                } else {
                                    if (tempPiece.getType() == Typ.TURM || tempPiece.getType() == Typ.DAME) {
                                        friendlyPiece.setPinned(true);
                                    }
                                }
                            } else {
                                positionBooleanArray[i] = false;
                            }
                        }
                    }
                }
            }
        }
    }
}
