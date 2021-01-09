package Model.Spiellogik;

import Model.Exceptions.GameException;
import Model.Exceptions.StatusException;
import Model.Spiellogik.Figuren.iPiece;

import java.io.Serializable;

public interface iBoard extends Serializable {
    /**
     * Erzeugt ein 2d Array aus int-Werten
     * @throws StatusException wenn versucht wird die Methode in irgendeinem Zustand der nicht ONE_PICKED ist aufzurufen
     */
    void initializeField() throws StatusException;

    /**
     * Prüft, ob sich eine Figur an der Gegebenen Position befindet
     * @param x x-Wert der Position
     * @param y y-Wert der Position
     * @return Die Figur die sich darauf befindet, null wenn das Feld leer ist
     */
    iPiece onField(int x, int y);

    /**
     * Bewegt eine Figur zu einem Feld
     * @param piece Figur die bewegt werden soll
     * @param x x-Wert der neuen Position
     * @param y y-Wert der neuen Position
     * @throws GameException wenn ein Spieler versucht die Figuren des Gegners zu bewegen
     * @throws StatusException wenn ein Spieler versucht einen Zug zu machen, obwohl dieser nicht am Zug ist
     */
    void move(iPiece piece, int x, int y) throws GameException, StatusException;

    /**
     * überprüft ob sich ein Spieler im Schach befindet
     * @param player Spieler, bei dem die Überprüfung stattfinden soll
     * @return true, wenn sich die Perosn im Schach befindet, false wenn nicht
     */
    boolean checkForCheck(iPlayer player);

    /**
     * Überprüft ob ein Spieler im Schachmatt steht
     * @param player Spieler, bei dem die Überprüfung stattfinden soll
     * @return true, wenn der Spieler Schachmatt ist, false wenn nicht
     */
    boolean checkMate(iPlayer player);

    /**
     * Überprüft ob das Spiel in einem Patt endet
     * @param player Spieler, bei dem die Überprüfung stattfinden soll
     * @return true, wenn das Spiel in einem Patt geendet ist, false wenn nicht
     */
    boolean checkStalemate(iPlayer player);

    /**
     * Überprüft ob die Figur dazu berchtigt ist eine bestimmte Bewegung zu machen
     * @param piece Figur von der die Überprüfung stattfinden soll
     * @param x x-Koordinate der Position auf die sich bewegt werden soll
     * @param y y-Koordinate der Position auf die sich bewegt werden soll
     * @return true, wenn die Figur berechtigt ist diese Bewegung zu machen, false wenn nicht
     * @throws GameException wenn die gewünschte Position außerhalb der Grenzen des Spielfelds ist
     */
    boolean checkValidMove(iPiece piece, int x, int y) throws GameException;

    /**
     * Lässt den Spieler eine Farbe auswählen, Insofern die Farbe noch nicht vergeben wurde
     * @param playerName Name des Spielers der eine Farbe wählen möchte
     * @param color Gewünschte Farbe
     * @return true, wenn die gewünschte Farbe vorhanden ist oder wenn eine Farbe zugeteilt werden konnte, false
     * wenn eine nicht vorhergesehene Farbe gewählt wurde
     * @throws StatusException wenn die Methode im Falschen Zustand aufgerufen wurde
     * @throws GameException wenn sich bereits zwei Spieler im Spiel befinden und eine weitere Person versucht eine Farbe zu wählen
     */
    boolean pickColor(String playerName, Color color) throws StatusException, GameException;

}
