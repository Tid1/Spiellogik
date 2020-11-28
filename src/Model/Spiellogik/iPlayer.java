package Model.Spiellogik;

public interface iPlayer {
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
}
