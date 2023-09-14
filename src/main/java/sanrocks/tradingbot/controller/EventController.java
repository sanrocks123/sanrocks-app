/* (C) 2023 */
package sanrocks.tradingbot.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sanrocks.tradingbot.domain.ProductQuoteEvent;
import sanrocks.tradingbot.service.product.ProductEventService;

/**
 * Java Source EventController created on 12/25/2021
 *
 * @author : Sanjeev Saxena
 * @version : 1.0
 * @email : sanrocks123@gmail.com
 */
@RestController
@RequestMapping("/v1")
public class EventController {

    @Autowired private ProductEventService eventService;

    /**
     * push any custom trade quote event
     *
     * @param productQuoteEvent
     * @return
     */
    @PostMapping("/event")
    public ResponseEntity<String> pushTradeQuoteMessage(
            @RequestBody ProductQuoteEvent productQuoteEvent) {

        eventService.pushTradeQuoteEvent(productQuoteEvent);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    /**
     * @return
     */
    @GetMapping("/event/all-errors")
    public ResponseEntity<List<String>> allErrors() {
        return new ResponseEntity<>(eventService.getAllErrors(), HttpStatus.OK);
    }
}
