package sanrocks.tradingbot.service;

import sanrocks.tradingbot.domain.Company;

public interface CompanyReadService {

    Company getCompanyById(final String id);
}
