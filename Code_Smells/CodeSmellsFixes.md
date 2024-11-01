
# Code Smells and Fixes

## 1. God Class in `Order`
Problem: The `Order` class handles multiple responsibilities, including price calculation, discount and tax application, email sending, and item management, making it complex and difficult to maintain.
Fix: Separate the responsibilities by creating new classes, such as `PriceCalculator` to handle price and discount calculations, `TaxService` for tax calculations, and `NotificationService` to handle email sending.

## 2. Long Method in `calculateTotalPrice`
Problem: The `calculateTotalPrice` method in `Order` is long and manages various calculations in one place, decreasing readability.
Fix: Break down the `calculateTotalPrice` method into smaller methods. 

## 3. Feature Envy in `Order` for `EmailSender`
Problem: The `Order` class directly calls `EmailSender` to send an email, mixing responsibilities.
Fix: Delegate the email sending responsibility to a `NotificationService` or similar class, allowing `Order` to focus solely on order-related logic.

## 4. Type Checking (`instanceof`) in `Order`
Problem: The `Order` class checks item types with `instanceof` to apply different behaviors, violating polymorphism.
Fix: Move specific behaviors, like tax calculation and discount eligibility, into the respective `Item` subclasses to allow each type of item to handle its own calculations.

## 5. Hardcoded Values
Problem: Hardcoded discount and tax values reduce flexibility and make maintenance difficult.
Fix: Replace these hardcoded values with constants or configuration parameters that can be easily modified.

### Summary
Refactoring will focus on separating concerns within `Order`, introducing a `Money` class for precision in financial calculations, improving method readability by breaking down long methods, and enhancing flexibility by avoiding hardcoded values.
