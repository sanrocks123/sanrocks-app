package sanrocks.tradingbot.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

/**
 * Java Source TradeBotUtils created on 12/23/2021
 *
 * @author : Sanjeev Saxena
 * @version : 1.0
 * @email : sanrocks123@gmail.com
 */

@Slf4j
public class TradeBotUtils {

    public static final String SUBSCRIPTION = "/subscriptions/me";
    public static final String BUY = "/core/21/users/me/trades";
    public static final String SELL = "/core/21/users/me/portfolio/positions/%s";

    /**
     * @param json
     * @param classz
     * @param <T>
     * @return
     */
    public static <T> T deserialize(String json, Class<T> classz) {
        try {
            return new ObjectMapper().readValue(json, classz);
        } catch (JsonProcessingException e) {
            log.error("event processing error : ", e);
            throw new IllegalArgumentException(String.format("json deserialization error - %s", e.getMessage()));
        }
    }

    /**
     * @param key
     * @return
     */
    public static JSONObject getNodeByName(final String key) {
        try {
            InputStream in = TradeBotUtils.class.getResourceAsStream("/trade.json");
            String tradeJson = IOUtils.toString(in, "UTF-8");
            return new JSONObject(tradeJson).getJSONObject(key);

        } catch (IOException e) {
            log.error("error reading request file", e);
            throw new IllegalArgumentException(String.format("error getting node", e.getMessage()));
        }
    }
}
