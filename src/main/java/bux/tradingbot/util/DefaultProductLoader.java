package bux.tradingbot.util;

import bux.tradingbot.domain.Product;
import bux.tradingbot.repository.ProductRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Java Source DefaultProductLoader created on 12/23/2021
 *
 * @author : Sanjeev Saxena
 * @version : 1.0
 * @email : sanrocks123@gmail.com
 */

@Component
public class DefaultProductLoader {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final Map<String, Product> products = new ConcurrentHashMap<>();
    private final List<String> subscriptions = new ArrayList<>();

    @Autowired
    private Environment env;

    @PostConstruct
    public void init() {
        loadDefaults();
    }

    /**
     * load defaults at startup
     */
    private void loadDefaults() {

        JSONArray products = TradeBotUtils.getNodeByName("products").getJSONArray(env.getActiveProfiles()[0]);
        Random random = new Random();

        for (int i = 0; i < products.length(); i++) {
            Product p = TradeBotUtils.deserialize(products.getJSONObject(i).toString(), Product.class);
            setRandomBuyPrice(random, p);
            this.products.put(p.getProductId(), p);
            this.subscriptions.add(String.format("trading.product.%s", p.getProductId()));
        }

        log.info("default products loaded, size: [{}]", products.length());
    }

    /**
     * @param random
     * @param p
     */
    private void setRandomBuyPrice(final Random random, final Product p) {

        if (p.getPriceChangeList().isEmpty())
            return;

        int priceIdx = random.nextInt(p.getPriceChangeList().size());
        p.setBuyPrice(p.getPriceChangeList().get(priceIdx));
        p.getPriceChangeList().clear();
        p.setCurrentPrice(null);
    }


    /**
     * @return
     */
    public List<String> getSubscriptions() {
        return subscriptions;
    }

    /**
     * @return
     */
    public Map<String, Product> getProducts() {
        return products;
    }

}
