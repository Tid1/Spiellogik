package Model.Spiellogik.Figuren;

import Model.Spiellogik.Color;
import Model.Spiellogik.MoveSets.iMoveSet;

public class Dame implements iPiece{
    private final Color COLOR;
    private final Typ TYP = Typ.DAME;
    private Position position;
    private boolean captured = false;

    public Dame(Color color){
        this.COLOR = color;
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
    public boolean isCaptured() {
        return captured;
    }

    @Override
    public void setCaputred(boolean caputred) {
        this.captured = captured;
    }

    @Override
    public iMoveSet getMoveset() {
        return null;
    }
}
