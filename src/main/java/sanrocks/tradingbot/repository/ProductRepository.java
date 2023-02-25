/* (C) 2023 */
package sanrocks.tradingbot.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import sanrocks.tradingbot.domain.Product;

@Repository
public interface ProductRepository extends ReactiveMongoRepository<Product, String> {}
