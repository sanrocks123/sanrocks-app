package com.sanrocks.tax;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.EqualsExclude;
import org.apache.commons.lang3.builder.HashCodeExclude;

@Data
@Builder
@EqualsAndHashCode
public class SaleTrade implements Comparable<SaleTrade> {

    @EqualsExclude
    @HashCodeExclude
    private int rowId;
    private String date;

    private String iSIN;
    private String symbol;
    private String description;

    private Integer quantity;
    private Double salePricePerUnit;
    private BigDecimal salesConsideration;
    private BigDecimal costOfAcquisition;
    private BigDecimal gains;

    public String toString() {
        return String.format("%4s %12s %13s %10s %100s %6s %10s %10s %10s", rowId, date, iSIN,
            symbol,
            description, quantity,
            costOfAcquisition, salesConsideration, gains);
    }

    @Override
    public int compareTo(final SaleTrade o) {
        return o.gains.compareTo(this.gains);
    }
}
