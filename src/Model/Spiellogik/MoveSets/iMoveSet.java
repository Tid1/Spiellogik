package Model.Spiellogik.MoveSets;

import Model.Spiellogik.Figuren.Position;
import Model.Spiellogik.Figuren.iPiece;
import Model.Spiellogik.iBoard;

import java.util.List;

public interface iMoveSet {
    /**
     * Gibt an auf welche Position sich eine gegebene Figur von ihrer jetzigen Position bewegen darf
     * @param board Aktueller Zustand des Spielfeldes
     * @return Eine Liste aller Positionen auf denen sich die Figur befinden könnte
     */
    List<Position> moveSet(iBoard board);

}