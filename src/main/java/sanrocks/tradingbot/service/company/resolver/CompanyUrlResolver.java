/* (C) 2023 */
package sanrocks.tradingbot.service.company.resolver;

import com.coxautodev.graphql.tools.GraphQLResolver;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import sanrocks.tradingbot.domain.Company;
import sanrocks.tradingbot.rules.ProductRulesExecutor;
import sanrocks.tradingbot.util.aspect.ApplyRuleTracer;

@Slf4j
@Component
@AllArgsConstructor
public class CompanyUrlResolver implements GraphQLResolver<Company> {

    private final ProductRulesExecutor productRulesExecutor;

    @ApplyRuleTracer
    public String getUrl(Company company) {

        log.info("getUrl, companyId : [{}]", company.getId());

        productRulesExecutor.doExecute();
        productRulesExecutor.doExecuteSingleRule();

        return "http://base:port/v1/companies";
    }
}
