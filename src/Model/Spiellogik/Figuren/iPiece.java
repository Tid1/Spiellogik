package Model.Spiellogik.Figuren;

import Model.Spiellogik.Color;
import Model.Spiellogik.MoveSets.iMoveSet;

public interface iPiece {
    Color getColor();
    Typ getType();
    Position getPosition();
    void setPosition(Position position);
    boolean isCaptured();
    void setCaputred(boolean caputred);
    iMoveSet getMoveset();
}
