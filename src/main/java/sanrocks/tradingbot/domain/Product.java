/* (C) 2023 */
package sanrocks.tradingbot.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Data;
import org.json.JSONObject;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Java Source Product created on 12/23/2021
 *
 * @author : Sanjeev Saxena
 * @version : 1.0
 * @email : sanrocks123@gmail.com
 */
@Data
@Document
@JsonIgnoreProperties(ignoreUnknown = true)
public class Product implements Serializable {

    private static final long SerialVersionUID = 10l;
    private static final int CLOSING_POSITION_MARGIN = 5;

    @Id private String productId;

    @NotBlank
    @Size(min = 0, max = 20)
    private String productName;

    private BigDecimal currentPrice;
    private BigDecimal buyPrice;
    private BigDecimal upperLimitSellPrice;
    private BigDecimal lowerLimitSellPrice;

    private List<BigDecimal> priceChangeList = new ArrayList<>(500);
    private List<Trade> trades = new ArrayList<>();
    private List<String> openPositionIds = new CopyOnWriteArrayList<>();
    private List<String> errors = new ArrayList<>();

    public Product() {}

    public Product(String productName) {
        this.productName = productName;
    }

    /**
     * @param productId
     * @param productName
     * @param buyPrice
     * @param lowerLimitSellPrice
     * @param upperLimitSellPrice
     */
    public Product(
            String productId,
            String productName,
            BigDecimal buyPrice,
            BigDecimal lowerLimitSellPrice,
            BigDecimal upperLimitSellPrice) {
        this.productId = productId;
        this.productName = productName;
        this.buyPrice = buyPrice.setScale(2, RoundingMode.HALF_DOWN);
        this.lowerLimitSellPrice = lowerLimitSellPrice.setScale(2, RoundingMode.HALF_DOWN);
        this.upperLimitSellPrice = upperLimitSellPrice.setScale(2, RoundingMode.HALF_DOWN);
    }

    /**
     * @return
     */
    public boolean isTargetBuyPrice() {
        if (null == getCurrentPrice()) {
            return false;
        }
        return getCurrentPrice().equals(getBuyPrice());
    }

    /**
     * @return
     */
    public boolean isTargetSellPrice() {
        if (null == getCurrentPrice()) {
            return false;
        }
        return getCurrentPrice().equals(getLowerLimitSellPrice())
                || getCurrentPrice().equals(getUpperLimitSellPrice());
    }

    /**
     * @param trade
     */
    public void addTrade(Trade trade) {
        trades.add(trade);
        if (trade.getPositionType().equalsIgnoreCase("OPEN")) {
            openPositionIds.add(trade.getPositionId());
        } else if (trade.getPositionType().equalsIgnoreCase("CLOSE")) {
            openPositionIds.remove(trade.getPositionId());
        }
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BigDecimal getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(BigDecimal buyPrice) {
        this.buyPrice = buyPrice;
        BigDecimal margin = getClosingPositionMarginAmount(getBuyPrice());
        setLowerLimitSellPrice(getBuyPrice().subtract(margin));
        setUpperLimitSellPrice(getBuyPrice().add(margin));
    }

    /**
     * @param buyPrice
     * @return
     */
    private BigDecimal getClosingPositionMarginAmount(final BigDecimal buyPrice) {
        double value = (CLOSING_POSITION_MARGIN * (buyPrice.floatValue() / 100.0f));
        return new BigDecimal(value).setScale(2, RoundingMode.HALF_DOWN);
    }

    public BigDecimal getUpperLimitSellPrice() {
        return upperLimitSellPrice;
    }

    public void setUpperLimitSellPrice(BigDecimal upperLimitSellPrice) {
        this.upperLimitSellPrice = upperLimitSellPrice;
    }

    public BigDecimal getLowerLimitSellPrice() {
        return lowerLimitSellPrice;
    }

    public void setLowerLimitSellPrice(BigDecimal lowerLimitSellPrice) {
        this.lowerLimitSellPrice = lowerLimitSellPrice;
    }

    public String toString() {
        return new JSONObject(this).toString();
    }

    public String toString(int indent) {
        return new JSONObject(this).toString(indent);
    }

    public BigDecimal getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(BigDecimal currentPrice) {
        this.currentPrice = currentPrice;
        if (!Objects.isNull(currentPrice)) {
            this.getPriceChangeList().add(currentPrice);
        }
    }

    public List<String> getOpenPositionIds() {
        return openPositionIds;
    }

    public List<Trade> getTrades() {
        return trades;
    }

    public void addError(String error) {
        errors.add(error);
    }

    public List<String> getErrors() {
        return errors;
    }

    public List<BigDecimal> getPriceChangeList() {
        return priceChangeList;
    }

    public void setPriceChangeList(final List<BigDecimal> priceChangeList) {
        this.priceChangeList = priceChangeList;
    }

    public long getTargetBuyPriceMatchCount() {
        return priceChangeList.stream().filter(p -> buyPrice.equals(p)).count();
    }
}
