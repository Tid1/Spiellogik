package Model.Spiellogik.Figuren;

import Model.Spiellogik.Color;
import Model.Spiellogik.MoveSets.SpringerMoveset;
import Model.Spiellogik.MoveSets.iMoveSet;

public class Springer implements iPiece{
    public static final long serialVersionUID = 444L;
    private final Color COLOR;
    private final Typ TYP = Typ.SPRINGER;
    private Position position;
    private boolean captured = false;
    private boolean pinned = false;

    public Springer(Color color){
        this.COLOR = color;
    }
    public Springer(Color color, Position position){
        this.COLOR = color;
        this.position = position;
    }

    @Override
    public Color getColor() {
        return COLOR;
    }

    @Override
    public Typ getType() {
        return TYP;
    }

    @Override
    public Position getPosition() {
        return position;
    }

    @Override
    public void setPosition(int x, int y) {
        this.position = new Position(x, y);
    }

    @Override
    public boolean isPinend() {
        return pinned;
    }

    @Override
    public void setPinned(boolean pinned) {
        this.pinned = pinned;
    }

    @Override
    public boolean isCaptured() {
        return captured;
    }

    @Override
    public void setCaputred(boolean caputred) {
        this.captured = caputred;
    }

    @Override
    public iMoveSet getMoveset() {
        return new SpringerMoveset(COLOR, this.position);
    }
}
