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

@Slf4j
@Component
@Rule(name = "when_targetSellPriceMatched_then_applyTrades", description = "apple product sell condition", priority = 1)
public class AppleSellRule extends ProductBaseRules {

    @Condition
    public boolean isTargetSellPriceMatched(@Fact("product") Product product) {

        if (null == product.getCurrentPrice()) {
            return false;
        }
        //return true;
        return product.getCurrentPrice().compareTo(product.getLowerLimitSellPrice()) == 0
            || product.getCurrentPrice().compareTo(product.getUpperLimitSellPrice()) == 0;
    }

    @Action
    public void applyTrades(@Fact("product") Product product) {
        Trade trade = new Trade();
        trade.setId("aa");
        trade.setPositionId("positionId");
        trade.setPositionType("SELL");

        product.setTrades(List.of(trade));

        log.info("when_targetSellPriceMatched_then_applyTrades : {}", product.toString(4));
    }

}
