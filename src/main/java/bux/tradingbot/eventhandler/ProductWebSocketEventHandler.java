package bux.tradingbot.eventhandler;

import bux.tradingbot.config.TradingWebSocketConfig;
import bux.tradingbot.domain.ProductQuoteEvent;
import bux.tradingbot.service.ProductEventService;
import bux.tradingbot.util.DefaultProductLoader;
import bux.tradingbot.util.TradeBotUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.*;

/**
 * Java Source ProductWebSocketEventHandler created on 12/24/2021
 *
 * @author : Sanjeev Saxena
 * @version : 1.0
 * @email : sanrocks123@gmail.com
 */

public class ProductWebSocketEventHandler extends Endpoint {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private static final String TEXT = "matched target buy/sell trades will be printed on console. To view live event stream, please set logging level to DEBUG | TRACE";

    private final DefaultProductLoader productTrades;
    private final TradingWebSocketConfig tradingWebSocketConfig;
    private final ProductEventService eventService;
    private Session session;

    /**
     * @param tradingWebSocketConfig
     * @param eventService
     * @param productTrades
     */
    public ProductWebSocketEventHandler(final TradingWebSocketConfig tradingWebSocketConfig,
            final ProductEventService eventService, final DefaultProductLoader productTrades) {
        this.tradingWebSocketConfig = tradingWebSocketConfig;
        this.eventService = eventService;
        this.productTrades = productTrades;
    }

    /**
     * @param session
     * @param config
     */
    @Override
    public void onOpen(final Session session, final EndpointConfig config) {
        log.info("onOpen socket connection established");

        this.session = session;
        this.session.addMessageHandler(new javax.websocket.MessageHandler.Whole<String>() {

            @Override
            public void onMessage(final String message) {

                JSONObject jsonMessage = new JSONObject(message);
                log.debug("onMessage - {}", jsonMessage.toString());

                logSubscriptionSuccess(jsonMessage);

                ProductQuoteEvent pqe = null;
                try {
                    pqe = TradeBotUtils.deserialize(message, ProductQuoteEvent.class);

                    if (pqe.isWebSocketConnectedEvent()) {
                        log.info("WebSocket Connected - {}", new JSONObject(message).toString(4));
                        subscribeDefaultProducts();
                    }

                    if (pqe.isTradingQuoteEvent())
                        eventService.pushTradeQuoteEvent(pqe);

                } catch (IllegalArgumentException e) {
                    log.error("onMessage, error : ", e);
                    eventService.pushError(pqe, e.getMessage());

                } catch (Exception e) {
                    log.error("onMessage, error : ", e);
                    eventService.pushError(pqe, e.getMessage());
                }
            }
        });
    }

    /**
     * @param userSession
     * @param reason
     */
    @Override
    public void onClose(Session userSession, CloseReason reason) {
        log.info("onClose socket event[{}] - reason [{}] [{}]", userSession.getId(), reason.getCloseCode(),
                reason.getReasonPhrase());
        this.session = null;

        // retry connection on closure
        tradingWebSocketConfig.connect(this);
    }

    /**
     * @param session
     * @param throwable
     */
    @Override
    public void onError(Session session, Throwable throwable) {
        log.error("onError", throwable);
    }

    /**
     * @param message
     */
    public void sendMessage(String message) {
        this.session.getAsyncRemote().sendText(message);
    }

    /**
     *
     */
    private void subscribeDefaultProducts() {
        log.info("\n\nNote: {}\n", TEXT);
        productTrades.getSubscriptions().forEach(p -> {

            JSONObject request = TradeBotUtils.getNodeByName("subscribe");
            request.getJSONArray("subscribeTo").put(p);

            log.info("subscribeDefaultProducts, send message: {}", request.toString(4));
            sendMessage(request.toString());
        });
    }

    private void logSubscriptionSuccess(final JSONObject jsonMessage) {
        if (jsonMessage.has("SUBSCRIBETO")) {
            log.info("onSubscriptionSuccess - {} - receiving events in background...", jsonMessage);
        }
    }
}
