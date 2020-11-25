package Model.Spiellogik.Figuren;

public interface iPosition {
    /**
     * Gibt den x-Wert einer Position zurück
     * @return Der x-Wert einer Position
     */
    int getX();

    /**
     * Gibt den y-Wert eine Position zurück
     * @return Der y-Wert einer Position
     */
    int getY();

    /**
     * Gibt eine Position(x- und y-Wert einer der Position) zurück
     * @return Die Position
     */
    iPosition getPosition();
}
