package Model.Spiellogik;

public class PlayerImpl implements iPlayer {
    private final Color COLOR;
    private final String NAME;
    private boolean checked;

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

    @Override
    public boolean surrender() {
        return true;
    }

    @Override
    public boolean checked() {
        return checked;
    }

    @Override
    public void setChecked(boolean checked){
        this.checked = checked;
    }
}
