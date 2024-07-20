package com.sanrocks.tax;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Builder
@EqualsAndHashCode
public class InflatedGainTrade implements Comparable<InflatedGainTrade> {

    private SaleTrade source;
    private SaleTrade target;
    private BigDecimal gains;

    @Override
    public int compareTo(final InflatedGainTrade o) {
        return o.gains.compareTo(this.gains);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("\n");
        sb.append(
            String.format("%13s %10s %100s %6s %10s %10s %10s", source.getISIN(),
                source.getSymbol(), source.getDescription(),
                source.getQuantity(), source.getCostOfAcquisition(), source.getSalesConsideration(),
                source.getGains()));
        sb.append("\n");

        sb.append(
            String.format("%13s %10s %100s %6s %10s %10s %10s", target.getISIN(),
                target.getSymbol(), target.getDescription(),
                target.getQuantity(), target.getCostOfAcquisition(), target.getSalesConsideration(),
                target.getGains()));
        sb.append("\n");

        sb.append(
            String.format("%13s %10s %100s %6s %10s %10s %10s", "", "", "", "", "", "",
                getGains()));

        sb.append("\n");

        return sb.toString();
    }
}
