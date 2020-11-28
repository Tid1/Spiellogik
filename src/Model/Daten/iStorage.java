package Model.Daten;

import Model.Spiellogik.iBoard;

import java.util.List;

public interface iStorage {
    /**
     * Fügt den Zustand eines Spielfelds zu einem Speicher hinzu
     * @param board Das Board, dessen Zustand gespeichert werden soll
     */
    void add(iBoard board);

    /**
     * Löscht ein Board aus dem Speicher
     * @param board Das zu löschende Board
     */
    void delete(iBoard board);

    /**
     * Listet alle Boards im Speicher auf
     * @return Liste an Boards
     */
    List<iBoard> list();
}
