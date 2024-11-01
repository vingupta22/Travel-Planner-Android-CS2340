import java.util.List;

public class PriceCalculator {
    public double calculateTotalPrice(List<Item> items) {
        double total = 0.0;
        for (Item item : items) {
            double price = item.getPrice();
            price -= item.getDiscountAmount() * (item.getDiscountType() == DiscountType.PERCENTAGE ? price : 1);
            total += price * item.getQuantity();
            if (item instanceof TaxableItem) {
                TaxableItem taxableItem = (TaxableItem) item;
                total += taxableItem.getTaxRate() / 100.0 * price;
            }
        }
        return applyOrderDiscount(total, items);
    }

    private double applyOrderDiscount(double total, List<Item> items) {
        if (items.stream().anyMatch(item -> item instanceof GiftCardItem)) {
            total -= 10.0;
        }
        if (total > 100.0) {
            total *= 0.9;
        }
        return total;
    }
}
