package Model.Spiellogik;

import Model.Exceptions.GameException;
import Model.Exceptions.StatusException;
import Model.Spiellogik.Figuren.iPiece;

public interface iBoard {
    /**
     * Wahrscheinlich 2d-Array aus int; Maybe auch 1d Array aus Positionen
     */
    void initializeField(iPlayer player1, iPlayer player2) throws StatusException;

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
     * @return true, wenn das Spiel in einem Patt geendet ist, false wenn nicht
     */
    boolean checkStalemate();

    /**
     * Überprüft ob die Figur dazu berchtigt ist eine bestimmte Bewegung zu machen
     * @param piece Figur von der die Überprüfung stattfinden soll
     * @param x x-Koordinate der Position auf die sich bewegt werden soll
     * @param y y-Koordinate der Position auf die sich bewegt werden soll
     * @return true, wenn die Figur berechtigt ist diese Bewegung zu machen, false wenn nicht
     */
    boolean checkValidMove(iPiece piece, int x, int y) throws GameException;

    boolean pickColor(String playerName, Color color) throws StatusException, GameException;

}
