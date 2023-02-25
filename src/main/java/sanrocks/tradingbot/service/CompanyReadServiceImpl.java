/* (C) 2023 */
package sanrocks.tradingbot.service;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sanrocks.tradingbot.domain.Company;
import sanrocks.tradingbot.exception.CompanyNotFoundException;
import sanrocks.tradingbot.repository.CompanyRepository;

@Service
@Slf4j
@RequiredArgsConstructor
public class CompanyReadServiceImpl implements GraphQLQueryResolver, CompanyReadService {

    private final CompanyRepository companyRepository;
    private final HttpServletRequest httpServletRequest;

    public Company getCompanyById(final String id) {
        log.info("http request: {}", httpServletRequest.getRequestURI());

        Optional<Company> result = companyRepository.findById(id);

        if (result.isEmpty()) {
            Map<String, Object> map = new HashMap<>();
            map.put("hint", "you may want to try out with some other valid companyIds");
            throw new CompanyNotFoundException(String.format("Company Id '%s' Not Found", id), map);
        }

        log.info("getCompanyById, result: [{}]", result.get());
        log.info("getCompanyById, result: [{}]", result.get());

        return result.get();
    }
}
