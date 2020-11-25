package Model.Spiellogik.Figuren;

public class Position implements iPosition {
    private int x;
    private int y;

    public Position(int x, int y){
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public iPosition getPosition(){
        return new Position(x, y);
    }
}
