package Model.Spiellogik.MoveSets;

import Model.Spiellogik.Figuren.Position;
import Model.Spiellogik.Figuren.iPiece;

import java.util.List;

public interface iMoveSet {
    /**
     * Gibt an auf welche Position sich eine gegebene Figur von ihrer jetzigen Position bewegen darf
     * @param piece Figur, dessen Aktionen überprüft werden sollen
     * @return Ein Array aller Positionen auf denen sich die Figur befinden könnte
     */
    List<Position> moveSet(Position position);

    //TODO macht das Sinn eine Figur mitzugeben? Kann das zu circular dependency führen?
    //TODO maybe geb ich nichts mit weil ich jeder Figur ein individuelles Moveset gebe und bei speziellen Sachen kann ich dann den Figurtypen ansprechen
    //TODO Für Bauer etc first move variable hier in
    //Dieses Turm König Ding noch hinzufügen
    //Bauer zur Dame machen
    /*Generell einfach Moveset-Klassen entwerfen die für die Figuren individuell sind
      Damit man dann später der Figur ein Attribut Moveset geben kann*/
}
