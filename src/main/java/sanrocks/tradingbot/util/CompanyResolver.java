package sanrocks.tradingbot.util;

import sanrocks.tradingbot.domain.Company;
import sanrocks.tradingbot.domain.Tweet;
import com.coxautodev.graphql.tools.GraphQLResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CompanyResolver implements GraphQLResolver<Company> {

    // auto wire to tweet datasource

    public String getUrl(Company company) {
        log.info("getUrl, companyId : [{}]", company.getId());
        return "http://base:port/v1/companies";
    }

    public Tweet getTweet(Company company) {
        Tweet tweet = new Tweet();
        tweet.setId("123");
        tweet.setText("Hello Twitter");

        log.info("getTweet, companyId: [{}], tweet: {}", company.getId(), tweet);
        return tweet;
    }
}
