package bux.tradingbot.eventhandler;

import bux.tradingbot.domain.Product;
import bux.tradingbot.domain.Trade;
import bux.tradingbot.service.TradeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Java Source ProductPriceUpdateEventHandler created on 12/23/2021
 *
 * @author : Sanjeev Saxena
 * @version : 1.0
 * @email : sanrocks123@gmail.com
 */

@Service
public class ProductPriceUpdateEventHandler {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private TradeService tradeService;

    /**
     * @param product
     */
    public Product onPriceUpdate(Product product) {

        if (product.isTargetBuyPrice()) {
            log.info("buy, [{} {}], currentPrice: [{}]", product.getProductId(), product.getProductName(), product.getCurrentPrice());
            Trade trade = tradeService.buy(product.getProductId());
            product.addTrade(trade);
            return product;
        }

        if (product.isTargetSellPrice()) {
            log.info("sell, [{} {}], currentPrice: [{}]", product.getProductId(), product.getProductName(), product.getCurrentPrice());

            if (product.getOpenPositionIds().isEmpty()) {
                log.info("sell, [{} {}], no open positions available", product.getProductId(), product.getProductName());
                return product;
            }
            product.getOpenPositionIds().parallelStream().forEach(positionId -> {
                product.addTrade(tradeService.sell(positionId));
            });
            return product;
        }

        log.debug("onPriceUpdate, [{} {}], target buy/sell condition not met, event ignored", product.getProductId(), product.getProductName());
        return product;
    }

}
