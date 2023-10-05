package sanrocks.tradingbot.rules;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class CompletableFutureTest {

    @Test
    public void test() {

        CompletableFuture<String> f1 =
                CompletableFuture.supplyAsync(
                        () -> {
                            log.info("f1");
                            try {
                                TimeUnit.SECONDS.sleep(3);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                            return "f1";
                        });

        CompletableFuture<String> f2 =
                CompletableFuture.supplyAsync(
                        () -> {
                            log.info("f2");
                            try {
                                TimeUnit.SECONDS.sleep(1);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                            return "f2";
                        });

        CompletableFuture<String> f3 =
                CompletableFuture.supplyAsync(
                        () -> {
                            log.info("f3");
                            try {
                                TimeUnit.SECONDS.sleep(2);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                            return "f3";
                        });

        CompletableFuture.allOf(f1, f2, f3);

        log.info("completed");
    }
}
