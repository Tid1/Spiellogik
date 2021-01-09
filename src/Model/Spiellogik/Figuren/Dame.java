package Model.Spiellogik.Figuren;

import Model.Spiellogik.Color;
import Model.Spiellogik.MoveSets.BauerMoveset;
import Model.Spiellogik.MoveSets.DameMoveset;
import Model.Spiellogik.MoveSets.iMoveSet;

public class Dame implements iPiece{
    public static final long serialVersionUID = 45L;
    private final Color COLOR;
    private final Typ TYP = Typ.DAME;
    private Position position;
    private boolean captured = false;

    public Dame(Color color){
        this.COLOR = color;
    }
    public Dame(Color color, Position position){
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
    public boolean isCaptured() {
        return captured;
    }

    @Override
    public void setCaputred(boolean caputred) {
        this.captured = captured;
    }

    @Override
    public iMoveSet getMoveset() {
        return new DameMoveset(COLOR, this.position);
    }
}
