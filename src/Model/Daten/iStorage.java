package Model.Daten;

import Model.Spiellogik.iBoard;

public interface iStorage {

    //TODO zur Persistierung Shared preferences benutzen; für objecte GSON files
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
     */
    void list();
}
