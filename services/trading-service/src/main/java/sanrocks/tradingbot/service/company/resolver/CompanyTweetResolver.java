/* (C) 2023 */
package sanrocks.tradingbot.service.company.resolver;

import com.coxautodev.graphql.tools.GraphQLResolver;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sanrocks.tradingbot.domain.Company;
import sanrocks.tradingbot.domain.Tweet;
import sanrocks.tradingbot.rules.ProductRulesExecutor;
import sanrocks.tradingbot.util.aspect.ApplyRuleTracer;

@Slf4j
@Service
@AllArgsConstructor
public class CompanyTweetResolver implements GraphQLResolver<Company> {

    // auto wire to tweet datasource
    private final ProductRulesExecutor productRulesExecutor;

    @ApplyRuleTracer
    public Tweet getTweet(Company company) {

        Tweet tweet = new Tweet();
        tweet.setId("123");
        tweet.setText("Hello Twitter");

        log.info("getTweet, companyId: [{}], tweet: {}", company.getId(), tweet);

        productRulesExecutor.doExecute();
        // productRulesExecutor.doExecute();
        // productRulesExecutor.doExecute();

        return tweet;
    }
}
