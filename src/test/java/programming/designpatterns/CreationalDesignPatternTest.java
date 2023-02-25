/* (C) 2017 */
package programming.designpatterns;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Java Source CreationalDesignPatternTest.java created on Nov 27, 2019
 *
 * @author : Sanjeev Saxena
 * @email : sanrocks123@gmail.com
 * @version : 1.0
 */
public class CreationalDesignPatternTest {

    private static final Logger log = LoggerFactory.getLogger(CreationalDesignPatternTest.class);

    @Test
    public void testSingleton() throws InterruptedException {

        final ExecutorService execService = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 5; i++) {
            execService.execute(
                    () -> {
                        Singleton.getInstance();
                    });
        }
        execService.shutdown();
        execService.awaitTermination(5, TimeUnit.MINUTES);
    }

    @Test
    // @Ignore
    public void testSingletonEnum() throws InterruptedException {

        final ExecutorService execService = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 5; i++) {
            execService.execute(
                    () -> {
                        log.info("started");
                        SingletonEnum.INSTANCE.getAtomicInteger();
                    });
        }
        execService.shutdown();
        execService.awaitTermination(5, TimeUnit.MINUTES);
    }
}
