package pl.tym.stock.sales.offerting;

import java.math.BigDecimal;
import java.util.List;

public class Offer {

    private final BigDecimal total;
    private final List<OfferLine> offerLines;

    private Offer(BigDecimal total, List<OfferLine> itemsCount) {
        this.total = total;
        this.offerLines = itemsCount;
    }

    public static Offer of(BigDecimal total, List<OfferLine> itemsCount) {
        return new Offer(total, itemsCount);
    }

    public BigDecimal getTotal() {
        return total;
    }

    public int getLinesCount() {
        return offerLines.size();
    }
}
