package Model.Spiellogik.Figuren;

import Model.Spiellogik.Color;
import Model.Spiellogik.MoveSets.iMoveSet;

import java.io.Serializable;

public interface iPiece extends Serializable {
    /**
     * Farbe der Figur
     * @return Farbe der Figur
     */
    Color getColor();

    /**
     * Typ der Figur
     * @return Typ der Figur
     */
    Typ getType();

    /**
     * Derzeitige Position der Figur
     * @return Derzeitige Position der Figur
     */
    Position getPosition();

    /**
     * Neue Position der Figur
     * @param x x-Wert der neuen Position
     * @param y y-Wert der neuen Position
     */
    void setPosition(int x, int y);

    /**
     * Gibt zurück ob sich eine Figur bewegen kann.
     * Zählt als pinned wenn sich durch bewegen der Figur
     * der König in Gefahr befindet
     * @return true, wenn die Figur pinned ist, false wenn nicht
     */
    boolean isPinend();

    /**
     * Setzt den Status einer Figur auf pinned.
     * Wenn eine Figur pinned ist, kann sich diese nicht bewegen,
     * da sonst der König im Schach oder Schachmatt wäre
     */
    void setPinned(boolean pinned);

    /**
     * Gibt zurück ob eine Figur geschlagen wurde
     * @return true, wenn sie geschlagen wurde, false wenn nicht
     */
    boolean isCaptured();

    /**
     * Ändert den Zustand der Figur
     * @param caputred Der neue Zustand der Figur. true, wenn
     *                 die Figur geschlagen wurde, false wenn nicht
     */
    void setCaputred(boolean caputred);

    /**
     * Erstellt ein anonymes Moveset mit der Farbe der Figur und der derzeitigen Position
     * @return das anonyme Moveset
     */
    iMoveSet getMoveset();
}
