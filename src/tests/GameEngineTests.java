import Model.Exceptions.GameException;
import Model.Exceptions.StatusException;
import Model.Spiellogik.Color;
import Model.Spiellogik.Figuren.Position;
import Model.Spiellogik.Figuren.Typ;
import Model.Spiellogik.Figuren.iPiece;
import Model.Spiellogik.iBoard;
import Model.Spiellogik.iPlayer;
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
        iPiece figur = null;

        bauerWhite.setPosition(4, 4);
        figur.setPosition(5,4);

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

    @Test
    void bauerGeschlagenFehler() throws GameException, StatusException {
        iBoard board = null;
        iPiece bauerWhiteOne = null;
        iPiece bauerWhiteTwo = null;

        bauerWhiteOne.setPosition(4,4);
        bauerWhiteTwo.setPosition(5,5);

        board.move(bauerWhiteOne, 5,5);

        Position expectedPosition = new Position(4,4);

        assertEquals(bauerWhiteOne.getPosition().getX(), expectedPosition.getX());
        assertEquals(bauerWhiteOne.getPosition().getY(), expectedPosition.getY());
        assertFalse(bauerWhiteTwo.isCaptured());
    }

    @Test
    void bauerZuKoenigen() throws GameException, StatusException {
        iBoard board = null;
        iPiece bauerWhite = null;

        bauerWhite.setPosition(7,3);
        board.move(bauerWhite, 8,3);

        Position expectedPosition = new Position(8,3);
        Typ typ = Typ.KOENIG;

        assertEquals(bauerWhite.getPosition().getX(), expectedPosition.getX());
        assertEquals(bauerWhite.getPosition().getY(), expectedPosition.getY());
        assertEquals(bauerWhite.getType(), typ);
    }

    @Test
    void bauerDoubleMoveFehler() throws GameException, StatusException {
        iBoard board = null;
        iPiece bauer = null;

        bauer.setPosition(4, 5);
        board.move(bauer, 6, 5);

        Position expectedPosition = new Position(4, 5);

        assertEquals(bauer.getPosition().getX(), expectedPosition.getX());
        assertEquals(bauer.getPosition().getY(), expectedPosition.getY());
    }

    @Test
    void bauerMoveFehler() throws GameException, StatusException {
        iBoard board = null;
        iPiece bauer = null;

        bauer.setPosition(4, 5);

        assertThrows(GameException.class, () ->{
           board.move(bauer, 5, 7);
        });
    }

    @Test
    void bauerMoveBackwards(){
        iBoard board = null;
        iPiece bauer = null;

        bauer.setPosition(4, 5);

        assertThrows(GameException.class,() ->{
            board.move(bauer,3, 5);
        });
    }

    @Test
    void pieceOutOfBounds() throws GameException, StatusException {
        iBoard board = null;
        iPiece piece = null;

        piece.setPosition(7, 5);

        assertThrows(GameException.class, () ->{
            board.move(piece, 8, 5);
        });

      /*  Position expectedPosition = new Position(7, 5);
        assertEquals(piece.getPosition().getX(), expectedPosition.getX());
        assertEquals(piece.getPosition().getY(), expectedPosition.getY());*/

    }

    private class BoardEngineTester implements iBoard{
        private boolean lastCallMove = false;
        private boolean lastCallPickColor = false;

        private iPiece piece = null;
        private int x = 0;
        private int y = 0;

        private String name;
        private Color color;

        @Override
        public void initializeField(iPlayer player1, iPlayer player2) throws StatusException {
            //Kann leer bleiben
        }

        @Override
        public iPiece onField(int x, int y) {
            //Kann leer bleiben
            return null;
        }

        @Override
        public void move(iPiece piece, int x, int y) throws GameException, StatusException {
            this.lastCallMove = true;
            this.lastCallPickColor = false;
            this.piece = piece;
            this.x = x;
            this.y = y;

        }

        @Override
        public boolean checkForCheck(iPlayer player) {
            //Kann leer bleiben
            return false;
        }

        @Override
        public boolean checkMate(iPlayer player) {
            //Kann leer bleiben
            return false;
        }

        @Override
        public boolean checkStalemate() {
            //Kann leer bleiben
            return false;
        }

        @Override
        public boolean checkValidMove(iPiece piece, int x, int y) throws GameException {
            //Kann leer bleiben
            return false;
        }

        @Override
        public boolean pickColor(String playerName, Color color) throws StatusException, GameException {
            this.lastCallPickColor = true;
            this.lastCallMove = false;
            this.name = playerName;
            this.color = color;
            //Dummy return
            return false;
        }

    }

}
