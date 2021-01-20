package Model.Spiellogik.Figuren;

import Model.Spiellogik.Color;
import Model.Spiellogik.MoveSets.TurmMoveset;
import Model.Spiellogik.MoveSets.iMoveSet;

public class Turm implements iPiece{
    public static final long serialVersionUID = 55L;
    private final Color COLOR;
    private final Typ TYP = Typ.TURM;
    private Position position;
    private boolean captured = false;
    private boolean hasMoved = false; //TODO Joshua Fragen ob sowas Sinn macht für Turm und König weil Rochade
    private boolean pinned = false;

    public Turm(Color color){
        this.COLOR = color;
    }
    public Turm(Color color, Position position){
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
        return new TurmMoveset(COLOR, this.position);
    }

    public boolean hasMoved() {
        return hasMoved;
    }

    public void setMoved(){
        this.hasMoved = true;
    }
}
