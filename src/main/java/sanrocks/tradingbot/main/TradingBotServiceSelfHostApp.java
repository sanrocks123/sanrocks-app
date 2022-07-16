package sanrocks.tradingbot.main;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Java Source TradingBotServiceSelfHostApp created on 12/22/2021
 *
 * @author : Sanjeev Saxena
 * @version : 1.0
 * @email : sanrocks123@gmail.com
 */
@Slf4j
// @EnableElasticsearchRepositories(basePackages = "sanrocks")
@EnableMongoRepositories(basePackages = "sanrocks")
@EnableReactiveMongoRepositories(basePackages = "sanrocks")
@SpringBootApplication(scanBasePackages = "sanrocks")
public class TradingBotServiceSelfHostApp {

    private static final List<String> SUPPORTED_PROFILES = Arrays.asList("localhost", "dev");

    /**
     * @param args
     */
    public static void main(final String[] args) {

        Object profile = System.getenv().get("PROFILE");

        if (!Objects.isNull(profile) && !SUPPORTED_PROFILES.contains(profile.toString().toLowerCase())) {
            throw new IllegalArgumentException(String
                    .format("Unsupported spring profile. Supported profile are [profile=%s]", SUPPORTED_PROFILES));
        }
        System.setProperty("spring.profiles.active",
                Objects.isNull(profile) ? "localhost" : profile.toString().toLowerCase());

        final SpringApplication app = new SpringApplication(TradingBotServiceSelfHostApp.class);
        app.run(args);

        log.info("service using java runtime version: {}", System.getProperties().get("java.runtime.version"));
        log.info("OpenAPI 3.0 Docs - http://localhost:1000/trading-bot/swagger-ui.html");
    }

}
