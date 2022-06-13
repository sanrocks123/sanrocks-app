package bux.tradingbot.controller;

import bux.tradingbot.domain.Product;
import bux.tradingbot.repository.ProductRepository;
import bux.tradingbot.util.DefaultProductLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Java Source ProductController created on 12/23/2021
 *
 * @author : Sanjeev Saxena
 * @version : 1.0
 * @email : sanrocks123@gmail.com
 */

@RestController
@RequestMapping("/v1/product")
public class ProductController {

    @Autowired
    private DefaultProductLoader productTrades;

    @Autowired
    private ProductRepository productRepository;

    @PostMapping
    public Mono<List<Product>> save(@RequestBody Product product) throws ExecutionException, InterruptedException {

        Callable<Mono<Product>> callable = () -> {
            return productRepository.findAll().next();
        };

        FutureTask<Mono<Product>> t1 = new FutureTask<>(callable);
        new Thread(t1).start();

        Mono<Product> products = t1.get();
        Mono<Product> save = productRepository.save(product);
        Mono<List<Product>> result = products.zipWith(save).map((t) -> Arrays.asList(t.getT1(), t.getT2()));
        return result;
    }

    /**
     * @return
     */
    @GetMapping("/all")
    public ResponseEntity<List<Product>> getAllProducts() {
        return new ResponseEntity<>(productTrades.getProducts().entrySet().stream().map(e -> e.getValue()).collect(Collectors.toList()), HttpStatus.OK);
    }

    /**
     * @return
     */
    @GetMapping("/{productId}")
    public ResponseEntity<Product> getProductById(@PathVariable("productId") String productId) {

        Product product = productTrades.getProducts().get(productId);
        if (Objects.isNull(product)) {
            new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    /**
     * patch product
     *
     * @param product
     * @return
     */
    @PatchMapping("/{productId}")
    public ResponseEntity<Product> patchProduct(@PathVariable("productId") String productId, @RequestBody Product product) {

        Product response = productTrades.getProducts().computeIfPresent(productId, (k, v) -> {
            v.setBuyPrice(product.getBuyPrice());
            return v;
        });

        if (Objects.isNull(response)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
