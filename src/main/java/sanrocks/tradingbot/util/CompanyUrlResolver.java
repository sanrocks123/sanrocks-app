/* (C) 2023 */
package sanrocks.tradingbot.util;

import com.coxautodev.graphql.tools.GraphQLResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import sanrocks.tradingbot.domain.Company;

@Slf4j
@Component
public class CompanyUrlResolver implements GraphQLResolver<Company> {

    public String getUrl(Company company) {
        log.info("getUrl, companyId : [{}]", company.getId());
        return "http://base:port/v1/companies";
    }
}
