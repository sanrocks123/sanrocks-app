/* (C) 2023 */
package sanrocks.tradingbot.service.company;

import sanrocks.tradingbot.domain.Company;

public interface CompanyWriteService {

    Company saveCompany(final Company company);
}
