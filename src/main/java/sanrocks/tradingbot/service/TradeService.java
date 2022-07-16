package sanrocks.tradingbot.service;

import sanrocks.tradingbot.domain.Trade;

/**
 * Java Source TradeService created on 12/23/2021
 *
 * @author : Sanjeev Saxena
 * @version : 1.0
 * @email : sanrocks123@gmail.com
 */

public interface TradeService {

    /**
     * @param productId
     * @return
     */
    Trade buy(final String productId);

    /**
     * @param positionId
     * @return
     */
    Trade sell(final String positionId);

}
