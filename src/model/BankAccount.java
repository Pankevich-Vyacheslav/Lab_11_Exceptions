package model;

import exceptions.AccountException;

public class BankAccount {

    // Дані банківського рахунку
    private final String customerName;
    private final String userId;
    private final String password;
    private double balance;

    // Конструктор
    public BankAccount(String customerName, String userId, String password) {

        if (customerName == null || customerName.isBlank()) {
            throw new AccountException("Ім'я клієнта не може бути порожнім");
        }

        if (userId == null || userId.isBlank()) {
            throw new AccountException("userId не може бути порожнім");
        }

        // Пароль: мінімум 8 символів і хоча б одна цифра
        if (password == null || password.length() < 8 || password.chars().noneMatch(Character::isDigit)) {
            throw new AccountException("Пароль повинен містити мінімум 8 символів і цифру");
        }

        this.customerName = customerName;
        this.userId = userId;
        this.password = password;
        this.balance = 0;
    }

    // Гетери
    public String getCustomerName() {
        return customerName;
    }

    public String getUserId() {
        return userId;
    }

    public double getBalance() {
        return balance;
    }

    // Перевірка пароля
    public boolean checkPassword(String enteredPassword) {
        return password.equals(enteredPassword);
    }

    // Перевірка суми операції
    protected void validateAmount(double amount) {
        if (!Double.isFinite(amount) || amount <= 0) {
            throw new AccountException("Сума повинна бути більшою за 0");
        }
    }

    // Поповнення рахунку
    public void deposit(double amount) {
        validateAmount(amount);

        if (!Double.isFinite(balance + amount)) {
            throw new AccountException("Занадто велика сума поповнення");
        }

        balance += amount;
    }

    // Зняття коштів
    public void withdraw(double amount) {
        validateAmount(amount);

        if (amount > balance) {
            throw new AccountException("Недостатньо коштів на рахунку");
        }

        balance -= amount;
    }
}

