import java.util.List;

public class Order {
    private List<Item> items;
    private String customerName;
    private String customerEmail;
    private PriceCalculator priceCalculator;
    private NotificationService notificationService;

    public Order(List<Item> items, String customerName, String customerEmail, PriceCalculator priceCalculator, NotificationService notificationService) {
        this.items = items;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.priceCalculator = priceCalculator;
        this.notificationService = notificationService;
    }

    public double getTotalPrice() {
        return priceCalculator.calculateTotalPrice(items);
    }

    public void sendConfirmationEmail() {
        StringBuilder message = new StringBuilder("Your order details:\n");
        for (Item item : items) {
            message.append(item.getName()).append(" - ").append(item.getPrice()).append("\n");
        }
        message.append("Total: ").append(getTotalPrice());
        notificationService.sendOrderConfirmation(customerEmail, customerName, message.toString());
    }

    public void addItem(Item item) {
        items.add(item);
    }

    public void removeItem(Item item) {
        items.remove(item);
    }
}
