/* (C) 2023 */
package sanrocks.tradingbot.domain;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;

/**
 * Java Source Trade created on 12/23/2021
 *
 * @author : Sanjeev Saxena
 * @version : 1.0
 * @email : sanrocks123@gmail.com
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Trade implements Serializable {

    private static final long serialVersionUID = 10l;

    @Setter @Getter private String id;

    @Setter @Getter private String positionId;

    @Setter @Getter private String positionType;

    private Map<String, Object> attributes = new HashMap<>();

    @JsonAnyGetter
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @JsonAnySetter
    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public String toString() {
        return new JSONObject(this).toString();
    }
}
