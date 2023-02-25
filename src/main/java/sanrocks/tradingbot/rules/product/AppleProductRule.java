/* (C) 2023 */
package sanrocks.tradingbot.rules.product;

import java.math.BigDecimal;
import lombok.extern.slf4j.Slf4j;
import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Fact;
import org.jeasy.rules.annotation.Rule;
import org.springframework.stereotype.Component;
import sanrocks.tradingbot.domain.Product;

@Slf4j
@Component
@Rule(
        name = "when_appleProductMatched_then_applySellPricingLimits",
        description = "calculate apple pricing",
        priority = 1)
public class AppleProductRule extends ProductBaseRules {

    @Condition
    public boolean isAppleProduct(@Fact("product") Product product) {
        return product.getProductName().equalsIgnoreCase("apple");
    }

    @Action
    public void applySellPricingLimit(
            @Fact("product") Product product, @Fact("buyPrice") BigDecimal buyPrice) {

        product.setBuyPrice(buyPrice);

        log.info("when_applyProductMatched_then_applySellPricingLimits {}", product.toString(4));

        // throw new RuntimeException("Rule apply failure");
    }
}
