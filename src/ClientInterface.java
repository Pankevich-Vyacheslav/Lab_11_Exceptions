
import model.BankAccount;
import model.CheckingAccount;
import exceptions.AccountException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ClientInterface {

    public static void main(String[] args) throws IOException {
        ClientInterface clientInterface = new ClientInterface();
        clientInterface.start();
    }

    private final List<BankAccount> accounts = new ArrayList<>();

    private final Scanner scanner = new Scanner(System.in);

    private List<String> options = List.of(
            "1 - Create account",
            "2 - Delete account",
            "3 - Make account deposit",
            "4 - Make account withdrawal",
            "5 - Print account info",
            "6 - Exit"
    );

    public void start() throws IOException {

        String option;
        while (true) {
            printMainMenu();
            option = scanner.next();

            int choice;

            try {
                choice = Integer.parseInt(option);
            } catch (NumberFormatException e) {
                System.err.println("Error: Введіть число від 1 до 6");
                continue;
            }

            switch (choice) {
                case 1:
                    try {
                        addNewAccount();
                    } catch (Exception e) {
                        System.err.println("Error: " + e.getMessage() + "\n");
                        break;
                    }
                    break;
                case 2:
                    try {
                        deleteAccount();
                    } catch (AccountException e) {
                        System.err.println("Error: " + e.getMessage() + "\n");
                    }
                    break;
                case 3:
                    try {
                        deposit();
                    } catch (AccountException e) {
                        System.err.println("Error: " + e.getMessage() + "\n");
                    }
                    break;
                case 4:
                    try {
                        withdraw();
                    } catch (AccountException e) {
                        System.err.println("Error: " + e.getMessage() + "\n");
                    }
                    break;
                case 5:
                    try {
                        printInfo();
                    } catch (AccountException e) {
                        System.err.println("Error: " + e.getMessage() + "\n");
                    }
                    break;
                case 6:
                    return;
                default:
                    System.err.println("Error: Виберіть пункт від 1 до 6");
                    break;
            }
        }

    }

    private void printMainMenu() throws IOException {
        System.out.println("\n****************");
        System.out.println("Banking system: ");
        System.out.println("****************");
        options.forEach(System.out::println);
        System.out.print("\n>>> Choose your option: ");
    }

    private void addNewAccount() {

        System.out.println("\n****************");
        System.out.println("Creating new account: ");
        System.out.println("****************");

        // Введення даних клієнта
        System.out.print(">>> Enter customer name: ");
        String name = scanner.next();

        System.out.print(">>> Enter user id: ");
        String userId = scanner.next();

        System.out.print(">>> Enter user password: ");
        String password = scanner.next();

        System.out.print(">>> Confirm password: ");
        String confirmPassword = scanner.next();

        // Перевірка унікальності userId
        boolean exists = accounts.stream()
                .anyMatch(account -> account.getUserId().equals(userId));

        if (exists) {
            throw new AccountException("Рахунок із таким userId вже існує");
        }

        // Перевірка підтвердження пароля
        if (!password.equals(confirmPassword)) {
            throw new AccountException("Пароль та підтвердження не збігаються");
        }

        // Створення нового поточного рахунку
        CheckingAccount account = new CheckingAccount(name, userId, password);

        accounts.add(account);

        System.out.println("\n Рахунок успішно створено!");
    }

    private void deleteAccount() {

        System.out.println("\n****************");
        System.out.println("Deleting account: ");
        System.out.println("****************");

        System.out.print(">>> Enter user id: ");
        String userId = scanner.next();

        System.out.print(">>> Enter user password: ");
        String password = scanner.next();

        // Пошук рахунку за userId
        BankAccount account = accounts.stream()
                .filter(a -> a.getUserId().equals(userId))
                .findFirst()
                .orElseThrow(() -> new AccountException("Рахунок із таким userId не знайдено"));

        // Перевірка пароля
        if (!account.checkPassword(password)) {
            throw new AccountException("Неправильний пароль");
        }

        // Перевірка балансу
        if (account.getBalance() != 0) {
            throw new AccountException("Неможливо видалити рахунок із ненульовим балансом");
        }

        // Видалення рахунку
        accounts.remove(account);

        System.out.println("\n Рахунок успішно видалено!");
    }

    private void deposit() {

        System.out.println("\n****************");
        System.out.println("Making a deposit: ");
        System.out.println("****************");

        System.out.print(">>> Enter user id: ");
        String userId = scanner.next();

        System.out.print(">>> Enter user password: ");
        String password = scanner.next();

        // Пошук рахунку
        BankAccount account = accounts.stream()
                .filter(a -> a.getUserId().equals(userId))
                .findFirst()
                .orElseThrow(() -> new AccountException("Рахунок із таким userId не знайдено"));

        // Перевірка пароля
        if (!account.checkPassword(password)) {
            throw new AccountException("Неправильний пароль");
        }

        // Введення суми
        System.out.print(">>> Enter amount: ");
        String input = scanner.next();

        double amount;
        try {
            amount = Double.parseDouble(input);
        } catch (NumberFormatException e) {
            throw new AccountException("Некоректний формат суми");
        }

        // Поповнення рахунку
        account.deposit(amount);

        System.out.println("\n Рахунок успішно поповнено!");
        System.out.println("\n Поточний баланс: " + account.getBalance());
    }

    private void withdraw() {

        // Введення даних користувача
        System.out.print(">>> Enter user id: ");
        String userId = scanner.next();

        System.out.print(">>> Enter user password: ");
        String password = scanner.next();

        // Пошук банківського рахунку
        BankAccount account = accounts.stream()
                .filter(a -> a.getUserId().equals(userId))
                .findFirst()
                .orElseThrow(() -> new AccountException("Рахунок із таким userId не знайдено"));

        // Перевірка пароля
        if (!account.checkPassword(password)) {
            throw new AccountException("Неправильний пароль");
        }

        // Введення суми зняття
        System.out.print(">>> Enter amount: ");
        String input = scanner.next();

        double amount;
        try {
            amount = Double.parseDouble(input);
        } catch (NumberFormatException e) {
            throw new AccountException("Некоректний формат суми зняття");
        }

        // Зняття коштів із перевірками
        account.withdraw(amount);

        System.out.println("\n Кошти успішно знято!");
        System.out.println("\n Поточний баланс: " + account.getBalance());
    }

    private void printInfo() {

        System.out.println("\n****************");
        System.out.println("Printing account info: ");
        System.out.println("****************");

        System.out.print(">>> Enter user id: ");
        String userId = scanner.next();

        System.out.print(">>> Enter user password: ");
        String password = scanner.next();

        // Пошук рахунку
        BankAccount account = accounts.stream()
                .filter(a -> a.getUserId().equals(userId))
                .findFirst()
                .orElseThrow(() -> new AccountException("Рахунок із таким userId не знайдено"));

        // Перевірка пароля
        if (!account.checkPassword(password)) {
            throw new AccountException("Неправильний пароль");
        }

        // Виведення інформації про рахунок
        System.out.println("\n Account info:");
        System.out.println(" - customer name: " + account.getCustomerName());
        System.out.println(" - user id: " + account.getUserId());
        System.out.println(" - account balance: " + account.getBalance());

        // Інформація про поточний рахунок
        if (account instanceof CheckingAccount checking) {
            System.out.println(" - account type: Checking");
            System.out.println(" - account daily withdrawal limit: " + checking.getDailyWithdrawalLimit());
        }

        System.out.println("\n Інформацію успішно отримано!");
    }
}