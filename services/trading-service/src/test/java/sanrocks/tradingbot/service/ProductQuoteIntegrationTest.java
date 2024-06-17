/* (C) 2023 */
package sanrocks.tradingbot.service;

import java.time.Duration;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import sanrocks.tradingbot.TradingBotServiceSelfHostApp;
import sanrocks.tradingbot.domain.ProductQuoteEvent;

@Disabled
@Slf4j
@SpringBootTest(classes = TradingBotServiceSelfHostApp.class)
public class ProductQuoteIntegrationTest {

    @Disabled
    @Test
    public void givenStreamAPI_whenItsCalled_thenConsumeDataInStream() throws InterruptedException {

        Flux<ProductQuoteEvent> productQuoteEventFlux =
                WebClient.builder()
                        .baseUrl("http://localhost:1000/v1/product-quote/stream")
                        .build()
                        .get()
                        .retrieve()
                        .bodyToFlux(ProductQuoteEvent.class);

        productQuoteEventFlux
                .delayElements(Duration.ofSeconds(2))
                .subscribe(
                        response -> {
                            log.info("response: {}", response);
                        });
    }
}
