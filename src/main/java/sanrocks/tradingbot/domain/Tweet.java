package sanrocks.tradingbot.domain;

import com.google.gson.Gson;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class Tweet {

    @Id
    private String id;
    private String text;

    public String toString() {
        return new Gson().toJson(this);
    }
}

@Data
class TweetInput extends Tweet {

}
