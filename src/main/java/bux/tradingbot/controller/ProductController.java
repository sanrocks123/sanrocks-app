package bux.tradingbot.controller;

import bux.tradingbot.domain.Product;
import bux.tradingbot.util.DefaultProductLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.*;

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
