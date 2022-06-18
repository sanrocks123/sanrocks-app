package bux.tradingbot.domain;

import com.google.gson.Gson;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Document(collection = "companies")
public class Company {
    @Id
    private String id;
    private String name;

    @Field("email_address")
    private String emailAddress;

    @Field("phone_number")
    private String phoneNumber;
    private String description;

    public String toString() {
        return new Gson().toJson(this);
    }
}
