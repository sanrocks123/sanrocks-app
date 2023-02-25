/* (C) 2023 */
package sanrocks.tradingbot.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

// @Disabled
@Slf4j
public class ElasticSearchDataImportUtilTest {

    WebClient webClient =
            WebClient.builder().baseUrl("http://localhost:9200/companies/_doc").build();

    @Test
    public void elasticSearchDataImport() {

        String[] collections = {"companies"};
        for (String indexName : collections) {
            doImport(indexName);
        }
    }

    private void doImport(String collectionName) {
        String dataset =
                String.format(
                        "/Users/sanrocks/git-repos/mongodb-json-files/datasets/%s.json",
                        collectionName);
        try {

            List<JSONObject> docs = new ArrayList<>();

            try (BufferedReader br = new BufferedReader(new FileReader(dataset))) {
                String line;
                while ((line = br.readLine()) != null) {

                    try {

                        JSONObject json = new JSONObject(line);
                        json.remove("_id");
                        json.remove("image");
                        json.remove("screenshots");

                        // log.info("json : {}", json.toString(4));
                        docs.add(json);

                        String response =
                                webClient
                                        .post()
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .bodyValue(json.toString(4))
                                        .retrieve()
                                        .bodyToMono(String.class)
                                        .block();

                        log.info("ES response: {}", response);
                    } catch (Exception exception) {
                        log.error("error: {}", exception.getMessage());
                    }
                }
            } catch (IOException e) {
                log.error("error : {}", e.getMessage());
                throw new RuntimeException(e);
            }

            log.info("total docs: {}", docs.size());

        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
