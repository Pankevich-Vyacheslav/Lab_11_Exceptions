package model;

import exceptions.AccountException;
import java.time.LocalDate;

public class CheckingAccount extends BankAccount {

    // Денний ліміт зняття коштів
    private double dailyWithdrawalLimit = 300.0;

    // Загальна сума зняття за поточний день
    private double withdrawnToday = 0.0;

    // Дата останнього обліку зняття
    private LocalDate lastWithdrawalDate = LocalDate.now();

    // Конструктор
    public CheckingAccount(String customerName, String userId, String password) {
        super(customerName, userId, password);
    }

    // Отримання денного ліміту
    public double getDailyWithdrawalLimit() {
        return dailyWithdrawalLimit;
    }

    // Встановлення денного ліміту
    public void setDailyWithdrawalLimit(double limit) {
        if (!Double.isFinite(limit) || limit <= 0) {
            throw new AccountException("Денний ліміт повинен бути більшим за 0");
        }

        this.dailyWithdrawalLimit = limit;
    }

    // Перевизначення методу зняття коштів
    @Override
    public void withdraw(double amount) {

        // Перевірка правильності суми
        validateAmount(amount);

        // Перевірка зміни календарного дня
        LocalDate today = LocalDate.now();

        if (!today.equals(lastWithdrawalDate)) {
            withdrawnToday = 0.0;
            lastWithdrawalDate = today;
        }

        // Перевірка денного ліміту
        if (amount > dailyWithdrawalLimit - withdrawnToday) {
            throw new AccountException("Перевищено денний ліміт зняття коштів");
        }

        // Зняття через базовий клас
        super.withdraw(amount);

        // Облік успішного зняття
        withdrawnToday += amount;
    }
}

