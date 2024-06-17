/* (C) 2023 */
package sanrocks.tradingbot.service.company;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sanrocks.tradingbot.domain.Company;

@Slf4j
@Service
public class CompanyWriteServiceImpl implements CompanyWriteService, GraphQLMutationResolver {

    @Override
    public Company saveCompany(final Company company) {

        log.info("request: {}", company);
        return company;
    }
}
