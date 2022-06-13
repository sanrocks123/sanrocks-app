package bux.tradingbot.repository;

import bux.tradingbot.domain.ProductQuoteEvent;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductQuoteEventRepository extends MongoRepository<ProductQuoteEvent, String> {
}
