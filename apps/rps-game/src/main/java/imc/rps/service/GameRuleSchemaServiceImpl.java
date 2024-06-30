package imc.rps.service;

import imc.rps.domain.Player;
import imc.rps.domain.Symbol;
import imc.rps.utils.GameResultEnum;
import imc.rps.utils.GameUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameRuleSchemaServiceImpl implements GameRuleSchemaService {

    private final Map<Symbol, Symbol> ruleSchemaMap = new HashMap<>();

    public GameRuleSchemaServiceImpl() {
        buildRuleMap();
    }

    /**
     * dynamically builds the rule schema. The last element will link back to first element and thus
     * form circular closed loop.
     *
     * <p>Map will look like [{a:b}, {b:c}, {c:a}]
     */
    private void buildRuleMap() {
        List<Symbol> symbols = GameUtils.getSymbolList();
        int i = 0, j = i + 1;
        do {
            ruleSchemaMap.put(
                    symbols.get(i), j == symbols.size() ? symbols.get(0) : symbols.get(j));
            i++;
            j++;
        } while (i < symbols.size());

        // printRuleSchemaMap();
    }

    /**
     * Hash table based look up for O(1) time complexity
     *
     * @param player1
     * @param player2
     * @return
     */
    @Override
    public GameResultEnum findWinner(final Player player1, final Player player2) {

        Symbol player1Symbol = player1.getSelectedSymbol();
        Symbol player2Symbol = player2.getSelectedSymbol();

        return player1Symbol.code().equalsIgnoreCase(player2Symbol.code())
                ? GameResultEnum.CLASH
                : ruleSchemaMap.get(player1Symbol).code().equalsIgnoreCase(player2Symbol.code())
                        ? GameResultEnum.PLAYER1
                        : ruleSchemaMap
                                        .get(player2Symbol)
                                        .code()
                                        .equalsIgnoreCase(player1Symbol.code())
                                ? GameResultEnum.PLAYER2
                                : GameResultEnum.EVALUATION_ERROR;
    }

    private void printRuleSchemaMap() {
        System.out.print("\n\nruleSchemaMap");
        ruleSchemaMap.forEach(
                (k, v) -> {
                    System.out.printf("\n%s -> %s", k, v);
                });
    }
}
