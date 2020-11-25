package Model.Spiellogik;

public class PlayerImpl implements iPlayer {
    private final Color COLOR;
    private final String NAME;

    public PlayerImpl(Color color, String name){
        this.COLOR = color;
        this.NAME = name;
    }


    @Override
    public Color getColor() {
        return COLOR;
    }

    @Override
    public String getName() {
        return NAME;
    }
}
