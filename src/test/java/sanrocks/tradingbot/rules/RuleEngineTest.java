/* (C) 2023 */
package sanrocks.tradingbot.rules;

import java.math.BigDecimal;
import lombok.extern.slf4j.Slf4j;
import org.jeasy.random.EasyRandom;
import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rules;
import org.jeasy.rules.api.RulesEngine;
import org.jeasy.rules.core.DefaultRulesEngine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sanrocks.tradingbot.domain.Company;
import sanrocks.tradingbot.domain.Product;
import sanrocks.tradingbot.rules.product.AppleProductRule;
import sanrocks.tradingbot.rules.product.AppleSellRule;

@Slf4j
public class RuleEngineTest {

    Rules rules = new Rules();
    RulesEngine rulesEngine = new DefaultRulesEngine();

    @BeforeEach
    public void init() {
        System.out.println("initializing rule engine...");
        rules.register(new AppleProductRule());
        rules.register(new AppleSellRule());

        EasyRandom random = new EasyRandom();
        Company company = random.nextObject(Company.class);
        log.info("company object: {}", company);
    }

    @Test
    public void ruleEngineMain() {

        Product product = new Product("apple");
        product.setCurrentPrice(new BigDecimal("160.10"));

        Facts facts = new Facts();
        facts.put("product", product);
        facts.put("lowerLimit", new BigDecimal("50.02"));
        facts.put("upperLimit", new BigDecimal("60.10"));

        rulesEngine.fire(rules, facts);

        log.info("after rules execution: {}", product);
    }
}
