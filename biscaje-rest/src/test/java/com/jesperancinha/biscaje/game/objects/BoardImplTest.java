package com.jesperancinha.biscaje.game.objects;

import com.jesperancinha.biscaje.model.Board;
import com.jesperancinha.biscaje.model.Player;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Stack;

import static org.junit.Assert.assertSame;

/**
 * Created by joaofilipesabinoesperancinha on 17-04-16.
 */
public class BoardImplTest {

    private static final String JOAO = "João";
    private static final String PATRICIA = "Patricia";
    private static final String MARIA = "Maria";

    @Test
    public void getPlayers() {
        final Player player1 = Player.builder().build();
        final Player player2 = Player.builder().build();
        final Player player3 = Player.builder().build();

        player1.setOrderId(3);
        player2.setOrderId(1);
        player3.setOrderId(2);

        List<Player> players = Arrays.asList(
                player1,
                player2,
                player3
        );

        final Stack<Player> testStash = new Stack<>();
        testStash.push(player1);
        testStash.push(player3);
        testStash.push(player2);

        final Board board = new BoardImpl(players, null);
        final List<Player> allPlayersList = board.getPlayers();

        assertSame(player2, board.getCurrentPlayer());
        assertSame(player3, player2.getNextPlayer());
        assertSame(player1, player3.getNextPlayer());
        assertSame(player2, player1.getNextPlayer());

        for (final Player player : allPlayersList) {
            assertSame(testStash.pop(), player);
        }
    }

    @Test
    public void getCurrentPlayer() {
        final Player player1 = Player.builder().build();
        final Player player2 = Player.builder().build();
        final Player player3 = Player.builder().build();

        player1.setOrderId(3);
        player2.setOrderId(1);
        player3.setOrderId(2);

        List<Player> players = Arrays.asList(
                player1,
                player2,
                player3
        );
        final Board board = new BoardImpl(players, null);

        assertSame(player2, board.getCurrentPlayer());
    }

}