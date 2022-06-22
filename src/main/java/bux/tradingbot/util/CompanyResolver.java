package bux.tradingbot.util;

import bux.tradingbot.domain.Company;
import bux.tradingbot.domain.Tweet;
import com.coxautodev.graphql.tools.GraphQLResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CompanyResolver implements GraphQLResolver<Company> {

    // auto wire to tweet datasource

    public Tweet getTweet(Company company) {
        Tweet tweet = new Tweet();
        tweet.setId("123");
        tweet.setText("Hello Twitter");

        log.info("getTweet, tweet: {}", tweet);
        return tweet;
    }
}
