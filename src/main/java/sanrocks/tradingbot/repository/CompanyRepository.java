/* (C) 2023 */
package sanrocks.tradingbot.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import sanrocks.tradingbot.domain.Company;

@Repository
public interface CompanyRepository extends MongoRepository<Company, String> {}
