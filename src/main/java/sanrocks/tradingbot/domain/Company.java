package sanrocks.tradingbot.domain;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Document(collection = "companies")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Company {

    @Id
    private String id;
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

