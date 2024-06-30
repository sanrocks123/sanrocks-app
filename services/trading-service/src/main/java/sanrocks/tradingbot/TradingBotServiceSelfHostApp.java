/* (C) 2023 */
package sanrocks.tradingbot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

/**
 * Java Source TradingBotServiceSelfHostApp created on 12/22/2021
 *
 * @author : Sanjeev Saxena
 * @version : 1.0
 * @email : sanrocks123@gmail.com
 */
@Slf4j
// @EnableElasticsearchRepositories
@EnableAspectJAutoProxy
@EnableMongoRepositories
@EnableReactiveMongoRepositories
@SpringBootApplication
public class TradingBotServiceSelfHostApp {

    /**
     * @param args
     */
    public static void main(final String[] args) {

        final SpringApplication app = new SpringApplication(TradingBotServiceSelfHostApp.class);
        app.run(args);

        log.info("OpenAPI 3.0 Docs - http://localhost:1000/trading-bot/swagger-ui.html");
    }
}
