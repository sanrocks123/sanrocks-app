package sanrocks.tradingbot.service;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClients;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.mongodb.core.MongoTemplate;

@Slf4j
//@SpringBootTest(classes = BuxTradingBotServiceSelfHostApp.class)
public class PracticeTest {

    private MongoTemplate restaurantDbTemplate;
    //private ElasticsearchRestTemplate esTemplate;

    @BeforeEach
    public void setup() {
        ConnectionString connectionString = new ConnectionString("mongodb://localhost:27017");
        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .build();
        restaurantDbTemplate = new MongoTemplate(MongoClients.create(mongoClientSettings), "restaurantdb");

        /**
        ClientConfiguration clientConfiguration
                = ClientConfiguration.builder()
                .connectedTo("localhost:9200")
                .build();

        esTemplate = new ElasticsearchRestTemplate(RestClients.create(clientConfiguration).rest());
    */
         }

    //@Disabled
    @Test
    public void testElasticSearchImport() {
        restaurantDbTemplate.getCollection("restaurant").find().forEach(r -> {
            log.info("document : {}", r.toJson());
           // esTemplate.

        });
    }
}
