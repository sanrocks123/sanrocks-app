package com.sanrocks.tax;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AisToTradeBookReconciliationUtil {

    public static final int INFLATED_GAIN_THRESHOLD = 5000;

    public void calculateTax(final String aisTradeBook, final String brokerTradeBook)
        throws IOException, URISyntaxException {

        List<SaleTrade> aisTradeList = readAisTrades(aisTradeBook);

        SaleTrade itemizedAis = aggregateItemizedAis(aisTradeList);

        findZeroCostOfAcquisitionTrades(aisTradeList);

        List<SaleTrade> brokerTradeList = readBrokerTrades(brokerTradeBook);

        Map<String, SaleTrade> aisStock2TradeMap = getStock2SaleTradeMap(aisTradeList);
        Map<String, SaleTrade> brokerStock2TradeMap = getStock2SaleTradeMap(brokerTradeList);

        applyNormalization(aisStock2TradeMap, brokerStock2TradeMap);

        printGroupByStockSummary("AIS Trade Book", aisStock2TradeMap);
        printGroupByStockSummary("Broker Trade Book", brokerStock2TradeMap);

        BigDecimal inflatedGains = getInflatedGains(aisStock2TradeMap, brokerStock2TradeMap);
        printSummary(itemizedAis, inflatedGains);
    }

    private void printSummary(final SaleTrade itemizedAis, final BigDecimal inflatedGains) {
        StringBuilder sbx = new StringBuilder("\n\n");
        sbx.append("Sales Consideration\t: ").append(itemizedAis.getSalesConsideration())
            .append("\n");
        sbx.append("Cost of Acquisition\t: ").append(itemizedAis.getCostOfAcquisition())
            .append("\n");
        sbx.append("Gains as per AIS\t: ").append(itemizedAis.getGains()).append("\n");
        sbx.append("Inflated Gains\t: ").append(inflatedGains).append("\n");
        sbx.append("Actual Gains\t: ").append(itemizedAis.getGains().subtract(inflatedGains))
            .append("\n");

        log.info("SUMMARY: {}", sbx);
    }

    private SaleTrade aggregateItemizedAis(final List<SaleTrade> aisTradeList) {
        SaleTrade itemizedAis = SaleTrade.builder().rowId(9999)
            .date("")
            .iSIN("")
            .symbol("")
            .description("Total AIS itemized sale of shares")
            .quantity(0)
            .salesConsideration(new BigDecimal(0))
            .costOfAcquisition(new BigDecimal(0)).gains(new BigDecimal(0)).build();

        StringBuilder sb = new StringBuilder("\n\n");
        for (SaleTrade saleTrade : aisTradeList) {
            sb.append(saleTrade).append("\n");

            itemizedAis.setQuantity(itemizedAis.getQuantity() + saleTrade.getQuantity());
            itemizedAis.setSalesConsideration(
                saleTrade.getSalesConsideration().add(itemizedAis.getSalesConsideration()));
            itemizedAis.setCostOfAcquisition(
                saleTrade.getCostOfAcquisition().add(itemizedAis.getCostOfAcquisition()));
            itemizedAis.setGains(saleTrade.getGains().add(itemizedAis.getGains()));
        }

        sb.append("\n").append(itemizedAis);
        log.debug("Aggregated Summary - Total AIS itemized sale of shares{}\n", sb);
        log.info("Aggregated Summary - Itemized AIS\n\n{}\n", itemizedAis);
        return itemizedAis;
    }

    private List<SaleTrade> readAisTrades(final String aisTradeBook)
        throws URISyntaxException, IOException {
        final List<SaleTrade> aisTradeList = new ArrayList<>();
        try (Stream<String> linesStream = Files.lines(Paths.get(getUri(aisTradeBook)))) {
            AtomicInteger counter = new AtomicInteger(0);
            linesStream.forEach(line -> {

                if (counter.get() == 0) {
                    log.debug("header row found, skipping");
                    counter.incrementAndGet();
                    return;
                }

                List<String> tokens = Arrays.stream(line.split("\",\""))
                    .map(l -> l.replaceAll(",", "")).toList();

                SaleTrade aisTradeItem = SaleTrade.builder().rowId(counter.get())
                    .date(tokens.get(3))
                    .iSIN(tokens.get(4))
                    .symbol("")
                    .description(tokens.get(5))
                    .quantity(Double.valueOf(tokens.get(10)).intValue())
                    .salesConsideration(new BigDecimal(tokens.get(12)))
                    .costOfAcquisition(new BigDecimal(tokens.get(13)))
                    .gains(new BigDecimal(tokens.get(12)).subtract(
                        new BigDecimal(tokens.get(13))))
                    .build();
                aisTradeList.add(aisTradeItem);
                counter.incrementAndGet();
            });
        }
        return aisTradeList;
    }

    private void findZeroCostOfAcquisitionTrades(
        final List<SaleTrade> aisTradeList) {

        final StringBuilder sb = new StringBuilder();
        sb.append("\n\n");
        SaleTrade sharesWithZeroCostOfAcquisition = SaleTrade.builder().rowId(9999)
            .date("")
            .iSIN("")
            .symbol("")
            .description("tradesWithZeroCostOfAcquisition")
            .quantity(0)
            .salesConsideration(new BigDecimal(0))
            .costOfAcquisition(new BigDecimal(0)).gains(new BigDecimal(0)).build();

        aisTradeList.stream()
            .filter(a -> a.getCostOfAcquisition().compareTo(new BigDecimal(0)) == 0)
            .forEach(saleTrade -> {
                sb.append(saleTrade).append("\n");

                sharesWithZeroCostOfAcquisition.setSalesConsideration(
                    sharesWithZeroCostOfAcquisition.getSalesConsideration()
                        .add(saleTrade.getSalesConsideration()));
                sharesWithZeroCostOfAcquisition.setCostOfAcquisition(
                    sharesWithZeroCostOfAcquisition.getCostOfAcquisition()
                        .add(saleTrade.getCostOfAcquisition()));

                sharesWithZeroCostOfAcquisition.setGains(
                    sharesWithZeroCostOfAcquisition.getGains().add(saleTrade.getGains()));
                sharesWithZeroCostOfAcquisition.setQuantity(
                    sharesWithZeroCostOfAcquisition.getQuantity() + saleTrade.getQuantity());

            });

        sb.append("\n").append(sharesWithZeroCostOfAcquisition);

        //itemizedAis.setGains(
        //itemizedAis.getSalesConsideration().subtract(itemizedAis.getCostOfAcquisition())
        //.subtract(sharesWithZeroCostOfAcquisition.getGains()));

        //itemizedAis.setDescription(
        //"Actual capital gains after deducting ZERO cost of acquisition shares");

        log.info("Aggregated Summary - applyZeroCostOfAcquisition{}\n\n", sb);
    }

    private void applyNormalization(final Map<String, SaleTrade> aisStock2TradeMap,
        final Map<String, SaleTrade> brokerStock2TradeMap) {

        Map<String, String> tradeBook2AisISINMap = new HashMap<>();
        tradeBook2AisISINMap.put("INE299N01021", "INE299N01013");
        tradeBook2AisISINMap.put("INE056I01025", "INE056I01017");
        tradeBook2AisISINMap.put("INE549S01036", "INE549S01028");
        tradeBook2AisISINMap.put("INE704P01025", "INE704P01017");

        brokerStock2TradeMap.values().forEach(b -> {
            if (!aisStock2TradeMap.containsKey(b.getISIN())) {
                SaleTrade currentAisSaleTrade = aisStock2TradeMap.get(
                    tradeBook2AisISINMap.get(b.getISIN()));

                if (Objects.isNull(currentAisSaleTrade)) {
                    log.info("broker security: {} {} not found in AIS", b.getISIN(), b.getSymbol());
                    return;
                }

                aisStock2TradeMap.remove(currentAisSaleTrade.getISIN());
                currentAisSaleTrade.setISIN(b.getISIN());
                aisStock2TradeMap.computeIfAbsent(b.getISIN(), v -> currentAisSaleTrade);
            }
        });

        StringBuilder sb = new StringBuilder("\n\n");

        sb.append("aisStock2TradeMap").append("\n");
        aisStock2TradeMap.values().forEach(s -> {
            if (brokerStock2TradeMap.containsKey(s.getISIN())) {
                s.setSymbol(brokerStock2TradeMap.get(s.getISIN()).getSymbol());
                return;
            }
            sb.append(s).append("\n");
            s.setSymbol("");
        });

        sb.append("\n\nbrokerStock2TradeMap").append("\n");
        brokerStock2TradeMap.values().forEach(s -> {
            if (aisStock2TradeMap.containsKey(s.getISIN())) {
                s.setDescription(aisStock2TradeMap.get(s.getISIN()).getDescription());
                return;
            }
            sb.append(s).append("\n");
            s.setDescription("");
        });
        log.debug("updateMissingFields, {}", sb);
    }

    @SneakyThrows
    private List<SaleTrade> readBrokerTrades(final String brokerTradeBookFile) {

        List<SaleTrade> brokerTradeList = new ArrayList<>();
        AtomicInteger row = new AtomicInteger();
        try (Stream<String> linesStream = Files.lines(
            Paths.get(getUri(brokerTradeBookFile)))) {
            linesStream.forEach(line -> {

                if (row.get() == 0) {
                    log.debug("broker header row found, skipping");
                    row.incrementAndGet();
                    return;
                }

                String[] tokens = line.split("\t");
                SaleTrade saleTrade = SaleTrade.builder()
                    .rowId(row.incrementAndGet())
                    .date(tokens[3])
                    .symbol(tokens[0])
                    .iSIN(tokens[1])
                    .description(tokens[0])
                    .quantity(Integer.valueOf(tokens[4]))
                    .costOfAcquisition(new BigDecimal(tokens[5]))
                    .salesConsideration(new BigDecimal(tokens[6]))
                    .gains(new BigDecimal(tokens[7])).build();
                brokerTradeList.add(saleTrade);
            });
        }

        StringBuilder sb = new StringBuilder("\n");
        brokerTradeList.forEach(s ->
            sb.append(s).append("\n")
        );

        SaleTrade brokerTradeBook = SaleTrade.builder().rowId(9999)
            .date("")
            .description("")
            .quantity(0)
            .salesConsideration(new BigDecimal(0))
            .costOfAcquisition(new BigDecimal(0)).gains(new BigDecimal(0)).build();

        for (SaleTrade saleTrade : brokerTradeList) {
            brokerTradeBook.setQuantity(brokerTradeBook.getQuantity() + saleTrade.getQuantity());
            brokerTradeBook.setSalesConsideration(
                saleTrade.getSalesConsideration().add(brokerTradeBook.getSalesConsideration()));
            brokerTradeBook.setCostOfAcquisition(
                saleTrade.getCostOfAcquisition().add(brokerTradeBook.getCostOfAcquisition()));
            brokerTradeBook.setGains(saleTrade.getGains().add(brokerTradeBook.getGains()));
        }

        brokerTradeBook.setDescription(
            String.format("Aggregated Summary - Broker Stocks, count: %s",
                brokerTradeList.size()));
        sb.append("\n").append(brokerTradeBook).append("\n");
        log.debug("Broker Trade Book: \n{}", sb);

        return brokerTradeList;
    }

    private BigDecimal getInflatedGains(final Map<String, SaleTrade> aisStock2TradeMap,
        final Map<String, SaleTrade> brokerTradeMap) {
        StringBuilder sb = new StringBuilder("\n\n");

        List<SaleTrade> tradeNotMatch = new ArrayList<>();
        AtomicInteger tradeMatched = new AtomicInteger();
        AtomicInteger aisGainsMoreThanTradeBook = new AtomicInteger();
        SaleTrade inflatedGains = SaleTrade.builder().gains(new BigDecimal(0)).build();
        List<InflatedGainTrade> inflatedGainTradeList = new ArrayList<>();

        aisStock2TradeMap.forEach((iSIN, aisTradeEntry) -> {

            if (brokerTradeMap.containsKey(iSIN)) {
                tradeMatched.incrementAndGet();
                SaleTrade zTradeEntry = brokerTradeMap.get(iSIN);

                if (aisTradeEntry.getGains().subtract(zTradeEntry.getGains()).intValue()
                    >= INFLATED_GAIN_THRESHOLD) {
                    aisGainsMoreThanTradeBook.incrementAndGet();

                    BigDecimal runningGains = inflatedGains.getGains();
                    BigDecimal currentGain = aisTradeEntry.getGains()
                        .subtract(zTradeEntry.getGains());
                    inflatedGains.setGains(runningGains.add(currentGain));

                    inflatedGainTradeList.add(
                        InflatedGainTrade.builder().source(zTradeEntry).target(aisTradeEntry)
                            .gains(currentGain).build());
                }
                return;
            }
            tradeNotMatch.add(aisTradeEntry);
        });

        inflatedGainTradeList.stream().sorted().forEach(sb::append);
        log.info("Trade Matching Result, AisGain > TradeBookGain Threshold: {} {}",
            INFLATED_GAIN_THRESHOLD, sb);

        StringBuilder aisTradeNotFound = new StringBuilder("\n");
        tradeNotMatch.forEach(t -> {
            aisTradeNotFound.append(t).append("\n");
        });

        if (!tradeNotMatch.isEmpty()) {
            log.info("AIS trade not found in Trade Book, count: [{}], trades: \n{}",
                tradeNotMatch.size(), aisTradeNotFound);
        }

        sb.setLength(0);
        sb.append("Stats\n\n");
        sb.append("totalStocks \t\t: ").append((tradeMatched.get() + tradeNotMatch.size()))
            .append("\n");
        sb.append("tradeMatched \t\t: ").append(tradeMatched.get()).append("\n");
        sb.append("tradeNotMatch \t\t: ").append(tradeNotMatch.size()).append("\n");
        sb.append("aisGains > TradeBook \t\t: ").append(aisGainsMoreThanTradeBook.get())
            .append("\n");
        sb.append("inflatedGains \t\t: ").append(inflatedGains.getGains()).append("\n");

        log.info("{}", sb);

        return inflatedGains.getGains();
    }

    private URI getUri(final String filepath) throws URISyntaxException {
        return Objects.requireNonNull(
                AisToTradeBookReconciliationUtil.class.getResource(filepath))
            .toURI();
    }

    private Map<String, SaleTrade> getStock2SaleTradeMap(
        final List<SaleTrade> saleTradeList) {
        Map<String, SaleTrade> stock2SaleTradeMap = new HashMap<>();

        saleTradeList.forEach((trade) -> {
            String stockName = trade.getISIN();

            stock2SaleTradeMap.computeIfPresent(stockName, (k, v) -> {
                v.setQuantity(trade.getQuantity() + v.getQuantity());
                v.setSalesConsideration(
                    trade.getSalesConsideration().add(v.getSalesConsideration()));
                v.setCostOfAcquisition(trade.getCostOfAcquisition().add(v.getCostOfAcquisition()));
                v.setGains(trade.getGains().add(v.getGains()));
                return v;
            });

            stock2SaleTradeMap.computeIfAbsent(stockName, stockDetails -> trade);
        });

        log.debug("stock2SaleTradeMap: {}, keyset: {}", stock2SaleTradeMap.size(),
            stock2SaleTradeMap.keySet());

        return stock2SaleTradeMap;
    }

    private void printGroupByStockSummary(final String message,
        final Map<String, SaleTrade> stock2SaleTradeMap) {
        StringBuilder sb = new StringBuilder("\n");
        stock2SaleTradeMap.values().stream().sorted().forEach(s -> {
            sb.append(s.toString()).append("\n");
        });

        SaleTrade summary = SaleTrade.builder().rowId(9999).date("")
            .description("Aggregated Summary - Group By ISINs, sort order GAINs")
            .quantity(0)
            .salesConsideration(new BigDecimal(0))
            .costOfAcquisition(new BigDecimal(0))
            .gains(new BigDecimal(0))
            .build();

        for (SaleTrade saleTrade : stock2SaleTradeMap.values()) {
            summary.setQuantity(summary.getQuantity() + saleTrade.getQuantity());
            summary.setSalesConsideration(
                summary.getSalesConsideration().add(saleTrade.getSalesConsideration()));
            summary.setCostOfAcquisition(
                summary.getCostOfAcquisition().add(saleTrade.getCostOfAcquisition()));
            summary.setGains(summary.getGains().add(saleTrade.getGains()));
        }
        summary.setDescription(
            String.format("Aggregated Summary - Group By Stocks, unique stocks: %s",
                stock2SaleTradeMap.size()));

        sb.append("\n\n").append(summary).append("\n\n");

        log.info("{}, Group By Stocks: \n{}", message, sb);
    }


}
