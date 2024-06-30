/* (C) 2023 */
package sanrocks.tradingbot.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import sanrocks.tradingbot.domain.Product;
import sanrocks.tradingbot.repository.ProductRepository;
import sanrocks.tradingbot.util.DefaultProductLoader;

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

    @Autowired private DefaultProductLoader productTrades;

    @Autowired private ProductRepository productRepository;

    @PostMapping
    @Operation(summary = "Create new product resource")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "201",
                        description = "New product resource created",
                        content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Product.class))
                        }),
                @ApiResponse(
                        responseCode = "400",
                        description = "something is wrong product request payload",
                        content = @Content)
            })
    public Mono<List<Product>> save(@RequestBody Product product)
            throws ExecutionException, InterruptedException {

        Callable<Mono<Product>> callable =
                () -> {
                    return productRepository.findAll().next();
                };

        FutureTask<Mono<Product>> t1 = new FutureTask<>(callable);
        new Thread(t1).start();

        Mono<Product> products = t1.get();
        Mono<Product> save = productRepository.save(product);
        Mono<List<Product>> result =
                products.zipWith(save).map((t) -> Arrays.asList(t.getT1(), t.getT2()));
        return result;
    }

    /**
     * @return
     */
    @GetMapping("/all")
    @Operation(summary = "API to list products")
    public ResponseEntity<List<Product>> getAllProducts(Pageable pageable) {
        return new ResponseEntity<>(
                productTrades.getProducts().entrySet().stream()
                        .map(e -> e.getValue())
                        .collect(Collectors.toList()),
                HttpStatus.OK);
    }

    /**
     * @return
     */
    @GetMapping("/{productId}")
    @Operation(summary = "API to find product by ID")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "product found",
                        content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Product.class))
                        }),
                @ApiResponse(
                        responseCode = "404",
                        description = "Product Not Found",
                        content = @Content)
            })
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
    public ResponseEntity<Product> patchProduct(
            @PathVariable("productId") String productId, @RequestBody Product product) {

        Product response =
                productTrades
                        .getProducts()
                        .computeIfPresent(
                                productId,
                                (k, v) -> {
                                    v.setBuyPrice(product.getBuyPrice());
                                    return v;
                                });

        if (Objects.isNull(response)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
