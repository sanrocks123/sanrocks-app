package com.sanrocks.tax;

import java.io.IOException;
import java.net.URISyntaxException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class AISTradeBookReconciliationUtilTest {

    @Test
    public void calculateCapitalGains() throws IOException, URISyntaxException {
        new AisToTradeBookReconciliationUtil().calculateTax("/tax/2023-24/ais-trades.csv",
            "/tax/2023-24/broker-trades.txt");
    }

}
