import Model.Exceptions.GameException;
import Model.Exceptions.StatusException;
import Model.Spiellogik.Figuren.Position;
import Model.Spiellogik.Figuren.iPiece;
import Model.Spiellogik.iBoard;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GameEngineTests {

    @Test
    void bauerStartSingleMove() throws GameException, StatusException {
        iBoard board = null;
        iPiece bauer = null;

        bauer.setPosition(2,2);
        board.move(bauer, 3, 2);

        Position expectedPosition = new Position(3, 2);

        assertEquals(bauer.getPosition().getX(), expectedPosition.getX());
        assertEquals(bauer.getPosition().getY(), expectedPosition.getY());

    }

    @Test
    void bauerSingleMoveNotStart() throws GameException, StatusException {
        iBoard board = null;
        iPiece bauer = null;

        bauer.setPosition(4,5);
        board.move(bauer, 5, 5);

        Position expectedPosition = new Position(5,5);

        assertEquals(bauer.getPosition().getX(), expectedPosition.getX());
        assertEquals(bauer.getPosition().getY(), expectedPosition.getY());
    }

    @Test
    void bauerDoubleMoveStart() throws GameException, StatusException {
        iBoard board = null;
        iPiece bauer = null;

        bauer.setPosition(2,2);
        board.move(bauer, 4, 2);

        Position expectedPosition = new Position(4,2);

        assertEquals(bauer.getPosition().getX(), expectedPosition.getX());
        assertEquals(bauer.getPosition().getY(), expectedPosition.getY());
    }

    @Test
    void bauerBlocked() throws GameException, StatusException {
        iBoard board = null;
        iPiece bauerWhite = null;
        iPiece bauerBlack = null;

        bauerWhite.setPosition(4, 4);
        bauerBlack.setPosition(5,4);

        board.move(bauerWhite, 5, 4);

        Position expectedPosition = new Position(4, 4);

        assertEquals(bauerWhite.getPosition().getX(), expectedPosition.getX());
        assertEquals(bauerWhite.getPosition().getY(), expectedPosition.getY());
    }

    @Test
    void bauerGeschlagenGut() throws GameException, StatusException {
        iBoard board = null;
        iPiece bauerWhite = null;
        iPiece bauerBlack = null;

        bauerWhite.setPosition(4, 4);
        bauerBlack.setPosition(5, 5);

        board.move(bauerWhite, 5, 5);

        Position expectedPosition = new Position(5, 5);

        assertEquals(bauerWhite.getPosition().getX(), expectedPosition.getX());
        assertEquals(bauerWhite.getPosition().getY(), expectedPosition.getY());
        assertTrue(bauerBlack.isCaptured());

    }

}
