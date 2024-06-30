package imc.rps.service;

import imc.rps.domain.Player;
import imc.rps.utils.GameResultEnum;

public interface GameRuleSchemaService {

    /**
     * @param player1
     * @param player2
     * @return
     */
    public GameResultEnum findWinner(Player player1, Player player2);
}
