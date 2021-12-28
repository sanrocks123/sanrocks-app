package bux.tradingbot.config;

import bux.tradingbot.eventhandler.ProductWebSocketEventHandler;
import bux.tradingbot.service.ProductEventService;
import bux.tradingbot.util.DefaultProductLoader;
import bux.tradingbot.util.TradeBotUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.websocket.ClientEndpointConfig;
import javax.websocket.ContainerProvider;
import javax.websocket.WebSocketContainer;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Java Source TradingWebSocketConfig created on 12/25/2021
 *
 * @author : Sanjeev Saxena
 * @version : 1.0
 * @email : sanrocks123@gmail.com
 */

@Configuration
public class TradingWebSocketConfig {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private int retry = 0;

    @Value("${bux.rtf-server}")
    private String url;

    @Value("${bux.server-api.token}")
    private String token;

    @Autowired
    private ProductEventService eventService;

    @Autowired
    private DefaultProductLoader productTrades;

    /**
     *
     */
    @PostConstruct
    public void init() {
        ProductWebSocketEventHandler eventHandler = new ProductWebSocketEventHandler(this, eventService, productTrades);
        this.url = String.format("%s%s", this.url.trim(), TradeBotUtils.SUBSCRIPTION);
        this.token = String.format("Bearer %s", this.token);

        connect(eventHandler);
    }

    /**
     * @param eventHandler
     */
    public void connect(final ProductWebSocketEventHandler eventHandler) {
        try {
            ClientEndpointConfig.Builder configBuilder = ClientEndpointConfig.Builder.create();
            configBuilder.configurator(new ClientEndpointConfig.Configurator() {
                @Override
                public void beforeRequest(Map<String, List<String>> headers) {
                    headers.put("Authorization", Arrays.asList(token));
                }
            });

            ClientEndpointConfig clientConfig = configBuilder.build();
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            container.connectToServer(eventHandler, clientConfig, URI.create(url));

        } catch (Exception e) {
            log.error("socket connect error {}", e.getMessage());
            connect(eventHandler);
        }
    }

}
