package Model.Spiellogik;

import java.io.Serializable;

public interface iPlayer extends Serializable {
    /**
     * Farbe die einem Spieler zugewiesen wird
     * @return Die Farbe des Spielers
     */
    Color getColor();

    /**
     * Name des Spielers
     * @return Namen des Spielers
     */
    String getName();

    boolean surrender();
    boolean checked();
    void setChecked(boolean checked);
}
