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

    /**
     * Gibt an, ob sich der gegebene Spieler im Schach
     * befindent.
     * @return true, falls er sich im Schachbefindet, false, wenn nicht
     */
    boolean checked();

    /**
     * Ändert den Zustand des Spielers
     * @param checked boolean welcher den Zustand des Spielers aussagt.
     *                true, wenn sich der Spieler im Schach befindet,
     *                false wenn nicht
     */
    void setChecked(boolean checked);
}
