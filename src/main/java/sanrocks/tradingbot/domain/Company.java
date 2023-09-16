/* (C) 2023 */
package sanrocks.tradingbot.domain;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import sanrocks.tradingbot.domain.graph.GraphBaseAttributes;

@Data
@EqualsAndHashCode(callSuper = false)
@Document(collection = "companies")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Company extends GraphBaseAttributes {

    @Id private String id;
    private String name;

    @Field("email_address")
    private String emailAddress;

    @Field("phone_number")
    private String phoneNumber;

    private String description;
    private String url;

    private Tweet tweet;

    private String time;

    public String toString() {
        return new Gson().toJson(this);
    }
}
