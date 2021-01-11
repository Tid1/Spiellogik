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
    void turmMoveSuccessRight() throws GameException, StatusException {
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
    void turmMoveUnsuccessfulRight() throws GameException, StatusException {
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
    void turmMoveSuccessLeft() throws GameException, StatusException {
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

        turm.setPosition(6,3);
        board.move(turm, 3, 3);

        Position expected = new Position(3, 3);

        assertEquals(turm.getPosition().getX(), expected.getX());
        assertEquals(turm.getPosition().getY(), expected.getY());
    }

    @Test
    void turmMoveUnsuccessfulLeft() throws GameException, StatusException {
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

        turm.setPosition(6,3);
        ranPiece.setPosition(5, 3);

        assertThrows(GameException.class, () -> {
            board.move(turm, 3, 3);
        });

        Position expected = new Position(6, 3);

        assertEquals(turm.getPosition().getX(), expected.getX());
        assertEquals(turm.getPosition().getY(), expected.getY());
    }

    @Test
    void turmMoveSuccessUp() throws GameException, StatusException {
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
    void turmMoveUnsuccessfulUp() throws GameException, StatusException {
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
    void turmMoveSuccessDown() throws GameException, StatusException {
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
        turm.setPosition(3,6);

        board.move(turm, 3, 3);

        Position expected = new Position(3, 3);

        assertEquals(turm.getPosition().getX(), expected.getX());
        assertEquals(turm.getPosition().getY(), expected.getY());
    }

    @Test
    void turmMoveUnsuccessfulDown() throws GameException, StatusException {
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

        turm.setPosition(3,6);
        ranPiece.setPosition(3, 5);

        assertThrows(GameException.class, () -> {
            board.move(turm, 3, 3);
        });

        Position expected = new Position(3, 6);

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
                assertEquals(Typ.SPRINGER, board.onField(i, 1).getType());
                assertEquals(Typ.SPRINGER, board.onField(i, 8).getType());
            } else if (i == 3 || i == 6){
                assertEquals(Typ.LAEUFER, board.onField(i, 1).getType());
                assertEquals(Typ.LAEUFER, board.onField(i, 8).getType());
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
    void pieceOutOfBoundsUpperX() throws GameException, StatusException {
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

    @Test
    void pieceOutOfBoundsUpperY() throws GameException, StatusException {
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
            board.move(dame, 7, 9);
        });

        Position expectedPosition = new Position(7, 5);
        assertEquals(dame.getPosition().getX(), expectedPosition.getX());
        assertEquals(dame.getPosition().getY(), expectedPosition.getY());

    }

    @Test
    void pieceOutOfBoundsLowerX() throws GameException, StatusException {
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
            board.move(dame, -1, 5);
        });

        Position expectedPosition = new Position(7, 5);
        assertEquals(dame.getPosition().getX(), expectedPosition.getX());
        assertEquals(dame.getPosition().getY(), expectedPosition.getY());

    }

    @Test
    void pieceOutOfBoundsLowerY() throws GameException, StatusException {
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
            board.move(dame, 9, -1);
        });

        Position expectedPosition = new Position(7, 5);
        assertEquals(dame.getPosition().getX(), expectedPosition.getX());
        assertEquals(dame.getPosition().getY(), expectedPosition.getY());

    }

    @Test
    void testLaeuferSuccessTopRight() throws StatusException, GameException {
        BoardImpl board = new BoardImpl();
        Laeufer lauefer = new Laeufer(Color.White);

        board.pickColor(ALICE, Color.Black);
        board.pickColor(BOB, Color.White);

        board.initializeField();
        Map<iPlayer, List<iPiece>> map = board.getMap();

        for (Map.Entry<iPlayer, List<iPiece>> entry : map.entrySet()){
            entry.getValue().clear();
            if (entry.getKey().getColor() == Color.White){
                entry.getValue().add(lauefer);
            }
        }

        lauefer.setPosition(2, 2);
        board.move(lauefer, 4, 4);
        Position expectedPosition = new Position(4, 4);

        assertEquals(expectedPosition.getX(), lauefer.getPosition().getX());
        assertEquals(expectedPosition.getY(), lauefer.getPosition().getY());
    }

    @Test
    void testLaeuferUnsuccessfulTopRight() throws StatusException, GameException {
        BoardImpl board = new BoardImpl();
        Laeufer lauefer = new Laeufer(Color.White);
        Bauer ranPiece = new Bauer(Color.White);

        board.pickColor(ALICE, Color.Black);
        board.pickColor(BOB, Color.White);

        board.initializeField();
        Map<iPlayer, List<iPiece>> map = board.getMap();

        for (Map.Entry<iPlayer, List<iPiece>> entry : map.entrySet()){
            entry.getValue().clear();
            if (entry.getKey().getColor() == Color.White){
                entry.getValue().add(lauefer);
                entry.getValue().add(ranPiece);
            }
        }

        lauefer.setPosition(2, 2);
        ranPiece.setPosition(3, 3);

        assertThrows(GameException.class, () -> {
            board.move(lauefer, 4, 4);
        });

        Position expectedPosition = new Position(2, 2);
        assertEquals(expectedPosition.getX(), lauefer.getPosition().getX());
        assertEquals(expectedPosition.getY(), lauefer.getPosition().getY());
    }

    @Test
    void testLaeuferSuccessDownLeft() throws StatusException, GameException {
        BoardImpl board = new BoardImpl();
        Laeufer lauefer = new Laeufer(Color.White);

        board.pickColor(ALICE, Color.Black);
        board.pickColor(BOB, Color.White);

        board.initializeField();
        Map<iPlayer, List<iPiece>> map = board.getMap();

        for (Map.Entry<iPlayer, List<iPiece>> entry : map.entrySet()){
            entry.getValue().clear();
            if (entry.getKey().getColor() == Color.White){
                entry.getValue().add(lauefer);
            }
        }

        lauefer.setPosition(4, 4);
        board.move(lauefer, 2, 2);
        Position expectedPosition = new Position(2, 2);

        assertEquals(expectedPosition.getX(), lauefer.getPosition().getX());
        assertEquals(expectedPosition.getY(), lauefer.getPosition().getY());
    }

    @Test
    void testLaeuferUnsuccessfulDownLeft() throws StatusException, GameException {
        BoardImpl board = new BoardImpl();
        Laeufer lauefer = new Laeufer(Color.White);
        Bauer ranPiece = new Bauer(Color.White);

        board.pickColor(ALICE, Color.Black);
        board.pickColor(BOB, Color.White);

        board.initializeField();
        Map<iPlayer, List<iPiece>> map = board.getMap();

        for (Map.Entry<iPlayer, List<iPiece>> entry : map.entrySet()){
            entry.getValue().clear();
            if (entry.getKey().getColor() == Color.White){
                entry.getValue().add(lauefer);
                entry.getValue().add(ranPiece);
            }
        }

        lauefer.setPosition(4, 4);
        ranPiece.setPosition(3, 3);

        assertThrows(GameException.class, () -> {
            board.move(lauefer, 2, 2);
        });

        Position expectedPosition = new Position(4, 4);
        assertEquals(expectedPosition.getX(), lauefer.getPosition().getX());
        assertEquals(expectedPosition.getY(), lauefer.getPosition().getY());
    }

    @Test
    void testLaeuferSuccessTopLeft() throws StatusException, GameException {
        BoardImpl board = new BoardImpl();
        Laeufer lauefer = new Laeufer(Color.White);

        board.pickColor(ALICE, Color.Black);
        board.pickColor(BOB, Color.White);

        board.initializeField();
        Map<iPlayer, List<iPiece>> map = board.getMap();

        for (Map.Entry<iPlayer, List<iPiece>> entry : map.entrySet()){
            entry.getValue().clear();
            if (entry.getKey().getColor() == Color.White){
                entry.getValue().add(lauefer);
            }
        }

        lauefer.setPosition(4, 2);
        board.move(lauefer, 2, 4);
        Position expectedPosition = new Position(2, 4);

        assertEquals(expectedPosition.getX(), lauefer.getPosition().getX());
        assertEquals(expectedPosition.getY(), lauefer.getPosition().getY());
    }

    @Test
    void testLaeuferUnsuccessfulTopLeft() throws StatusException, GameException {
        BoardImpl board = new BoardImpl();
        Laeufer lauefer = new Laeufer(Color.White);
        Bauer ranPiece = new Bauer(Color.White);

        board.pickColor(ALICE, Color.Black);
        board.pickColor(BOB, Color.White);

        board.initializeField();
        Map<iPlayer, List<iPiece>> map = board.getMap();

        for (Map.Entry<iPlayer, List<iPiece>> entry : map.entrySet()){
            entry.getValue().clear();
            if (entry.getKey().getColor() == Color.White){
                entry.getValue().add(lauefer);
                entry.getValue().add(ranPiece);
            }
        }

        lauefer.setPosition(2, 4);
        ranPiece.setPosition(3, 3);

        assertThrows(GameException.class, () -> {
            board.move(lauefer, 4, 2);
        });

        Position expectedPosition = new Position(2, 4);
        assertEquals(expectedPosition.getX(), lauefer.getPosition().getX());
        assertEquals(expectedPosition.getY(), lauefer.getPosition().getY());
    }

    @Test
    void testLaeuferSuccessDownRight() throws StatusException, GameException {
        BoardImpl board = new BoardImpl();
        Laeufer lauefer = new Laeufer(Color.White);

        board.pickColor(ALICE, Color.Black);
        board.pickColor(BOB, Color.White);

        board.initializeField();
        Map<iPlayer, List<iPiece>> map = board.getMap();

        for (Map.Entry<iPlayer, List<iPiece>> entry : map.entrySet()){
            entry.getValue().clear();
            if (entry.getKey().getColor() == Color.White){
                entry.getValue().add(lauefer);
            }
        }

        lauefer.setPosition(2, 4);
        board.move(lauefer, 4, 2);
        Position expectedPosition = new Position(4, 2);

        assertEquals(expectedPosition.getX(), lauefer.getPosition().getX());
        assertEquals(expectedPosition.getY(), lauefer.getPosition().getY());
    }

    @Test
    void testLaeuferUnsuccessfulDownRight() throws StatusException, GameException {
        BoardImpl board = new BoardImpl();
        Laeufer lauefer = new Laeufer(Color.White);
        Bauer ranPiece = new Bauer(Color.White);

        board.pickColor(ALICE, Color.Black);
        board.pickColor(BOB, Color.White);

        board.initializeField();
        Map<iPlayer, List<iPiece>> map = board.getMap();

        for (Map.Entry<iPlayer, List<iPiece>> entry : map.entrySet()){
            entry.getValue().clear();
            if (entry.getKey().getColor() == Color.White){
                entry.getValue().add(lauefer);
                entry.getValue().add(ranPiece);
            }
        }

        lauefer.setPosition(4, 2);
        ranPiece.setPosition(3, 3);

        assertThrows(GameException.class, () -> {
            board.move(lauefer, 2, 4);
        });

        Position expectedPosition = new Position(4, 2);
        assertEquals(expectedPosition.getX(), lauefer.getPosition().getX());
        assertEquals(expectedPosition.getY(), lauefer.getPosition().getY());
    }

    @Test
    void testLaeuferBlockedByBlackPiece() throws StatusException, GameException {
        BoardImpl board = new BoardImpl();
        Laeufer lauefer = new Laeufer(Color.White);
        Bauer ranPiece = new Bauer(Color.Black);

        board.pickColor(ALICE, Color.Black);
        board.pickColor(BOB, Color.White);

        board.initializeField();
        Map<iPlayer, List<iPiece>> map = board.getMap();

        for (Map.Entry<iPlayer, List<iPiece>> entry : map.entrySet()){
            entry.getValue().clear();
            if (entry.getKey().getColor() == Color.White){
                entry.getValue().add(lauefer);
                entry.getValue().add(ranPiece);
            }
        }

        lauefer.setPosition(4, 2);
        ranPiece.setPosition(3, 3);

        assertThrows(GameException.class, () -> {
            board.move(lauefer, 2, 4);
        });

        Position expectedPosition = new Position(4, 2);
        assertEquals(expectedPosition.getX(), lauefer.getPosition().getX());
        assertEquals(expectedPosition.getY(), lauefer.getPosition().getY());
    }

    @Test
    void testSpringerMoveTopTopLeft() throws StatusException, GameException {
        BoardImpl board = new BoardImpl();
        Springer springer = new Springer(Color.White);

        board.pickColor(ALICE, Color.Black);
        board.pickColor(BOB, Color.White);

        board.initializeField();
        Map<iPlayer, List<iPiece>> map = board.getMap();

        for (Map.Entry<iPlayer, List<iPiece>> entry : map.entrySet()){
            entry.getValue().clear();
            if (entry.getKey().getColor() == Color.White){
                entry.getValue().add(springer);
            }
        }

        springer.setPosition(5, 4);
        board.move(springer,4, 6);

        Position expectedPosition = new Position(4, 6);
        assertEquals(expectedPosition.getX(), springer.getPosition().getX());
        assertEquals(expectedPosition.getY(), springer.getPosition().getY());
    }

    @Test
    void testSpringerMoveTopLeftLeft() throws StatusException, GameException {
        BoardImpl board = new BoardImpl();
        Springer springer = new Springer(Color.White);

        board.pickColor(ALICE, Color.Black);
        board.pickColor(BOB, Color.White);

        board.initializeField();
        Map<iPlayer, List<iPiece>> map = board.getMap();

        for (Map.Entry<iPlayer, List<iPiece>> entry : map.entrySet()){
            entry.getValue().clear();
            if (entry.getKey().getColor() == Color.White){
                entry.getValue().add(springer);
            }
        }

        springer.setPosition(5, 4);
        board.move(springer,3, 5);

        Position expectedPosition = new Position(3, 5);
        assertEquals(expectedPosition.getX(), springer.getPosition().getX());
        assertEquals(expectedPosition.getY(), springer.getPosition().getY());
    }

    @Test
    void testSpringerMoveTopTopRight() throws StatusException, GameException {
        BoardImpl board = new BoardImpl();
        Springer springer = new Springer(Color.White);

        board.pickColor(ALICE, Color.Black);
        board.pickColor(BOB, Color.White);

        board.initializeField();
        Map<iPlayer, List<iPiece>> map = board.getMap();

        for (Map.Entry<iPlayer, List<iPiece>> entry : map.entrySet()){
            entry.getValue().clear();
            if (entry.getKey().getColor() == Color.White){
                entry.getValue().add(springer);
            }
        }

        springer.setPosition(5, 4);
        board.move(springer,6, 6);

        Position expectedPosition = new Position(6, 6);
        assertEquals(expectedPosition.getX(), springer.getPosition().getX());
        assertEquals(expectedPosition.getY(), springer.getPosition().getY());
    }

    @Test
    void testSpringerMoveTopRightRight() throws StatusException, GameException {
        BoardImpl board = new BoardImpl();
        Springer springer = new Springer(Color.White);

        board.pickColor(ALICE, Color.Black);
        board.pickColor(BOB, Color.White);

        board.initializeField();
        Map<iPlayer, List<iPiece>> map = board.getMap();

        for (Map.Entry<iPlayer, List<iPiece>> entry : map.entrySet()){
            entry.getValue().clear();
            if (entry.getKey().getColor() == Color.White){
                entry.getValue().add(springer);
            }
        }

        springer.setPosition(5, 4);
        board.move(springer,6, 6);

        Position expectedPosition = new Position(6, 6);
        assertEquals(expectedPosition.getX(), springer.getPosition().getX());
        assertEquals(expectedPosition.getY(), springer.getPosition().getY());
    }

    @Test
    void testSpringerMoveBottomRightRight() throws StatusException, GameException {
        BoardImpl board = new BoardImpl();
        Springer springer = new Springer(Color.White);

        board.pickColor(ALICE, Color.Black);
        board.pickColor(BOB, Color.White);

        board.initializeField();
        Map<iPlayer, List<iPiece>> map = board.getMap();

        for (Map.Entry<iPlayer, List<iPiece>> entry : map.entrySet()){
            entry.getValue().clear();
            if (entry.getKey().getColor() == Color.White){
                entry.getValue().add(springer);
            }
        }

        springer.setPosition(5, 4);
        board.move(springer,7, 3);

        Position expectedPosition = new Position(7, 3);
        assertEquals(expectedPosition.getX(), springer.getPosition().getX());
        assertEquals(expectedPosition.getY(), springer.getPosition().getY());
    }

    @Test
    void testSpringerMoveBottomBottomRight() throws StatusException, GameException {
        BoardImpl board = new BoardImpl();
        Springer springer = new Springer(Color.White);

        board.pickColor(ALICE, Color.Black);
        board.pickColor(BOB, Color.White);

        board.initializeField();
        Map<iPlayer, List<iPiece>> map = board.getMap();

        for (Map.Entry<iPlayer, List<iPiece>> entry : map.entrySet()){
            entry.getValue().clear();
            if (entry.getKey().getColor() == Color.White){
                entry.getValue().add(springer);
            }
        }

        springer.setPosition(5, 4);
        board.move(springer,6, 2);

        Position expectedPosition = new Position(6, 2);
        assertEquals(expectedPosition.getX(), springer.getPosition().getX());
        assertEquals(expectedPosition.getY(), springer.getPosition().getY());
    }

    @Test
    void testSpringerMoveBottomLeftLeft() throws StatusException, GameException {
        BoardImpl board = new BoardImpl();
        Springer springer = new Springer(Color.White);

        board.pickColor(ALICE, Color.Black);
        board.pickColor(BOB, Color.White);

        board.initializeField();
        Map<iPlayer, List<iPiece>> map = board.getMap();

        for (Map.Entry<iPlayer, List<iPiece>> entry : map.entrySet()){
            entry.getValue().clear();
            if (entry.getKey().getColor() == Color.White){
                entry.getValue().add(springer);
            }
        }

        springer.setPosition(5, 4);
        board.move(springer,3, 3);

        Position expectedPosition = new Position(3, 3);
        assertEquals(expectedPosition.getX(), springer.getPosition().getX());
        assertEquals(expectedPosition.getY(), springer.getPosition().getY());
    }

    @Test
    void testSpringerMoveBottomBottomLeft() throws StatusException, GameException {
        BoardImpl board = new BoardImpl();
        Springer springer = new Springer(Color.White);

        board.pickColor(ALICE, Color.Black);
        board.pickColor(BOB, Color.White);

        board.initializeField();
        Map<iPlayer, List<iPiece>> map = board.getMap();

        for (Map.Entry<iPlayer, List<iPiece>> entry : map.entrySet()){
            entry.getValue().clear();
            if (entry.getKey().getColor() == Color.White){
                entry.getValue().add(springer);
            }
        }

        springer.setPosition(5, 4);
        board.move(springer,4, 2);

        Position expectedPosition = new Position(4, 2);
        assertEquals(expectedPosition.getX(), springer.getPosition().getX());
        assertEquals(expectedPosition.getY(), springer.getPosition().getY());
    }

    @Test
    void testSpringerMoveBlocked() throws StatusException, GameException {
        BoardImpl board = new BoardImpl();
        Springer springer = new Springer(Color.White);
        Bauer ranPiece = new Bauer(Color.White);

        board.pickColor(ALICE, Color.Black);
        board.pickColor(BOB, Color.White);

        board.initializeField();
        Map<iPlayer, List<iPiece>> map = board.getMap();

        for (Map.Entry<iPlayer, List<iPiece>> entry : map.entrySet()){
            entry.getValue().clear();
            if (entry.getKey().getColor() == Color.White){
                entry.getValue().add(springer);
                entry.getValue().add(ranPiece);
            }
        }

        springer.setPosition(5, 4);
        ranPiece.setPosition(3, 3);
        assertThrows(GameException.class, () -> {
            board.move(springer,3, 3);
        });

        Position expectedPosition = new Position(5, 4);
        assertEquals(expectedPosition.getX(), springer.getPosition().getX());
        assertEquals(expectedPosition.getY(), springer.getPosition().getY());
    }

    @Test
    void testSpringerMoveCapture() throws StatusException, GameException {
        BoardImpl board = new BoardImpl();
        Springer springer = new Springer(Color.White);
        Bauer ranPiece = new Bauer(Color.Black);

        board.pickColor(ALICE, Color.Black);
        board.pickColor(BOB, Color.White);

        board.initializeField();
        Map<iPlayer, List<iPiece>> map = board.getMap();

        for (Map.Entry<iPlayer, List<iPiece>> entry : map.entrySet()){
            entry.getValue().clear();
            if (entry.getKey().getColor() == Color.Black){
                entry.getValue().add(ranPiece);
            }else if (entry.getKey().getColor() == Color.White){
                entry.getValue().add(springer);
            }
        }

        springer.setPosition(5, 4);
        ranPiece.setPosition(3, 3);
        board.move(springer, 3, 3);

        Position expectedPosition = new Position(3, 3);
        assertTrue(ranPiece.isCaptured());
        assertEquals(expectedPosition.getX(), springer.getPosition().getX());
        assertEquals(expectedPosition.getY(), springer.getPosition().getY());
    }

    @Test
    void testDameMoveTop() throws StatusException, GameException {
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

        dame.setPosition(5, 4);
        board.move(dame, 5, 5);

        Position expectedPosition = new Position(5, 5);
        assertEquals(expectedPosition.getX(), dame.getPosition().getX());
        assertEquals(expectedPosition.getY(), dame.getPosition().getY());
    }

    @Test
    void testDameMoveTopLeft() throws StatusException, GameException {
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

        dame.setPosition(5, 4);
        board.move(dame, 4, 5);

        Position expectedPosition = new Position(4, 5);
        assertEquals(expectedPosition.getX(), dame.getPosition().getX());
        assertEquals(expectedPosition.getY(), dame.getPosition().getY());
    }

    @Test
    void testDameMoveLeft() throws StatusException, GameException {
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

        dame.setPosition(5, 4);
        board.move(dame, 4, 4);

        Position expectedPosition = new Position(4, 4);
        assertEquals(expectedPosition.getX(), dame.getPosition().getX());
        assertEquals(expectedPosition.getY(), dame.getPosition().getY());
    }

    @Test
    void testDameDownLeft() throws StatusException, GameException {
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

        dame.setPosition(5, 4);
        board.move(dame, 4, 3);

        Position expectedPosition = new Position(4, 3);
        assertEquals(expectedPosition.getX(), dame.getPosition().getX());
        assertEquals(expectedPosition.getY(), dame.getPosition().getY());
    }

    @Test
    void testDameMoveDown() throws StatusException, GameException {
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

        dame.setPosition(5, 4);
        board.move(dame, 5, 3);

        Position expectedPosition = new Position(5, 3);
        assertEquals(expectedPosition.getX(), dame.getPosition().getX());
        assertEquals(expectedPosition.getY(), dame.getPosition().getY());
    }

    @Test
    void testDameMoveDownRight() throws StatusException, GameException {
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

        dame.setPosition(5, 4);
        board.move(dame, 6, 3);

        Position expectedPosition = new Position(6, 3);
        assertEquals(expectedPosition.getX(), dame.getPosition().getX());
        assertEquals(expectedPosition.getY(), dame.getPosition().getY());
    }

    @Test
    void testDameMoveRight() throws StatusException, GameException {
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

        dame.setPosition(5, 4);
        board.move(dame, 6, 4);

        Position expectedPosition = new Position(6, 4);
        assertEquals(expectedPosition.getX(), dame.getPosition().getX());
        assertEquals(expectedPosition.getY(), dame.getPosition().getY());
    }

    @Test
    void testDameMoveTopRight() throws StatusException, GameException {
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

        dame.setPosition(5, 4);
        board.move(dame, 6, 5);

        Position expectedPosition = new Position(6, 5);
        assertEquals(expectedPosition.getX(), dame.getPosition().getX());
        assertEquals(expectedPosition.getY(), dame.getPosition().getY());
    }


    @Test
    void testDameBlocked() throws StatusException, GameException {
        BoardImpl board = new BoardImpl();
        Dame dame = new Dame(Color.White);
        Bauer ranPiece = new Bauer(Color.White);

        board.pickColor(ALICE, Color.Black);
        board.pickColor(BOB, Color.White);

        board.initializeField();
        Map<iPlayer, List<iPiece>> map = board.getMap();

        for (Map.Entry<iPlayer, List<iPiece>> entry : map.entrySet()){
            entry.getValue().clear();
            if (entry.getKey().getColor() == Color.White){
                entry.getValue().add(ranPiece);
                entry.getValue().add(dame);
            }
        }

        dame.setPosition(5, 4);
        ranPiece.setPosition(5, 5);

        assertThrows(GameException.class, () -> {
            board.move(dame, 5, 5);
        });

        Position expectedPosition = new Position(5, 4);
        assertEquals(expectedPosition.getX(), dame.getPosition().getX());
        assertEquals(expectedPosition.getY(), dame.getPosition().getY());
    }

    @Test
    void testDameSpringeMoveFail() throws StatusException, GameException {
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

        dame.setPosition(5, 4);
        assertThrows(GameException.class, () -> {
            board.move(dame, 6, 6);
        });

        Position expectedPosition = new Position(5, 4);
        assertEquals(expectedPosition.getX(), dame.getPosition().getX());
        assertEquals(expectedPosition.getY(), dame.getPosition().getY());
    }

    @Test
    void testKoenigMoveTop() throws StatusException, GameException {
        BoardImpl board = new BoardImpl();
        Koenig koenig = new Koenig(Color.White);
        board.pickColor(ALICE, Color.Black);
        board.pickColor(BOB, Color.White);

        board.initializeField();
        Map<iPlayer, List<iPiece>> map = board.getMap();

        for (Map.Entry<iPlayer, List<iPiece>> entry : map.entrySet()){
            entry.getValue().clear();
            if (entry.getKey().getColor() == Color.White){
                entry.getValue().add(koenig);
            }
        }

        koenig.setPosition(5, 4);
        board.move(koenig, 5, 5);

        Position expectedPosition = new Position(5, 5);
        assertEquals(expectedPosition.getX(), koenig.getPosition().getX());
        assertEquals(expectedPosition.getY(), koenig.getPosition().getY());
    }

    @Test
    void testKoenigMoveTopLeft() throws StatusException, GameException {
        BoardImpl board = new BoardImpl();
        Koenig koenig = new Koenig(Color.White);
        board.pickColor(ALICE, Color.Black);
        board.pickColor(BOB, Color.White);

        board.initializeField();
        Map<iPlayer, List<iPiece>> map = board.getMap();

        for (Map.Entry<iPlayer, List<iPiece>> entry : map.entrySet()){
            entry.getValue().clear();
            if (entry.getKey().getColor() == Color.White){
                entry.getValue().add(koenig);
            }
        }

        koenig.setPosition(5, 4);
        board.move(koenig, 4, 5);

        Position expectedPosition = new Position(4, 5);
        assertEquals(expectedPosition.getX(), koenig.getPosition().getX());
        assertEquals(expectedPosition.getY(), koenig.getPosition().getY());
    }

    @Test
    void testKoenigMoveLeft() throws StatusException, GameException {
        BoardImpl board = new BoardImpl();
        Koenig koenig = new Koenig(Color.White);
        board.pickColor(ALICE, Color.Black);
        board.pickColor(BOB, Color.White);

        board.initializeField();
        Map<iPlayer, List<iPiece>> map = board.getMap();

        for (Map.Entry<iPlayer, List<iPiece>> entry : map.entrySet()){
            entry.getValue().clear();
            if (entry.getKey().getColor() == Color.White){
                entry.getValue().add(koenig);
            }
        }

        koenig.setPosition(5, 4);
        board.move(koenig, 4, 4);

        Position expectedPosition = new Position(4, 4);
        assertEquals(expectedPosition.getX(), koenig.getPosition().getX());
        assertEquals(expectedPosition.getY(), koenig.getPosition().getY());
    }

    @Test
    void testKoenigDownLeft() throws StatusException, GameException {
        BoardImpl board = new BoardImpl();
        Koenig koenig = new Koenig(Color.White);
        board.pickColor(ALICE, Color.Black);
        board.pickColor(BOB, Color.White);

        board.initializeField();
        Map<iPlayer, List<iPiece>> map = board.getMap();

        for (Map.Entry<iPlayer, List<iPiece>> entry : map.entrySet()){
            entry.getValue().clear();
            if (entry.getKey().getColor() == Color.White){
                entry.getValue().add(koenig);
            }
        }

        koenig.setPosition(5, 4);
        board.move(koenig, 4, 3);

        Position expectedPosition = new Position(4, 3);
        assertEquals(expectedPosition.getX(), koenig.getPosition().getX());
        assertEquals(expectedPosition.getY(), koenig.getPosition().getY());
    }

    @Test
    void testKoenigMoveDown() throws StatusException, GameException {
        BoardImpl board = new BoardImpl();
        Koenig koenig = new Koenig(Color.White);
        board.pickColor(ALICE, Color.Black);
        board.pickColor(BOB, Color.White);

        board.initializeField();
        Map<iPlayer, List<iPiece>> map = board.getMap();

        for (Map.Entry<iPlayer, List<iPiece>> entry : map.entrySet()){
            entry.getValue().clear();
            if (entry.getKey().getColor() == Color.White){
                entry.getValue().add(koenig);
            }
        }

        koenig.setPosition(5, 4);
        board.move(koenig, 5, 3);

        Position expectedPosition = new Position(5, 3);
        assertEquals(expectedPosition.getX(), koenig.getPosition().getX());
        assertEquals(expectedPosition.getY(), koenig.getPosition().getY());
    }

    @Test
    void testKoenigMoveDownRight() throws StatusException, GameException {
        BoardImpl board = new BoardImpl();
        Koenig koenig = new Koenig(Color.White);
        board.pickColor(ALICE, Color.Black);
        board.pickColor(BOB, Color.White);

        board.initializeField();
        Map<iPlayer, List<iPiece>> map = board.getMap();

        for (Map.Entry<iPlayer, List<iPiece>> entry : map.entrySet()){
            entry.getValue().clear();
            if (entry.getKey().getColor() == Color.White){
                entry.getValue().add(koenig);
            }
        }

        koenig.setPosition(5, 4);
        board.move(koenig, 6, 3);

        Position expectedPosition = new Position(6, 3);
        assertEquals(expectedPosition.getX(), koenig.getPosition().getX());
        assertEquals(expectedPosition.getY(), koenig.getPosition().getY());
    }

    @Test
    void testKoenigMoveRight() throws StatusException, GameException {
        BoardImpl board = new BoardImpl();
        Koenig koenig = new Koenig(Color.White);
        board.pickColor(ALICE, Color.Black);
        board.pickColor(BOB, Color.White);

        board.initializeField();
        Map<iPlayer, List<iPiece>> map = board.getMap();

        for (Map.Entry<iPlayer, List<iPiece>> entry : map.entrySet()){
            entry.getValue().clear();
            if (entry.getKey().getColor() == Color.White){
                entry.getValue().add(koenig);
            }
        }

        koenig.setPosition(5, 4);
        board.move(koenig, 6, 4);

        Position expectedPosition = new Position(6, 4);
        assertEquals(expectedPosition.getX(), koenig.getPosition().getX());
        assertEquals(expectedPosition.getY(), koenig.getPosition().getY());
    }

    @Test
    void testKoenigMoveTopRight() throws StatusException, GameException {
        BoardImpl board = new BoardImpl();
        Koenig koenig = new Koenig(Color.White);
        board.pickColor(ALICE, Color.Black);
        board.pickColor(BOB, Color.White);

        board.initializeField();
        Map<iPlayer, List<iPiece>> map = board.getMap();

        for (Map.Entry<iPlayer, List<iPiece>> entry : map.entrySet()) {
            entry.getValue().clear();
            if (entry.getKey().getColor() == Color.White) {
                entry.getValue().add(koenig);
            }
        }

        koenig.setPosition(5, 4);
        board.move(koenig, 6, 5);

        Position expectedPosition = new Position(6, 5);
        assertEquals(expectedPosition.getX(), koenig.getPosition().getX());
        assertEquals(expectedPosition.getY(), koenig.getPosition().getY());
    }

    @Test
    void testKoenigBlocked() throws StatusException, GameException {
        BoardImpl board = new BoardImpl();
        Koenig koenig = new Koenig(Color.White);
        Bauer ranPiece = new Bauer(Color.White);

        board.pickColor(ALICE, Color.Black);
        board.pickColor(BOB, Color.White);

        board.initializeField();
        Map<iPlayer, List<iPiece>> map = board.getMap();

        for (Map.Entry<iPlayer, List<iPiece>> entry : map.entrySet()){
            entry.getValue().clear();
            if (entry.getKey().getColor() == Color.White){
                entry.getValue().add(ranPiece);
                entry.getValue().add(koenig);
            }
        }

        koenig.setPosition(5, 4);
        ranPiece.setPosition(5, 5);

        assertThrows(GameException.class, () -> {
            board.move(koenig, 5, 5);
        });

        Position expectedPosition = new Position(5, 4);
        assertEquals(expectedPosition.getX(), koenig.getPosition().getX());
        assertEquals(expectedPosition.getY(), koenig.getPosition().getY());
    }

    @Test
    void testKoenigSpringeMoveFail() throws StatusException, GameException {
        BoardImpl board = new BoardImpl();
        Koenig koenig= new Koenig(Color.White);

        board.pickColor(ALICE, Color.Black);
        board.pickColor(BOB, Color.White);

        board.initializeField();
        Map<iPlayer, List<iPiece>> map = board.getMap();

        for (Map.Entry<iPlayer, List<iPiece>> entry : map.entrySet()){
            entry.getValue().clear();
            if (entry.getKey().getColor() == Color.White){
                entry.getValue().add(koenig);
            }
        }

        koenig.setPosition(5, 4);
        assertThrows(GameException.class, () -> {
            board.move(koenig, 6, 6);
        });

        Position expectedPosition = new Position(5, 4);
        assertEquals(expectedPosition.getX(), koenig.getPosition().getX());
        assertEquals(expectedPosition.getY(), koenig.getPosition().getY());
    }

    @Test
    void testKoenigRochadeLeft() throws StatusException, GameException {
        BoardImpl board = new BoardImpl();

        board.pickColor(ALICE, Color.Black);
        board.pickColor(BOB, Color.White);

        board.initializeField();
        Map<iPlayer, List<iPiece>> map = board.getMap();
        Koenig koenig = (Koenig)board.onField(5, 1);

        for (Map.Entry<iPlayer, List<iPiece>> entry : map.entrySet()){
            if (entry.getKey().getColor() == Color.White){
                List<iPiece> pieces = entry.getValue();
                for (int i = 2; i <= 4; i++){
                    pieces.remove(board.onField(i, 1));
                }
            }
        }
        board.move(koenig, 3, 1);

        Position expectedKoenigPosition = new Position(3, 1);
        Typ typ = Typ.TURM;

        assertEquals(expectedKoenigPosition.getX(), koenig.getPosition().getX());
        assertEquals(expectedKoenigPosition.getY(), koenig.getPosition().getY());
        assertEquals(typ, board.onField(4, 1).getType());
    }

    @Test
    void testKoenigRochadeRight() throws StatusException, GameException {
        BoardImpl board = new BoardImpl();

        board.pickColor(ALICE, Color.Black);
        board.pickColor(BOB, Color.White);

        board.initializeField();
        Map<iPlayer, List<iPiece>> map = board.getMap();
        Koenig koenig = (Koenig)board.onField(5, 1);

        for (Map.Entry<iPlayer, List<iPiece>> entry : map.entrySet()){
            if (entry.getKey().getColor() == Color.White){
                List<iPiece> pieces = entry.getValue();
                for (int i = 6; i <= 7; i++){
                    pieces.remove(board.onField(i, 1));
                }
            }
        }
        board.move(koenig, 7, 1);

        Position expectedKoenigPosition = new Position(7, 1);
        Typ typ = Typ.TURM;

        assertEquals(expectedKoenigPosition.getX(), koenig.getPosition().getX());
        assertEquals(expectedKoenigPosition.getY(), koenig.getPosition().getY());
        assertEquals(typ, board.onField(6, 1).getType());
    }


    @Test
    void testKoenigBlackRochadeLeft() throws StatusException, GameException {
        BoardImpl board = new BoardImpl();

        board.pickColor(ALICE, Color.Black);
        board.pickColor(BOB, Color.White);

        board.initializeField();
        Map<iPlayer, List<iPiece>> map = board.getMap();
        Koenig koenig = (Koenig)board.onField(5, 8);
        Bauer bauer = (Bauer)board.onField(2, 2);

        for (Map.Entry<iPlayer, List<iPiece>> entry : map.entrySet()){
            if (entry.getKey().getColor() == Color.Black){
                List<iPiece> pieces = entry.getValue();
                for (int i = 2; i <= 4; i++){
                    pieces.remove(board.onField(i, 8));
                }
            }
        }
        board.move(bauer, 2, 3);
        board.move(koenig, 3, 8);

        Position expectedKoenigPosition = new Position(3, 8);
        Typ typ = Typ.TURM;

        assertEquals(expectedKoenigPosition.getX(), koenig.getPosition().getX());
        assertEquals(expectedKoenigPosition.getY(), koenig.getPosition().getY());
        assertEquals(typ, board.onField(4, 8).getType());
    }

    @Test
    void testKoenigBlackRochadeRight() throws StatusException, GameException {
        BoardImpl board = new BoardImpl();

        board.pickColor(ALICE, Color.Black);
        board.pickColor(BOB, Color.White);

        board.initializeField();
        Map<iPlayer, List<iPiece>> map = board.getMap();
        Koenig koenig = (Koenig)board.onField(5, 8);
        Bauer bauer = (Bauer)board.onField(2, 2);
        for (Map.Entry<iPlayer, List<iPiece>> entry : map.entrySet()){
            if (entry.getKey().getColor() == Color.Black){
                List<iPiece> pieces = entry.getValue();
                for (int i = 6; i <= 7; i++){
                    pieces.remove(board.onField(i, 8));
                }
            }
        }
        board.move(bauer, 2, 3);
        board.move(koenig, 7, 8);

        Position expectedKoenigPosition = new Position(7, 8);
        Typ typ = Typ.TURM;

        assertEquals(expectedKoenigPosition.getX(), koenig.getPosition().getX());
        assertEquals(expectedKoenigPosition.getY(), koenig.getPosition().getY());
        assertEquals(typ, board.onField(6, 8).getType());
    }
    @Test
    void whiteMovingTwoTurnsInARow() throws StatusException, GameException {
        BoardImpl board = new BoardImpl();

        board.pickColor(ALICE, Color.Black);
        board.pickColor(BOB, Color.White);

        board.initializeField();
        Map<iPlayer, List<iPiece>> map = board.getMap();

        Bauer bauer = (Bauer)board.onField(2, 2);
        board.move(bauer, 2, 3);

        assertThrows(StatusException.class, () ->{
           board.move(bauer, 2, 4);
        });

        Position expectedPosition = new Position(2, 3);

        assertEquals(bauer.getPosition().getX(), expectedPosition.getX());
        assertEquals(bauer.getPosition().getY(), expectedPosition.getY());
    }

    @Test
    void moveBlackPiece() throws StatusException, GameException {
        BoardImpl board = new BoardImpl();
        board.pickColor(ALICE, Color.Black);
        board.pickColor(BOB, Color.White);

        board.initializeField();
        Map<iPlayer, List<iPiece>> map = board.getMap();

        Bauer bauer = (Bauer)board.onField(2, 2);
        Bauer bauerBlack = (Bauer)board.onField(2, 7);
        bauer.setPosition(2,2);
        board.move(bauer, 2, 3);
        board.move(bauerBlack, 2, 6);


        Position expectedPosition = new Position(2, 3);
        Position expectedPositionBlack = new Position(2, 6);

        assertEquals(bauer.getPosition().getX(), expectedPosition.getX());
        assertEquals(bauer.getPosition().getY(), expectedPosition.getY());
        assertEquals(bauerBlack.getPosition().getX(), expectedPositionBlack.getX());
        assertEquals(bauerBlack.getPosition().getY(), expectedPositionBlack.getY());
    }


    @Test
    void testCheckMate() throws StatusException, GameException {
        BoardImpl board = new BoardImpl();
        board.pickColor(ALICE, Color.Black);
        board.pickColor(BOB, Color.White);

        board.initializeField();
        Bauer bauer = (Bauer)board.onField(5, 2);
        Dame dame = (Dame)board.onField(4, 1);
        Laeufer laeufer = (Laeufer)board.onField(6, 1);
        Bauer bauerBlack = (Bauer)board.onField(1, 7);

        board.move(bauer, 5, 3);
        board.move(bauerBlack, 1, 6);
        board.move(dame, 8, 5);
        board.move(bauerBlack, 1, 5);
        board.move(laeufer, 3, 4);
        board.move(bauerBlack, 1, 4);
        board.move(dame, 6, 7);

        assertTrue(board.getGameEnd());
        assertEquals(board.getStatus(), Status.END);
    }

    @Test
    void testCheck() throws StatusException, GameException {
        BoardImpl board = new BoardImpl();
        board.pickColor(ALICE, Color.Black);
        board.pickColor(BOB, Color.White);

        board.initializeField();
        Bauer bauer = (Bauer)board.onField(5, 2);
        Dame dame = (Dame)board.onField(4, 1);
        Laeufer laeufer = (Laeufer)board.onField(6, 1);
        Bauer bauerBlack = (Bauer)board.onField(1, 7);
        Bauer bauerBlackTwo = (Bauer)board.onField(4, 7);

        board.move(bauer, 5, 3);
        board.move(bauerBlack, 1, 6);
        board.move(dame, 8, 5);
        board.move(bauerBlack, 1, 5);
        board.move(laeufer, 3, 4);
        board.move(bauerBlackTwo, 4, 6);
        board.move(dame, 6, 7);

        Map<iPlayer, List<iPiece>> map = board.getMap();

        List<iPlayer> players = new LinkedList<>(map.keySet());

        for (iPlayer player : players){
            if (player.getColor() == Color.Black){
                assertTrue(player.checked());
            }
        }
    }

    @Test
    void testCheckProtectKing() throws StatusException, GameException {
        BoardImpl board = new BoardImpl();
        board.pickColor(ALICE, Color.Black);
        board.pickColor(BOB, Color.White);

        board.initializeField();
        Bauer bauer = (Bauer)board.onField(5, 2);
        Dame dame = (Dame)board.onField(4, 1);
        Laeufer laeufer = (Laeufer)board.onField(6, 1);
        Bauer bauerBlack = (Bauer)board.onField(1, 7);
        Bauer bauerBlackTwo = (Bauer)board.onField(6, 7);
        Bauer bauerBlackThree = (Bauer)board.onField(7, 7);

        board.move(bauer, 5, 3);
        board.move(bauerBlackTwo, 6, 6);
        board.move(dame, 8, 5);

        assertThrows(GameException.class, () -> {
            board.move(bauerBlack, 1, 6);
        });

        assertThrows(GameException.class, () -> {
           board.move(bauerBlackTwo, 6, 5);
        });

        board.move(bauerBlackThree, 7, 6);

        Map<iPlayer, List<iPiece>> map = board.getMap();
        List<iPlayer> players = new LinkedList<>(map.keySet());

        for (iPlayer player : players){
            if (player.getColor() == Color.Black){
                assertTrue(player.checked());
            }
        }
    }

    @Test
    void testStalemate() throws StatusException, GameException {
        BoardImpl board = new BoardImpl();
        Koenig koenigw= new Koenig(Color.White);
        Koenig koenigb= new Koenig(Color.Black);
        Dame dame= new Dame(Color.White);

        board.pickColor(ALICE, Color.Black);
        board.pickColor(BOB, Color.White);

        board.initializeField();
        Map<iPlayer, List<iPiece>> map = board.getMap();

        for (Map.Entry<iPlayer, List<iPiece>> entry : map.entrySet()){
            entry.getValue().clear();
            if (entry.getKey().getColor() == Color.White){
                entry.getValue().add(koenigw);
                entry.getValue().add(koenigb);
                entry.getValue().add(dame);
            }
        }

        koenigw.setPosition(8, 6);
        koenigb.setPosition(7, 8);
        dame.setPosition(1, 6);

        board.move(dame, 6, 6);

        assertTrue(board.getGameEnd());
        assertEquals(Status.STALEMATE, board.getStatus());
    }

    @Test
    void testStalemateFullGame() throws StatusException, GameException {
        BoardImpl board = new BoardImpl();

        board.pickColor(ALICE, Color.Black);
        board.pickColor(BOB, Color.White);

        board.initializeField();
        Map<iPlayer, List<iPiece>> map = board.getMap();

        iPiece picked = board.onField(5, 2);
        board.move(picked, 5, 3);

        picked = board.onField(1, 7);
        board.move(picked, 1, 5);

        picked = board.onField(4, 1);
        board.move(picked, 8, 5);

        picked = board.onField(1, 8);
        board.move(picked, 1, 6);

        picked = board.onField(8, 5);
        board.move(picked, 1, 5);

        picked = board.onField(8, 7);
        board.move(picked, 8, 5);

        picked = board.onField(8, 2);
        board.move(picked, 8, 4);

        picked = board.onField(1, 6);
        board.move(picked, 8, 6);

        picked = board.onField(1, 5);
        board.move(picked, 3, 7);

        picked = board.onField(6, 7);
        board.move(picked, 6, 6);

        picked = board.onField(3, 7);
        board.move(picked, 4, 7);

        picked = board.onField(5, 8);
        board.move(picked, 6, 7);

        picked = board.onField(4, 7);
        board.move(picked, 2, 7);

        picked = board.onField(4, 8);
        board.move(picked, 4, 3);

        picked = board.onField(2, 7);
        board.move(picked, 2, 8);

        picked = board.onField(4, 3);
        board.move(picked, 8, 7);

        picked = board.onField(2, 8);
        board.move(picked, 3, 8);

        picked = board.onField(6, 7);
        board.move(picked, 7, 6);

        picked = board.onField(3, 8);
        board.move(picked, 5, 6);


        assertTrue(board.getGameEnd());
        assertEquals(Status.STALEMATE, board.getStatus());
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
