package sanrocks.tradingbot.eventhandler;

import sanrocks.tradingbot.domain.Product;
import sanrocks.tradingbot.domain.ProductQuoteEvent;
import sanrocks.tradingbot.repository.ProductQuoteEventReactiveRepository;
import sanrocks.tradingbot.util.DefaultProductLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Java Source ProductQuoteMessageEventHandler created on 12/23/2021
 *
 * @author : Sanjeev Saxena
 * @version : 1.0
 * @email : sanrocks123@gmail.com
 */

@Component
public class ProductQuoteMessageEventHandler {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private DefaultProductLoader productTrades;

    @Autowired
    private ProductPriceUpdateEventHandler priceUpdateEventHandler;

    @Autowired
    private ProductQuoteEventReactiveRepository productQuoteEventRepository;


    /**
     * product update price is thread safe as products map is backed by ConcurrentHashMap
     *
     * @param pqe
     */
    public Product handleMessageEvent(ProductQuoteEvent pqe) {

        if (pqe.isTradingQuoteEvent()) {
            String securityId = pqe.getSecurityId();
            String currentPrice = pqe.getCurrentPrice();

            return productTrades.getProducts().computeIfPresent(securityId, (k, v) -> {
                v.setCurrentPrice(new BigDecimal(currentPrice));
                pqe.getBody().put("securityName", v.getProductName());
                productQuoteEventRepository.save(pqe).single().subscribe();
                return priceUpdateEventHandler.onPriceUpdate(v);
            });
        }

        log.debug("product not subscribed, event message skipped - {}", pqe);
        return null;
    }

}
