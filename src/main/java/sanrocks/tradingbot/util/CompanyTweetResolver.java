/* (C) 2023 */
package sanrocks.tradingbot.util;

import com.coxautodev.graphql.tools.GraphQLResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sanrocks.tradingbot.domain.Company;
import sanrocks.tradingbot.domain.Tweet;

@Slf4j
@Service
public class CompanyTweetResolver implements GraphQLResolver<Company> {

    // auto wire to tweet datasource

    public Tweet getTweet(Company company) {
        Tweet tweet = new Tweet();
        tweet.setId("123");
        tweet.setText("Hello Twitter");

        log.info("getTweet, companyId: [{}], tweet: {}", company.getId(), tweet);
        return tweet;
    }
}
