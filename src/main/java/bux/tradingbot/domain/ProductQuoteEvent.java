package bux.tradingbot.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.json.JSONObject;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Java Source ProductQuoteEvent created on 12/23/2021
 *
 * @author : Sanjeev Saxena
 * @version : 1.0
 * @email : sanrocks123@gmail.com
 */

@Data
@Document
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductQuoteEvent implements Serializable {

    private static final String CONNECT_CONNECTED_EVENT = "connect.connected";
    private static final String TRADING_QUOTE_EVENT = "trading.quote";

    @Id
    private String eventId;
    private String t = "";
    private Map<String, Object> body = new HashMap<>();

    public boolean isWebSocketConnectedEvent() {
        return CONNECT_CONNECTED_EVENT.equalsIgnoreCase(t);
    }

    public boolean isTradingQuoteEvent() {
        return TRADING_QUOTE_EVENT.equalsIgnoreCase(t);
    }

    public String getSecurityId() {
        return isTradingQuoteEvent() ? (String) getBody().get("securityId") : null;
    }

    public String getCurrentPrice() {
        return isTradingQuoteEvent() ? (String) getBody().get("currentPrice") : null;
    }

    public String toString() {
        return new JSONObject(this).toString();
    }

    public String getT() {
        return t;
    }

    public void setT(String event) {
        this.t = event;
    }

    public Map<String, Object> getBody() {
        return body;
    }

    public void setBody(Map<String, Object> body) {
        this.body = body;
    }
}
