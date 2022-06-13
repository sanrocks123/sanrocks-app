package bux.tradingbot.repository;

import bux.tradingbot.domain.ProductQuoteEvent;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface ProductQuoteEventReactiveRepository extends ReactiveMongoRepository<ProductQuoteEvent, String> {
}
