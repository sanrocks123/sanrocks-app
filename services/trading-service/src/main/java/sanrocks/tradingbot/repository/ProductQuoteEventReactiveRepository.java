/* (C) 2023 */
package sanrocks.tradingbot.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import sanrocks.tradingbot.domain.ProductQuoteEvent;

@Repository
@Transactional
public interface ProductQuoteEventReactiveRepository
        extends ReactiveMongoRepository<ProductQuoteEvent, String> {}
