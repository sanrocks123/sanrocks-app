package bux.tradingbot.service;

import bux.tradingbot.domain.Company;
import bux.tradingbot.repository.CompanyRepository;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CompanyServiceImpl implements GraphQLQueryResolver, CompanyService {
    private final CompanyRepository companyRepository;

    public Company getCompanyById(final String id) {
        Optional<Company> result = companyRepository.findById(id);
        log.info("getCompanyById, result: [{}]", result.get());

        return companyRepository.findById(id).get();
    }
}
