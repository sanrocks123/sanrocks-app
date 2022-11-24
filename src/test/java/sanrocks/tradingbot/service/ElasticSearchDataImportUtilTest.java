package sanrocks.tradingbot.service;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.junit.jupiter.api.Disabled;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Disabled
@Slf4j
public class ElasticSearchDataImportUtilTest {


    public void elasticSearchDataImport() {
        String[] collections = {"companies"};
        for (String collection : collections) {
            doImport(collection);
        }
    }


    private void doImport(String collectionName) {
        String dataset = String.format(
            "/Users/sanrocks/git-repos/mongodb-json-files/datasets/%s.json", collectionName);
        try {

            //Bulk Approach:
            int count = 0;
            int batch = 100;
            List<JSONObject> docs = new ArrayList<>();

            try (BufferedReader br = new BufferedReader(new FileReader(dataset))) {
                String line;
                while ((line = br.readLine()) != null) {
                    JSONObject json = new JSONObject(line);
                    json.remove("id");
                    log.info("json : {}", json.toString(4));
                    docs.add(json);
                    count++;
                    if (count == batch) {
                        //coll.bulkWrite(docs, new BulkWriteOptions().ordered(false));
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
                //BulkWriteResult bulkWriteResult = coll.bulkWrite(docs, new BulkWriteOptions().ordered(false));
                //log.info("Inserted" + bulkWriteResult);
            }

        } catch (Exception e) {
            log.error("Error");
        }
    }

}
