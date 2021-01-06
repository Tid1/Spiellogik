import Model.Exceptions.GameException;
import Model.Exceptions.StatusException;
import Model.Spiellogik.Color;
import Model.Spiellogik.Figuren.Position;
import Model.Spiellogik.Figuren.Typ;
import Model.Spiellogik.Figuren.iPiece;
import Model.Spiellogik.Status;
import Model.Spiellogik.iBoard;
import Model.Spiellogik.iPlayer;
import Netzwerk.BoardProtocolEngine;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

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
    void  wrongTurnToMoveTest() throws GameException, StatusException {
        Status status = Status.TURN_WHITE;
        iBoard board = null;
        iPiece blackPiece = null;

        blackPiece.setPosition(4,4);
        board.move(blackPiece, 5, 5);

        Position expected = new Position(4, 4);
        assertEquals(blackPiece.getPosition().getX(), expected.getX());
        assertEquals(blackPiece.getPosition().getY(), expected.getY());
    }

    @Test
    void checkPieceOnPosition(){
        iBoard board = null;
        iPiece piece = null;

        piece.setPosition(4,4);
        iPiece expected = board.onField(4,4);

        assertEquals(expected, piece);
    }

    @Test
    void surrenderSuccessful(){
        Status status = Status.TURN_WHITE;
        iPlayer playerBlack = null;

        boolean expected = playerBlack.surrender();
        assertTrue(expected);
    }

    @Test
    void turmMoveSuccessX() throws GameException, StatusException {
        iBoard board = null;
        iPiece turm = null;

        turm.setPosition(3,3);
        board.move(turm, 6, 3);

        Position expected = new Position(6, 3);

        assertEquals(turm.getPosition().getX(), expected.getX());
        assertEquals(turm.getPosition().getY(), expected.getY());
    }

    @Test
    void turmMoveUnsuccessfulX() throws GameException, StatusException {
        iBoard board = null;
        iPiece turm = null;
        iPiece ranPiece = null;

        turm.setPosition(3,3);
        ranPiece.setPosition(5, 3);

        board.move(turm, 6, 3);

        Position expected = new Position(3, 3);

        assertEquals(turm.getPosition().getX(), expected.getX());
        assertEquals(turm.getPosition().getY(), expected.getY());
    }

    @Test
    void turmMoveSuccessY() throws GameException, StatusException {
        iBoard board = null;
        iPiece turm = null;

        turm.setPosition(3,3);

        board.move(turm, 3, 6);

        Position expected = new Position(3, 6);

        assertEquals(turm.getPosition().getX(), expected.getX());
        assertEquals(turm.getPosition().getY(), expected.getY());
    }

    @Test
    void turmMoveUnsuccessfulY() throws GameException, StatusException {
        iBoard board = null;
        iPiece turm = null;
        iPiece ranPiece = null;

        turm.setPosition(3,3);
        ranPiece.setPosition(3, 5);

        board.move(turm, 3, 6);

        Position expected = new Position(3, 3);

        assertEquals(turm.getPosition().getX(), expected.getX());
        assertEquals(turm.getPosition().getY(), expected.getY());
    }

    @Test
    void protocolMachineMoveSuccess() throws GameException, StatusException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        iBoard boardSender = null;
        iPiece piece = null;
        piece.setPosition(4, 4);
        BoardProtocolEngine engine = new BoardProtocolEngine(boardSender, baos, null);

        engine.move(piece,5, 4);
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());

        BoardEngineTester boardMockReceiver = new BoardEngineTester();
        BoardProtocolEngine engineMock = new BoardProtocolEngine(boardMockReceiver,null, bais);

        assertTrue(boardMockReceiver.lastCallMove);
        assertEquals(piece.getPosition().getX(), boardMockReceiver.x);
        assertEquals(piece.getPosition().getY(), boardMockReceiver.y);
        assertEquals(piece, boardMockReceiver.piece);

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
        public void initializeField() throws StatusException {

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
        public boolean checkStalemate(iPlayer player) {
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
