package bux.tradingbot.main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Java Source BuxTradingBotServiceSelfHostApp created on 12/22/2021
 *
 * @author : Sanjeev Saxena
 * @version : 1.0
 * @email : sanrocks123@gmail.com
 */

@SpringBootApplication(scanBasePackages = "bux")
public class BuxTradingBotServiceSelfHostApp {

    private static final Logger log = LoggerFactory.getLogger(BuxTradingBotServiceSelfHostApp.class);
    private static final List<String> SUPPORTED_PROFILES = Arrays.asList("localhost", "dev");

    /**
     * @param args
     */
    public static void main(final String[] args) {

        Object profile = System.getenv().get("PROFILE");

        if (!Objects.isNull(profile) && !SUPPORTED_PROFILES.contains(profile.toString().toLowerCase())) {
            throw new IllegalArgumentException(String.format("Unsupported spring profile. Supported profile are [profile=%s]", SUPPORTED_PROFILES));
        }
        System.setProperty("spring.profiles.active", Objects.isNull(profile) ? "dev" : profile.toString().toLowerCase());

        final SpringApplication app = new SpringApplication(BuxTradingBotServiceSelfHostApp.class);
        app.run(args);

        log.info("Swagger API Docs - http://localhost:1000/swagger-ui/");
    }

}
