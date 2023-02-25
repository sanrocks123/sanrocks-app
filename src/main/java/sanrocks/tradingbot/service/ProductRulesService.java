/* (C) 2023 */
package sanrocks.tradingbot.service;

import java.math.BigDecimal;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jeasy.rules.api.Facts;
import org.springframework.stereotype.Service;
import sanrocks.tradingbot.domain.Product;
import sanrocks.tradingbot.domain.rules.ProductRuleEngine;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductRulesService {

    private final ProductRuleEngine productRuleEngine;

    @PostConstruct
    public void init() {

        Product product = new Product("apple");
        product.setCurrentPrice(new BigDecimal("160.12"));

        Facts facts = new Facts();
        facts.put("product", product);
        facts.put("buyPrice", new BigDecimal("160.10"));

        productRuleEngine.doExecute(facts);
    }
}
