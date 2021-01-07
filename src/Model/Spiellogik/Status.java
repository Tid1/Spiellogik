package Model.Spiellogik;

public enum Status {
    /**
     * Liste an Zuständen, die in dem Spiel vorkommen können
     */
    START, // 1
    ONE_PICKED, // 1.1
    INIT_FIELD, //1.3
    TURN_WHITE, // 2.1
    TURN_BLACK, //2.2
    STANDBY, // 3
    WHITE_CHECKED, //4.1
    BLACK_CHECKED, //4.2
    END, // 5
    STALEMATE
}
