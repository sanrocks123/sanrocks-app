package imc.rps.utils;

import imc.rps.domain.*;
import imc.rps.exception.PlayerInitializationException;
import imc.rps.service.GameRuleSchemaService;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class GameRunner {

    private final List<Game> games = new ArrayList<>();
    private final GameRuleSchemaService gameRuleService;
    private final Scanner scanner = new Scanner(System.in);

    private Player player1 = null;
    private Player player2 = null;

    public GameRunner(final GameRuleSchemaService gameRuleService) {
        this.gameRuleService = gameRuleService;
        System.out.print("\n\nGame Started\n\n");
    }

    /** game runner entrypoint */
    public void run() {
        boolean isPlayAgain = false;
        int gameId = 1;

        do {
            initializePlayers();
            String winner = getWinnerName(gameRuleService.findWinner(player1, player2));
            games.add(
                    new Game(
                            gameId,
                            player1.getSelectedSymbol().name(),
                            player2.getSelectedSymbol().name(),
                            winner));

            System.out.print(
                    "\n\n"
                        + "Would you like to play again? enter 'Y' for Yes OR press any key to exit"
                        + " : ");
            String option = scanner.nextLine();

            if (option.equalsIgnoreCase("Y") || option.equalsIgnoreCase("Yes")) {
                gameId++;
                isPlayAgain = true;
                continue;
            }
            isPlayAgain = false;

        } while (isPlayAgain);

        GameUtils.showResult(games, player1, player2);
        System.out.print("\n\nThank you for playing the game. \n\n");
    }

    /**
     * Both players will be initialized in parallel and game will wait until we have both players
     * ready
     */
    private void initializePlayers() {
        CompletableFuture<Void> player1 =
                CompletableFuture.runAsync(
                        () -> {
                            if (Objects.isNull(this.player1)) {
                                this.player1 = play(new Computer());
                                return;
                            }
                            play(this.player1);
                        });

        CompletableFuture<Void> player2 =
                CompletableFuture.runAsync(
                        () -> {
                            if (Objects.isNull(this.player2)) {
                                this.player2 = play(new Human());
                                return;
                            }
                            play(this.player2);
                        });

        try {
            player1.get();
            player2.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new PlayerInitializationException(
                    String.format(
                            "Oops ! something went wrong with player initialization - %s",
                            e.getMessage()),
                    e);
        }
    }

    private Player play(final Player player) {

        if (player.getName().isEmpty()) {
            player.setName(player.getPlayerNameInput());
            System.out.printf(
                    "\nPlayer with Id: %d, name: %s joined", player.getId(), player.getName());
        }

        Symbol selectedSymbol = player.getSelectedSymbolInput(GameUtils.getSymbolList());
        player.setSelectedSymbol(selectedSymbol);

        return player;
    }

    private String getWinnerName(final GameResultEnum result) {
        String name = null;
        switch (result) {
            case CLASH -> {
                name = "Clash";
            }
            case PLAYER1 -> {
                player1.updateWinCounter();
                name = player1.getName();
            }

            case PLAYER2 -> {
                player2.updateWinCounter();
                name = player2.getName();
            }
            case EVALUATION_ERROR -> {
                name = "ERROR";
            }
        }

        String messageFormat = "\nWinner is %s because %s flashed %s and %s flashed %s";
        if ("Clash".equalsIgnoreCase(name)) {
            messageFormat = "\nIt's a %s because %s flashed %s and %s also flashed %s";
        }

        System.out.printf(
                messageFormat,
                name.toUpperCase(),
                player1.getName(),
                player1.getSelectedSymbol().name().toUpperCase(),
                player2.getName(),
                player2.getSelectedSymbol().name().toUpperCase());

        return name.toLowerCase();
    }
}
