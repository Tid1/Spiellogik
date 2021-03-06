package Model.Spiellogik.Figuren;

import Model.Spiellogik.Color;
import Model.Spiellogik.MoveSets.BauerMoveset;
import Model.Spiellogik.MoveSets.KoenigMoveset;
import Model.Spiellogik.MoveSets.iMoveSet;

public class Koenig implements iPiece {
    public static final long serialVersionUID = 40L;
    private final Color COLOR;
    private final Typ TYP = Typ.KOENIG;
    private Position position;
    private boolean captured = false;
    private boolean hasMoved = false;

    public Koenig(Color color){
        this.COLOR = color;
    }
    public Koenig(Color color, Position position){
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
        return false;
    }

    @Override
    public void setPinned(boolean pinned) {return;}

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
        return new KoenigMoveset(COLOR, this.position);
    }

    public boolean hasMoved() {
        return hasMoved;
    }

    public void setMoved(){
        this.hasMoved = true;
    }
}
