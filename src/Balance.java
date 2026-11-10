import java.util.Objects;

public final class Balance {

    private final long amountInKopecks;
    private final Currency currency;

    public Balance(long amountInKopecks, Currency currency) {
        this.amountInKopecks = amountInKopecks;
        this.currency = Objects.requireNonNull(currency, "Currency cannot be null");
    }


    public long getAmountInKopecks() {
        return amountInKopecks;
    }

    public Currency getCurrency() {
        return currency;
    }

    public Balance add(long kopecks) {
        if (kopecks < 0) {
            throw new IllegalArgumentException("Cannot add negative amount. Use subtract.");
        }
        return new Balance(Math.addExact(this.amountInKopecks, kopecks), currency);
    }

    public Balance subtract(long kopecks) {
        if (kopecks < 0) {
            throw new IllegalArgumentException("Cannot subtract negative amount. Use add.");
        }
        if (kopecks > this.amountInKopecks) {
            throw new IllegalStateException("Insufficient funds. Current: " + this.amountInKopecks + ", requested: " + kopecks);
        }
        return new Balance(this.amountInKopecks - kopecks, currency);
    }

    public Balance convertTo(Currency target) {
        Objects.requireNonNull(target, "Target currency cannot be null");
        if (target == currency) return this;

        long resultKopecks = Math.round(
                (double) amountInKopecks * currency.getPriceInRublesKopecks()
                        / target.getPriceInRublesKopecks()
        );
        return new Balance(resultKopecks, target);
    }

    @Override
    public String toString() {
        long whole = amountInKopecks / 100;
        long frac = Math.abs(amountInKopecks % 100);
        return String.format("%d.%02d %s", whole, frac, currency.getSymbol());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Balance other)) return false;
        return amountInKopecks == other.amountInKopecks && currency == other.currency;
    }

    @Override
    public int hashCode() {
        return Objects.hash(amountInKopecks, currency);
    }
}