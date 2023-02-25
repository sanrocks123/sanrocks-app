/* (C) 2023 */
package sanrocks.tradingbot.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import sanrocks.tradingbot.domain.ProductQuoteEvent;

@Repository
public interface ProductQuoteEventRepository extends MongoRepository<ProductQuoteEvent, String> {}
