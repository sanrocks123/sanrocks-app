/* (C) 2023 */
package sanrocks.tradingbot.service;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoWriteException;
import com.mongodb.bulk.BulkWriteResult;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.BulkWriteOptions;
import com.mongodb.client.model.InsertOneModel;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.data.mongodb.core.MongoTemplate;

@Disabled
@Slf4j
public class MongoDBDataImportUtilTest {

    private MongoTemplate mongoTemplate;

    @BeforeEach
    public void setup() {
        ConnectionString connectionString = new ConnectionString("mongodb://localhost:27017");
        MongoClientSettings mongoClientSettings =
                MongoClientSettings.builder().applyConnectionString(connectionString).build();
        mongoTemplate = new MongoTemplate(MongoClients.create(mongoClientSettings), "tradedb");
    }

    @Test
    public void mongoDataImport() {
        String[] collections = {"companies", "restaurant"};
        for (String collection : collections) {
            doImport(collection);
        }
    }

    private void doImport(String collectionName) {
        String dataset =
                String.format(
                        "/Users/sanrocks/git-repos/mongodb-json-files/datasets/%s.json",
                        collectionName);
        MongoCollection<Document> coll = mongoTemplate.getCollection(collectionName);

        try {

            // drop previous import
            coll.drop();

            // Bulk Approach:
            int count = 0;
            int batch = 100;
            List<InsertOneModel<Document>> docs = new ArrayList<>();

            try (BufferedReader br = new BufferedReader(new FileReader(dataset))) {
                String line;
                while ((line = br.readLine()) != null) {
                    // log.info("line : {}", line);
                    docs.add(new InsertOneModel<>(Document.parse(line)));
                    count++;
                    if (count == batch) {
                        coll.bulkWrite(docs, new BulkWriteOptions().ordered(false));
                        docs.clear();
                        count = 0;
                    }
                }
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            if (count > 0) {
                BulkWriteResult bulkWriteResult =
                        coll.bulkWrite(docs, new BulkWriteOptions().ordered(false));
                log.info("Inserted" + bulkWriteResult);
            }

        } catch (MongoWriteException e) {
            log.error("Error");
        }
    }
}
