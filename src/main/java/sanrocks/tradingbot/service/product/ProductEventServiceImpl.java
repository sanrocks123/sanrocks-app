/* (C) 2023 */
package sanrocks.tradingbot.service.product;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import sanrocks.tradingbot.domain.ErrorResponse;
import sanrocks.tradingbot.domain.Product;
import sanrocks.tradingbot.domain.ProductQuoteEvent;
import sanrocks.tradingbot.eventhandler.ProductQuoteMessageEventHandler;
import sanrocks.tradingbot.util.DefaultProductLoader;

/**
 * Java Source ProductEventServiceImpl created on 12/24/2021
 *
 * @author : Sanjeev Saxena
 * @version : 1.0
 * @email : sanrocks123@gmail.com
 */
@Service
public class ProductEventServiceImpl implements ProductEventService {

    public static final int MAX_CONSUMERS = Runtime.getRuntime().availableProcessors() + 2;
    private final Logger log = LoggerFactory.getLogger(getClass());

    private BlockingQueue<ProductQuoteEvent> tradingEventQueue = new ArrayBlockingQueue<>(100);
    private List<String> allErrors = new ArrayList<>();

    @Autowired private DefaultProductLoader productLoader;

    @Autowired private ProductQuoteMessageEventHandler eventHandler;

    /** consumer threads to pick up events for processing */
    private Runnable consumer =
            () -> {
                while (true) {
                    ProductQuoteEvent pqe = null;
                    try {
                        pqe = tradingEventQueue.take();
                        log.debug(
                                String.format(
                                        "consumer[%s] picked event, securityId:[%7s],"
                                                + " price:[%-8s], event: %s",
                                        Thread.currentThread().getName(),
                                        pqe.getSecurityId(),
                                        pqe.getCurrentPrice(),
                                        pqe));

                        eventHandler.handleMessageEvent(pqe);

                    } catch (Exception ex) {
                        log.error(
                                "[{} {}] event processing error - {}",
                                pqe.getSecurityId(),
                                getProductName(pqe),
                                ex.getMessage());
                        pushError(pqe, ex.getMessage());
                    }
                }
            };

    /** start consumer thread pool */
    @PostConstruct
    public void startConsumers() {
        for (int i = 0; i < MAX_CONSUMERS; i++) {
            new Thread(consumer, String.valueOf(i)).start();
        }
        log.info("number of consumer threads started: {}", MAX_CONSUMERS);
    }

    /**
     * Buffer continuous flow of trade quote events to broker/queue.
     *
     * @param pqe
     * @return
     */
    @Override
    public boolean pushTradeQuoteEvent(final ProductQuoteEvent pqe) {

        if (!pqe.isTradingQuoteEvent()) {
            throw new HttpClientErrorException(
                    HttpStatus.BAD_REQUEST,
                    "",
                    new ErrorResponse(
                                    "only trading quote events are accepted", "invalid_trade_quote")
                            .toByteArray(),
                    Charset.defaultCharset());
        }

        if (!productLoader.getProducts().containsKey(pqe.getSecurityId())) {
            throw new HttpClientErrorException(
                    HttpStatus.BAD_REQUEST,
                    "",
                    new ErrorResponse(
                                    String.format(
                                            "securityId [%s] not subscribed", pqe.getSecurityId()),
                                    "invalid_product_id")
                            .toByteArray(),
                    Charset.defaultCharset());
        }

        if (tradingEventQueue.offer(pqe)) {
            return true;
        }

        pushError(pqe, "trading event queue is full");
        throw new HttpClientErrorException(
                HttpStatus.BANDWIDTH_LIMIT_EXCEEDED,
                "",
                new ErrorResponse(
                                "trading event queue is full, please retry again after sometime",
                                "queue_full")
                        .toByteArray(),
                Charset.defaultCharset());
    }

    /**
     * Push event failures to error queue for quick temporary access - store product specific error
     * events in product object.
     *
     * @param pqe
     * @param errorMessage
     * @return
     */
    @Override
    public boolean pushError(final ProductQuoteEvent pqe, final String errorMessage) {

        productLoader
                .getProducts()
                .computeIfPresent(
                        pqe.getSecurityId(),
                        (k, v) -> {
                            v.addError(errorMessage);
                            return v;
                        });

        allErrors.add(errorMessage);
        return false;
    }

    /**
     * @return
     */
    @Override
    public List<String> getAllErrors() {
        return allErrors;
    }

    /**
     * @param pqe
     * @return
     */
    private String getProductName(final ProductQuoteEvent pqe) {
        return productLoader
                .getProducts()
                .getOrDefault(pqe.getSecurityId(), new Product())
                .getProductName();
    }
}
