/* (C) 2023 */
package sanrocks.tradingbot.rules;

import java.math.BigDecimal;
import java.util.List;
import javax.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jeasy.rules.api.Facts;
import org.springframework.stereotype.Service;
import sanrocks.tradingbot.config.RuleEngineConfig;
import sanrocks.tradingbot.domain.Product;
import sanrocks.tradingbot.rules.product.ProductBaseRules;

@Slf4j
@Service
@AllArgsConstructor
public class ProductRulesExecutor {

    private List<ProductBaseRules> productBaseRules;

    @PostConstruct
    public void init() {
        // doExecute();
    }

    public void doExecute() {
        Product product = new Product("apple");
        product.setCurrentPrice(new BigDecimal("160.01"));

        Facts facts = new Facts();
        facts.put("product", product);
        facts.put("buyPrice", new BigDecimal("160.01"));

        RuleEngineConfig productRuleEngine =
                RuleEngineConfig.getRuleEngineInstance(productBaseRules);

        productRuleEngine.doExecute(facts);
        // productRuleEngine.doCheck(facts);

    }
}
