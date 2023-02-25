/* (C) 2023 */
package sanrocks.tradingbot.domain;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Document(collection = "restaurant")
@Builder
public class Restaurant {

    @Id private String id;
    private String name;

    @Field("URL")
    private String url;

    private String address;
    private String outcode;
    private String postcode;
    private double rating;

    @Field("type_of_food")
    private String foodType;
}
