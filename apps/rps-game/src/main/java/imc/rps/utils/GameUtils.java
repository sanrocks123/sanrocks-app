package imc.rps.utils;

import imc.rps.domain.Game;
import imc.rps.domain.Player;
import imc.rps.domain.Symbol;
import java.util.*;

public class GameUtils {

    private static final Object symbolsLock = new Object();
    private static final Object option2SymbolMapLock = new Object();

    private static volatile List<Symbol> symbols = getSymbolList();
    private static volatile Map<Integer, Symbol> option2SymbolMap = getOption2SymbolMap();

    /**
     * @return
     */
    public static List<Symbol> getSymbolList() {
        if (Objects.isNull(symbols)) {
            synchronized (symbolsLock) {
                if (Objects.isNull(symbols)) {
                    symbols = new ArrayList<>();
                    symbols.add(new Symbol(1, "open-hand", "paper"));
                    symbols.add(new Symbol(2, "fist", "rock"));
                    symbols.add(new Symbol(3, "index-middle-finger", "scissors"));
                    // printSymbolList();
                }
            }
        }
        return symbols;
    }

    /**
     * @return
     */
    public static Map<Integer, Symbol> getOption2SymbolMap() {
        if (Objects.isNull(option2SymbolMap)) {
            synchronized (option2SymbolMapLock) {
                if (Objects.isNull(option2SymbolMap)) {
                    option2SymbolMap = new HashMap<>();
                    getSymbolList()
                            .forEach(
                                    s -> {
                                        option2SymbolMap.put(s.sequenceNo(), s);
                                    });
                    // printOption2SymbolMap();
                }
            }
        }
        return option2SymbolMap;
    }

    /**
     * @param games
     * @param player1
     * @param player2
     */
    public static void showResult(
            final List<Game> games, final Player player1, final Player player2) {

        System.out.print("\n\nGame Summary");
        System.out.printf(
                String.format(
                        "\n\n%-3s  %-20s  %-20s  %-10s\n",
                        "No", player1.getName(), player2.getName(), "WINNER"));

        games.forEach(
                game -> {
                    System.out.printf(
                            String.format(
                                    "\n%-3d  %-20s  %-20s  %-10s",
                                    game.gameNo(),
                                    game.p1Symbol(),
                                    game.p2Symbol(),
                                    game.winner()));
                });

        int player1Wins = player1.getNumberOfWins();
        int player2Wins = player2.getNumberOfWins();

        System.out.printf(
                "\n\nTotal wins by %s is %d and %s is %d",
                player1.getName(), player1Wins, player2.getName(), player2Wins);

        if (player1Wins == player2Wins) {
            System.out.print("\n\nGame resulted in TIE");
            return;
        }

        System.out.printf(
                "\n\nFinal Winner is %s",
                player1Wins > player2Wins ? player1.getName() : player2.getName());
    }

    private static void printSymbolList() {
        System.out.print("\nsymbolList : ");
        symbols.forEach(
                s -> {
                    System.out.printf("\n%s", s);
                });
    }

    private static void printOption2SymbolMap() {
        System.out.print("\n\noption2SymbolMap");
        option2SymbolMap.forEach(
                (k, v) -> {
                    System.out.printf("\n%s -> %s", k, v);
                });
    }
}
