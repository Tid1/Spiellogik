import Model.Exceptions.GameException;
import Model.Exceptions.StatusException;
import Model.Spiellogik.*;
import Model.Spiellogik.Figuren.*;
import Netzwerk.BoardProtocolEngine;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class GameEngineTests {
    private final String ALICE = "Alice";
    private final String BOB = "Bob";
    private final String CHARLIE = "Charlie";

    @Test
    void pickColorTestSuccess() throws StatusException, GameException {
        BoardImpl board = new BoardImpl();
        boolean aliceInMap = false;
        boolean bobInMap = false;
        board.pickColor(ALICE, Color.Black);
        board.pickColor(BOB, Color.White);
        int expectedSize = 2;

        assertEquals(board.getMap().size(), expectedSize);
        List<iPlayer> players = new ArrayList<>(board.getMap().keySet());
        assertEquals(players.size(), expectedSize);

        iPlayer player1 = players.get(0);
        iPlayer player2 = players.get(1);

        assertNotEquals(player1.getColor(), player2.getColor());

        for (iPlayer player : players){
            if (player.getName().equals(ALICE)){
                aliceInMap = true;
            } else if (player.getName().equals(BOB)){
                bobInMap = true;
            }
        }
        assertTrue(aliceInMap);
        assertTrue(bobInMap);
    }

    @Test
    void pickColorTestThreePeople() throws StatusException {
        BoardImpl board = new BoardImpl();
        board.pickColor(ALICE, Color.Black);
        board.pickColor(BOB, Color.White);

        assertThrows(StatusException.class, () -> {
            board.pickColor(CHARLIE, Color.White);
        });
    }

    @Test
    void bauerStartSingleMove() throws GameException, StatusException {
        BoardImpl board = new BoardImpl();
        Bauer bauer = new Bauer(Color.White);

        board.pickColor(ALICE, Color.Black);
        board.pickColor(BOB, Color.White);

        board.initializeField();
        Map<iPlayer, List<iPiece>> map = board.getMap();

        for (Map.Entry<iPlayer, List<iPiece>> entry : map.entrySet()){
            entry.getValue().clear();
            if (entry.getKey().getColor() == Color.White){
                entry.getValue().add(bauer);
            }
        }
        bauer.setPosition(2,2);
        board.move(bauer, 2, 3);

        Position expectedPosition = new Position(2, 3);

        assertEquals(bauer.getPosition().getX(), expectedPosition.getX());
        assertEquals(bauer.getPosition().getY(), expectedPosition.getY());

    }

    @Test
    void bauerSingleMoveNotStart() throws GameException, StatusException {
        BoardImpl board = new BoardImpl();
        Bauer bauer = new Bauer(Color.White);

        board.pickColor(ALICE, Color.Black);
        board.pickColor(BOB, Color.White);

        board.initializeField();
        Map<iPlayer, List<iPiece>> map = board.getMap();

        for (Map.Entry<iPlayer, List<iPiece>> entry : map.entrySet()){
            if (entry.getKey().getColor() == Color.White){
                entry.getValue().add(bauer);
            }
        }

        bauer.setPosition(4,5);
        board.move(bauer, 4, 6);

        Position expectedPosition = new Position(4,6);

        assertEquals(bauer.getPosition().getX(), expectedPosition.getX());
        assertEquals(bauer.getPosition().getY(), expectedPosition.getY());
    }

    @Test
    void bauerDoubleMoveStart() throws GameException, StatusException {
        BoardImpl board = new BoardImpl();
        Bauer bauer = new Bauer(Color.White);

        board.pickColor(ALICE, Color.Black);
        board.pickColor(BOB, Color.White);

        board.initializeField();
        Map<iPlayer, List<iPiece>> map = board.getMap();

        for (Map.Entry<iPlayer, List<iPiece>> entry : map.entrySet()){
            if (entry.getKey().getColor() == Color.White){
                entry.getValue().add(bauer);
            }
        }
        bauer.setPosition(2,2);
        board.move(bauer, 2, 4);

        Position expectedPosition = new Position(2,4);

        assertEquals(bauer.getPosition().getX(), expectedPosition.getX());
        assertEquals(bauer.getPosition().getY(), expectedPosition.getY());
    }

    @Test
    void bauerBlocked() throws GameException, StatusException {
        BoardImpl board = new BoardImpl();
        Bauer bauer = new Bauer(Color.White);
        iPiece figur = new Laeufer(Color.White);

        board.pickColor(ALICE, Color.Black);
        board.pickColor(BOB, Color.White);

        board.initializeField();
        Map<iPlayer, List<iPiece>> map = board.getMap();

        for (Map.Entry<iPlayer, List<iPiece>> entry : map.entrySet()){
            if (entry.getKey().getColor() == Color.White){
                entry.getValue().add(bauer);
                entry.getValue().add(figur);
            }
        }

        bauer.setPosition(4, 4);
        figur.setPosition(5,4);

        assertThrows(GameException.class, () -> {
            board.move(bauer, 5, 4);
        });

        Position expectedPosition = new Position(4, 4);

        assertEquals(bauer.getPosition().getX(), expectedPosition.getX());
        assertEquals(bauer.getPosition().getY(), expectedPosition.getY());
    }

    @Test
    void bauerGeschlagenGut() throws GameException, StatusException {
        BoardImpl board = new BoardImpl();

        iPiece bauerWhite = new Bauer(Color.White);
        iPiece bauerBlack = new Bauer(Color.Black);

        board.pickColor(ALICE, Color.Black);
        board.pickColor(BOB, Color.White);

        board.initializeField();
        Map<iPlayer, List<iPiece>> map = board.getMap();

        for (Map.Entry<iPlayer, List<iPiece>> entry : map.entrySet()){
            if (entry.getKey().getColor() == Color.White){
                entry.getValue().add(bauerWhite);
            } else {
                entry.getValue().add(bauerBlack);
            }
        }


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
        BoardImpl board = new BoardImpl();

        iPiece bauerWhiteOne = new Bauer(Color.White);
        iPiece bauerWhiteTwo = new Bauer(Color.White);

        board.pickColor(ALICE, Color.Black);
        board.pickColor(BOB, Color.White);

        board.initializeField();
        Map<iPlayer, List<iPiece>> map = board.getMap();

        for (Map.Entry<iPlayer, List<iPiece>> entry : map.entrySet()){
            if (entry.getKey().getColor() == Color.White){
                entry.getValue().add(bauerWhiteOne);
                entry.getValue().add(bauerWhiteTwo);
            }
        }

        bauerWhiteOne.setPosition(4, 4);
        bauerWhiteTwo.setPosition(5, 5);

        assertThrows(GameException.class, () -> {
            board.move(bauerWhiteOne, 5, 5);
        });


    }

    @Test
    void bauerZuKoenigin() throws GameException, StatusException {
        BoardImpl board = new BoardImpl();

        iPiece bauer = new Bauer(Color.White);

        board.pickColor(ALICE, Color.Black);
        board.pickColor(BOB, Color.White);

        board.initializeField();
        Map<iPlayer, List<iPiece>> map = board.getMap();

        for (Map.Entry<iPlayer, List<iPiece>> entry : map.entrySet()){
            entry.getValue().clear();
            if (entry.getKey().getColor() == Color.White){
                entry.getValue().add(bauer);
            }
        }

        bauer.setPosition(3,7);
        board.move(bauer, 3,8);

        Typ typ = Typ.DAME;

        assertEquals(typ, board.onField(3,8).getType());
    }

    @Test
    void bauerDoubleMoveFehler() throws GameException, StatusException {
        BoardImpl board = new BoardImpl();
        Bauer bauer = new Bauer(Color.White);

        board.pickColor(ALICE, Color.Black);
        board.pickColor(BOB, Color.White);

        board.initializeField();
        Map<iPlayer, List<iPiece>> map = board.getMap();

        for (Map.Entry<iPlayer, List<iPiece>> entry : map.entrySet()){
            entry.getValue().clear();
            if (entry.getKey().getColor() == Color.White){
                entry.getValue().add(bauer);
            }
        }
        bauer.setPosition(4, 5);
        assertThrows(GameException.class, () -> {
            board.move(bauer, 4, 7);
        });

        Position expectedPosition = new Position(4, 5);

        assertEquals(bauer.getPosition().getX(), expectedPosition.getX());
        assertEquals(bauer.getPosition().getY(), expectedPosition.getY());
    }

    @Test
    void bauerMoveFehler() throws GameException, StatusException {
        BoardImpl board = new BoardImpl();
        Bauer bauer = new Bauer(Color.White);

        board.pickColor(ALICE, Color.Black);
        board.pickColor(BOB, Color.White);

        board.initializeField();
        Map<iPlayer, List<iPiece>> map = board.getMap();

        for (Map.Entry<iPlayer, List<iPiece>> entry : map.entrySet()){
            entry.getValue().clear();
            if (entry.getKey().getColor() == Color.White){
                entry.getValue().add(bauer);
            }
        }

        bauer.setPosition(4, 5);

        assertThrows(GameException.class, () ->{
           board.move(bauer, 5, 7);
        });
    }

    @Test
    void bauerMoveBackwards() throws StatusException {
        BoardImpl board = new BoardImpl();
        Bauer bauer = new Bauer(Color.White);

        board.pickColor(ALICE, Color.Black);
        board.pickColor(BOB, Color.White);

        board.initializeField();
        Map<iPlayer, List<iPiece>> map = board.getMap();

        for (Map.Entry<iPlayer, List<iPiece>> entry : map.entrySet()){
            entry.getValue().clear();
            if (entry.getKey().getColor() == Color.White){
                entry.getValue().add(bauer);
            }
        }

        bauer.setPosition(4, 5);

        assertThrows(GameException.class,() ->{
            board.move(bauer,3, 5);
        });
    }

    @Test
    void  wrongTurnToMoveTest() throws GameException, StatusException {
        BoardImpl board = new BoardImpl();
        Bauer bauer = new Bauer(Color.Black);

        board.pickColor(ALICE, Color.Black);
        board.pickColor(BOB, Color.White);

        board.initializeField();
        Map<iPlayer, List<iPiece>> map = board.getMap();

        for (Map.Entry<iPlayer, List<iPiece>> entry : map.entrySet()){
            entry.getValue().clear();
            if (entry.getKey().getColor() == Color.Black){
                entry.getValue().add(bauer);
            }
        }
        bauer.setPosition(4,4);
        assertThrows(StatusException.class, () -> {
            board.move(bauer, 4, 5);
        });
    }

    @Test
    void checkPieceOnPosition() throws StatusException {
        BoardImpl board = new BoardImpl();
        Bauer bauer = new Bauer(Color.Black);

        board.pickColor(ALICE, Color.Black);
        board.pickColor(BOB, Color.White);

        board.initializeField();
        Map<iPlayer, List<iPiece>> map = board.getMap();

        for (Map.Entry<iPlayer, List<iPiece>> entry : map.entrySet()){
            entry.getValue().clear();
            if (entry.getKey().getColor() == Color.Black){
                entry.getValue().add(bauer);
            }
        }

        bauer.setPosition(4,4);
        iPiece expected = board.onField(4,4);
        assertEquals(expected, bauer);
    }

    @Test
    void turmMoveSuccessX() throws GameException, StatusException {
        BoardImpl board = new BoardImpl();
        Turm turm = new Turm(Color.White);

        board.pickColor(ALICE, Color.Black);
        board.pickColor(BOB, Color.White);

        board.initializeField();
        Map<iPlayer, List<iPiece>> map = board.getMap();

        for (Map.Entry<iPlayer, List<iPiece>> entry : map.entrySet()){
            entry.getValue().clear();
            if (entry.getKey().getColor() == Color.White){
                entry.getValue().add(turm);
            }
        }

        turm.setPosition(3,3);
        board.move(turm, 6, 3);

        Position expected = new Position(6, 3);

        assertEquals(turm.getPosition().getX(), expected.getX());
        assertEquals(turm.getPosition().getY(), expected.getY());
    }

    @Test
    void turmMoveUnsuccessfulX() throws GameException, StatusException {
        BoardImpl board = new BoardImpl();
        Bauer ranPiece = new Bauer(Color.White);
        Turm turm = new Turm(Color.White);

        board.pickColor(ALICE, Color.Black);
        board.pickColor(BOB, Color.White);

        board.initializeField();
        Map<iPlayer, List<iPiece>> map = board.getMap();

        for (Map.Entry<iPlayer, List<iPiece>> entry : map.entrySet()) {
            entry.getValue().clear();
            if (entry.getKey().getColor() == Color.White) {
                entry.getValue().add(ranPiece);
                entry.getValue().add(turm);
            }
        }

        turm.setPosition(3,3);
        ranPiece.setPosition(5, 3);

        assertThrows(GameException.class, () -> {
            board.move(turm, 6, 3);
        });

        Position expected = new Position(3, 3);

        assertEquals(turm.getPosition().getX(), expected.getX());
        assertEquals(turm.getPosition().getY(), expected.getY());
    }

    @Test
    void turmMoveSuccessY() throws GameException, StatusException {
        BoardImpl board = new BoardImpl();
        Turm turm = new Turm(Color.White);

        board.pickColor(ALICE, Color.Black);
        board.pickColor(BOB, Color.White);

        board.initializeField();
        Map<iPlayer, List<iPiece>> map = board.getMap();

        for (Map.Entry<iPlayer, List<iPiece>> entry : map.entrySet()){
            entry.getValue().clear();
            if (entry.getKey().getColor() == Color.White){
                entry.getValue().add(turm);
            }
        }
        turm.setPosition(3,3);

        board.move(turm, 3, 6);

        Position expected = new Position(3, 6);

        assertEquals(turm.getPosition().getX(), expected.getX());
        assertEquals(turm.getPosition().getY(), expected.getY());
    }

    @Test
    void turmMoveUnsuccessfulY() throws GameException, StatusException {
        BoardImpl board = new BoardImpl();
        Turm turm = new Turm(Color.White);
        Bauer ranPiece = new Bauer(Color.White);

        board.pickColor(ALICE, Color.Black);
        board.pickColor(BOB, Color.White);

        board.initializeField();
        Map<iPlayer, List<iPiece>> map = board.getMap();

        for (Map.Entry<iPlayer, List<iPiece>> entry : map.entrySet()){
            entry.getValue().clear();
            if (entry.getKey().getColor() == Color.White){
                entry.getValue().add(turm);
                entry.getValue().add(ranPiece);
            }
        }

        turm.setPosition(3,3);
        ranPiece.setPosition(3, 5);

        assertThrows(GameException.class, () -> {
            board.move(turm, 3, 6);
        });

        Position expected = new Position(3, 3);

        assertEquals(turm.getPosition().getX(), expected.getX());
        assertEquals(turm.getPosition().getY(), expected.getY());
    }

    @Test
    void testTurmBlockedVonBauerStart() throws StatusException {
        BoardImpl board = new BoardImpl();
        Turm turm = new Turm(Color.White);
        //Bauer ranPiece = new Bauer(Color.White);

        board.pickColor(ALICE, Color.Black);
        board.pickColor(BOB, Color.White);

        board.initializeField();
        Map<iPlayer, List<iPiece>> map = board.getMap();

        for (Map.Entry<iPlayer, List<iPiece>> entry : map.entrySet()){
            if (entry.getKey().getColor() == Color.White){
                entry.getValue().add(turm);
               // entry.getValue().add(ranPiece);
            }
        }

        turm.setPosition(3,3);
       // ranPiece.setPosition(2, 3);

        assertThrows(GameException.class, () -> {
            board.move(turm, 3, 1);
        });

        Position expected = new Position(3, 3);

        assertEquals(turm.getPosition().getX(), expected.getX());
        assertEquals(turm.getPosition().getY(), expected.getY());
    }

    @Test
    void testInitializeField() throws StatusException {
        BoardImpl board = new BoardImpl();

        board.pickColor(ALICE, Color.Black);
        board.pickColor(BOB, Color.White);

        board.initializeField();
        Map<iPlayer, List<iPiece>> map = board.getMap();

        Typ typ = Typ.BAUER;

        for (int i = 1; i <= 8; i++){
            assertNotNull(board.onField(i, 2));
            assertNotNull(board.onField(i, 7));
            assertNotNull(board.onField(i, 1));
            assertNotNull(board.onField(i, 8));
            assertEquals(typ, board.onField(i, 2).getType());
            assertEquals(typ, board.onField(i, 7).getType());
            assertEquals(Color.White, board.onField(i, 1).getColor());
            assertEquals(Color.White, board.onField(i, 2).getColor());
            assertEquals(Color.Black, board.onField(i, 7).getColor());
            assertEquals(Color.Black, board.onField(i, 8).getColor());

            if (i == 1 || i == 8){
                assertEquals(Typ.TURM, board.onField(i, 1).getType());
                assertEquals(Typ.TURM, board.onField(i, 8).getType());
            } else if (i == 2 || i == 7){
                assertEquals(Typ.LAEUFER, board.onField(i, 1).getType());
                assertEquals(Typ.LAEUFER, board.onField(i, 8).getType());
            } else if (i == 3 || i == 6){
                assertEquals(Typ.SPRINGER, board.onField(i, 1).getType());
                assertEquals(Typ.SPRINGER, board.onField(i, 8).getType());
            } else if (i == 4){
                assertEquals(Typ.DAME, board.onField(i, 1).getType());
                assertEquals(Typ.DAME, board.onField(i, 8).getType());
            } else if (i == 5){
                assertEquals(Typ.KOENIG, board.onField(i, 1).getType());
                assertEquals(Typ.KOENIG, board.onField(i, 8).getType());
            }

        }

    }

    @Test
    void protocolMachineMoveSuccess() throws GameException, StatusException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BoardImpl boardSender = new BoardImpl();
        iPiece piece = new Bauer(Color.White);

        piece.setPosition(4, 4);
        BoardProtocolEngine engine = new BoardProtocolEngine(boardSender, baos, null);

        engine.pickColor(ALICE, Color.Black);
        engine.pickColor(BOB, Color.White);

        int xCoordinate = 4;
        int yCooridinate = 5;

        boardSender.initializeField();
        Map<iPlayer, List<iPiece>> map = boardSender.getMap();

        for (Map.Entry<iPlayer, List<iPiece>> entry : map.entrySet()){
            entry.getValue().clear();
            if (entry.getKey().getColor() == Color.White){
                entry.getValue().add(piece);
            }
        }
        BoardEngineTester boardMockReceiver = new BoardEngineTester(piece);
        assertEquals(piece.getPosition().getX(), boardMockReceiver.piece.getPosition().getX());
        assertEquals(piece.getPosition().getY(), boardMockReceiver.piece.getPosition().getY());

        engine.move(piece,xCoordinate, yCooridinate);
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());


        BoardProtocolEngine engineMock = new BoardProtocolEngine(boardMockReceiver,null, bais);
        engineMock.deserialize();

        assertTrue(boardMockReceiver.lastCallMove);
        assertEquals(piece.getPosition().getX(), boardMockReceiver.x);
        assertEquals(piece.getPosition().getY(), boardMockReceiver.y);
    }

    @Test
    void pieceOutOfBounds() throws GameException, StatusException {
        BoardImpl board = new BoardImpl();
        Dame dame = new Dame(Color.White);

        board.pickColor(ALICE, Color.Black);
        board.pickColor(BOB, Color.White);

        board.initializeField();
        Map<iPlayer, List<iPiece>> map = board.getMap();

        for (Map.Entry<iPlayer, List<iPiece>> entry : map.entrySet()){
            entry.getValue().clear();
            if (entry.getKey().getColor() == Color.White){
                entry.getValue().add(dame);
            }
        }

        dame.setPosition(7, 5);

        assertThrows(GameException.class, () ->{
            board.move(dame, 9, 5);
        });

        Position expectedPosition = new Position(7, 5);
        assertEquals(dame.getPosition().getX(), expectedPosition.getX());
        assertEquals(dame.getPosition().getY(), expectedPosition.getY());

    }

    private class BoardEngineTester implements iBoard{
        private boolean lastCallMove = false;
        private boolean lastCallPickColor = false;

        private iPiece piece;
        private int x = 0;
        private int y = 0;

        private String name;
        private Color color;

        BoardEngineTester(iPiece piece){
            this.piece = piece;
        }

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
