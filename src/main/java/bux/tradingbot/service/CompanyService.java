package bux.tradingbot.service;

import bux.tradingbot.domain.Company;

public interface CompanyService {
    Company getCompanyById(final String id);
}
