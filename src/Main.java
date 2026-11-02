public class Main {
    public static void main(String[] args) {
        System.out.println("1. Создание счетов");
        BankAccount alice = new BankAccount("Alice", new Balance(100_00, Currency.RUB), 50_000);
        BankAccount bob = new BankAccount("Bob", new Balance(0, Currency.USD), 1_000_00); // Лимит побольше

        System.out.println(alice);
        System.out.println(bob);

        System.out.println("\n 2. Пополнение и снятие");
        alice = alice.deposit(150_00);
        System.out.println("Alice после пополнения: " + alice);

        alice = alice.withdraw(10_00);
        System.out.println("Alice после снятия: " + alice);

        System.out.println("\n3. Перевод с конвертацией");


        alice = alice.transferTo(bob, 50_00);
        bob = bob.receive(alice, 50_00);

        System.out.println("Alice после перевода: " + alice);
        System.out.println("Bob после получения: " + bob);

        System.out.println("\n4. Проверка корнер-кейсов (Ошибки)");


        try {
            alice.withdraw(60_000);
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка лимита: " + e.getMessage());
        }

        try {
            alice.withdraw(40_000);
        } catch (IllegalStateException e) {
            System.out.println("Ошибка баланса: " + e.getMessage());
        }

        try {
            alice.deposit(-500);
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка операции: " + e.getMessage());
        }

        BankAccount blockedAccount = new BankAccount("Blocked", new Balance(0, Currency.RUB), 100_000, true);
        try {
            alice.transferTo(blockedAccount, 100_00);
        } catch (IllegalStateException e) {
            System.out.println("Ошибка перевода: " + e.getMessage());
        }
    }
}