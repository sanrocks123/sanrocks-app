package imc.rps.service;

import imc.rps.domain.Player;
import imc.rps.domain.Symbol;
import imc.rps.utils.GameResultEnum;
import imc.rps.utils.GameUtils;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class GameRulesSchemaServiceImplTest {

    GameRuleSchemaService gameRuleSchemaService = new GameRuleSchemaServiceImpl();

    @Test
    public void whenSameSymbolSelected_thenClash() {

        Player p1 =
                new Player() {
                    @Override
                    public String getPlayerNameInput() {
                        return "p1";
                    }

                    @Override
                    public Symbol getSelectedSymbolInput(final List<Symbol> symbols) {
                        return null;
                    }
                };
        p1.setSelectedSymbol(GameUtils.getSymbolList().get(0));

        Player p2 =
                new Player() {
                    @Override
                    public String getPlayerNameInput() {
                        return "p2";
                    }

                    @Override
                    public Symbol getSelectedSymbolInput(final List<Symbol> symbols) {
                        return null;
                    }
                };
        p2.setSelectedSymbol(GameUtils.getSymbolList().get(0));

        Assertions.assertEquals(GameResultEnum.CLASH, gameRuleSchemaService.findWinner(p1, p2));
    }

    @Test
    public void whenP1SelectsPaperAndP2SelectRock_thenP1Wins() {

        Player p1 =
                new Player() {
                    @Override
                    public String getPlayerNameInput() {
                        return "p1";
                    }

                    @Override
                    public Symbol getSelectedSymbolInput(final List<Symbol> symbols) {
                        return null;
                    }
                };
        p1.setSelectedSymbol(GameUtils.getSymbolList().get(0));

        Player p2 =
                new Player() {
                    @Override
                    public String getPlayerNameInput() {
                        return "p2";
                    }

                    @Override
                    public Symbol getSelectedSymbolInput(final List<Symbol> symbols) {
                        return null;
                    }
                };
        p2.setSelectedSymbol(GameUtils.getSymbolList().get(1));

        Assertions.assertEquals(GameResultEnum.PLAYER1, gameRuleSchemaService.findWinner(p1, p2));
    }

    @Test
    public void whenP2SelectsPaperAndP1SelectRock_thenP2Wins() {

        Player p1 =
                new Player() {
                    @Override
                    public String getPlayerNameInput() {
                        return "p1";
                    }

                    @Override
                    public Symbol getSelectedSymbolInput(final List<Symbol> symbols) {
                        return null;
                    }
                };
        p1.setSelectedSymbol(GameUtils.getSymbolList().get(1));

        Player p2 =
                new Player() {
                    @Override
                    public String getPlayerNameInput() {
                        return "p2";
                    }

                    @Override
                    public Symbol getSelectedSymbolInput(final List<Symbol> symbols) {
                        return null;
                    }
                };
        p2.setSelectedSymbol(GameUtils.getSymbolList().get(0));

        Assertions.assertEquals(GameResultEnum.PLAYER2, gameRuleSchemaService.findWinner(p1, p2));
    }
}
