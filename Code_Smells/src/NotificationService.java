public class NotificationService {
    private EmailSender emailSender;

    public NotificationService(EmailSender emailSender) {
        this.emailSender = emailSender;
    }

    public void sendOrderConfirmation(String customerEmail, String customerName, String message) {
        String subject = "Order Confirmation";
        String body = "Thank you for your order, " + customerName + "!\n\n" + message;
        emailSender.sendEmail(customerEmail, subject, body);
    }
}
