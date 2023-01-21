package sanrocks.tradingbot.service;

import sanrocks.tradingbot.domain.Company;

public interface CompanyWriteService {

    Company saveCompany(final Company company);
}
