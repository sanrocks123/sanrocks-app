/* (C) 2023 */
package sanrocks.tradingbot.service.company;

import sanrocks.tradingbot.domain.Company;

public interface CompanyReadService {

    Company getCompanyById(final String id);
}
