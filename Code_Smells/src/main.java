import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        
        EmailSender emailSender = new EmailSender();
        NotificationService notificationService = new NotificationService(emailSender);
        PriceCalculator priceCalculator = new PriceCalculator();

        Item item1 = new Item("Book", 20, 1, DiscountType.AMOUNT, 5);
        Item item2 = new TaxableItem("Laptop", 1000, 1, DiscountType.PERCENTAGE, 0.1);
        Item item3 = new GiftCardItem("Gift Card", 10, 1, DiscountType.AMOUNT, 0);

        List<Item> items = new ArrayList<>();
        items.add(item1);
        items.add(item2);
        items.add(item3);

        Order order = new Order(items, "John Doe", "johndoe@example.com", priceCalculator, notificationService);

        System.out.println("Total Price: " + order.getTotalPrice());
        order.sendConfirmationEmail();

        order.printOrder();
    }
}
