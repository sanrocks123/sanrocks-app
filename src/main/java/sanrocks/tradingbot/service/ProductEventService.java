package sanrocks.tradingbot.service;

import sanrocks.tradingbot.domain.ProductQuoteEvent;

import java.util.List;

/**
 * Java Source ProductEventService created on 12/24/2021
 *
 * @author : Sanjeev Saxena
 * @version : 1.0
 * @email : sanrocks123@gmail.com
 */

public interface ProductEventService {

    /**
     * @param pqe
     * @return
     */
    boolean pushTradeQuoteEvent(ProductQuoteEvent pqe);

    /**
     * @param pqe
     * @param errorMessage
     * @return
     */
    boolean pushError(final ProductQuoteEvent pqe, String errorMessage);

    /**
     * @return
     */
    List<String> getAllErrors();
}
