package sanrocks.tradingbot.service;

import sanrocks.tradingbot.domain.Company;

public interface CompanyService {
    Company getCompanyById(final String id);
}
