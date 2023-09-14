package sanrocks.tradingbot.rules.product;

import lombok.extern.slf4j.Slf4j;
import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Fact;
import org.jeasy.rules.annotation.Rule;
import org.springframework.stereotype.Component;
import sanrocks.tradingbot.domain.Product;

@Slf4j
@Component
@Rule(name = "AppleExceptionRule", description = "AppleExceptionRule", priority = 2)
public class AppleExceptionRule extends ProductBaseRules {

    @Condition
    public boolean when(@Fact("product") Product product) {
        return true;
    }

    @Action
    public void then(@Fact("product") Product product) {
        log.info("AppleExceptionRule, exception rule executed");
        throw new RuntimeException("Rule apply failure");
    }
}
