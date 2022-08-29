package sanrocks.tradingbot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import sanrocks.tradingbot.domain.ProductQuoteEvent;
import sanrocks.tradingbot.repository.ProductQuoteEventReactiveRepository;
import sanrocks.tradingbot.repository.ProductQuoteEventRepository;

import java.time.Duration;
import java.util.List;

@RestController
@RequestMapping("/v1/product-quote")
@CrossOrigin
public class ProductQuoteController {

    @Autowired
    private ProductQuoteEventReactiveRepository productQuoteEventReactiveRepository;

    @Autowired
    private ProductQuoteEventRepository productQuoteEventRepository;

    /**
     * @return
     */
    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ProductQuoteEvent> streamProducts() {
        return productQuoteEventReactiveRepository.findAll().delayElements(Duration.ofSeconds(0));
    }

    /**
     * @return
     */
    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ProductQuoteEvent>> listProductQuotes() {
        return new ResponseEntity<>(productQuoteEventRepository.findAll(), HttpStatus.OK);
    }
}
