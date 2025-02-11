/* (C) 2023 */
package sanrocks.tradingbot.rules.product;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Fact;
import org.jeasy.rules.annotation.Rule;
import org.springframework.stereotype.Component;
import sanrocks.tradingbot.domain.Product;
import sanrocks.tradingbot.domain.Trade;
import sanrocks.tradingbot.rules.ProductRulesExecutor;

@Slf4j
@Component
@Rule(
        name = "when_targetSellPriceMatched_then_applyTrades",
        description = "apple product sell condition",
        priority = 1)
public class AppleSellRule extends ProductBaseRules {

    @Condition
    public boolean isTargetSellPriceMatched(@Fact("product") Product product) {
        if (null == product.getCurrentPrice()) {
            return false;
        }
        return product.isTargetBuyPrice();
    }

    @Action
    public void applyTrades(
            @Fact("product") Product product,
            @Fact("productRulesExecutor") ProductRulesExecutor productRulesExecutor) {
        Trade trade = new Trade();
        trade.setId("aa");
        trade.setPositionId("positionId");
        trade.setPositionType("SELL");

        product.setTrades(List.of(trade));

        new Thread(productRulesExecutor::doExecuteSingleRule).start();

        log.info("when_targetSellPriceMatched_then_applyTrades : {}", product.toString());
    }
}
