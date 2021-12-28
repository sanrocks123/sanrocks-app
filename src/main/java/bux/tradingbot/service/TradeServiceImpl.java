package bux.tradingbot.service;

import bux.tradingbot.domain.Trade;
import bux.tradingbot.util.TradeBotUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

/**
 * Java Source TradeServiceImpl created on 12/23/2021
 *
 * @author : Sanjeev Saxena
 * @version : 1.0
 * @email : sanrocks123@gmail.com
 */

@Service
public class TradeServiceImpl implements TradeService {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private RestTemplate restTemplate;

    @Value("${bux.trade.server}")
    private String buxTradeServer;

    @Value("${bux.server-api.token}")
    private String token;

    /**
     * @param productId
     * @return
     */
    @Override
    public Trade buy(String productId) {

        Trade trade = new Trade();
        HttpEntity<String> entity = new HttpEntity<>(getBuyRequestPayload(productId).toString(), getHttpHeaders());

        String url = String.format("%s%s", buxTradeServer, TradeBotUtils.BUY);
        log.debug("[{}] trade buy, request: {}", productId, entity);

        ResponseEntity<String> response = executeTradeAPI(url, HttpMethod.POST, entity);
        prepareTradeResponse(trade, response);

        log.debug("[{}] trade buy, response: {}", productId, trade);
        return trade;
    }


    /**
     * @param positionId
     * @return
     */
    @Override
    public Trade sell(String positionId) {

        Trade trade = new Trade();
        HttpEntity<Void> entity = new HttpEntity<>(getHttpHeaders());

        String url = String.format("%s%s", buxTradeServer, String.format(TradeBotUtils.SELL, positionId));
        log.debug("[{}] trade sell, entity: {}", positionId, entity);

        ResponseEntity<String> response = executeTradeAPI(url, HttpMethod.DELETE, entity);
        prepareTradeResponse(trade, response);

        log.debug("[{}] trade sell, response: {}", positionId, trade);
        return trade;
    }

    /**
     * @return
     */
    private HttpHeaders getHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setAcceptLanguage(Locale.LanguageRange.parse("nl-NL,en;q=0.8"));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        return headers;
    }

    /**
     * @return
     */
    private JSONObject getBuyRequestPayload(String productId) {
        JSONObject request = TradeBotUtils.getNodeByName("buy");
        request.put("productId", productId);
        return request;
    }

    /**
     * Note: - We can further apply retry & circuit breakers on external API calls but keeping it simple for assignment
     *
     * @param url
     * @param method
     * @param entity
     * @return
     */
    private ResponseEntity<String> executeTradeAPI(final String url, final HttpMethod method, final HttpEntity<?> entity) {
        return restTemplate.exchange(url, method, entity, String.class);
    }

    /**
     * @param trade
     * @param response
     */
    private Trade prepareTradeResponse(final Trade trade, final ResponseEntity<String> response) {
        if (response.getStatusCode().equals(HttpStatus.OK)) {
            JSONObject tradeResponse = new JSONObject(response.getBody());
            trade.setId(tradeResponse.getString("id"));
            trade.setPositionId(tradeResponse.getString("positionId"));
            trade.setPositionType(tradeResponse.getString("type"));
            trade.getAttributes().putAll(tradeResponse.toMap());
        }
        return trade;
    }
}
