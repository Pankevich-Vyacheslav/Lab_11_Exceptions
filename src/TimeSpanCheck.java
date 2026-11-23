
import exceptions.IllegalInputTimeSpanException;
import exceptions.NegativeTimeSpanException;
import exceptions.IllegalScaleFactorException;

public class TimeSpanCheck {

    public static void main(String[] args) {

        // 1. Перевірка правильного масштабування
        TimeSpan time = new TimeSpan(1, 30);
        time.scale(2);

        System.out.println("Результат: "
                + time.getHours() + " год "
                + time.getMinutes() + " хв");

        // 2. Некоректний вхідний інтервал
        try {
            new TimeSpan(-1, 20);
        } catch (IllegalInputTimeSpanException e) {
            System.out.println("Помилка введення: " + e.getMessage());
        }

        // 3. Від'ємний результат віднімання
        try {
            TimeSpan span = new TimeSpan(1, 20);
            span.subtract(2, 0);
        } catch (NegativeTimeSpanException e) {
            System.out.println("Помилка віднімання: " + e.getMessage());
        }

        // 4. Некоректний множник
        try {
            TimeSpan span = new TimeSpan(2, 15);
            span.scale(0);
        } catch (IllegalScaleFactorException e) {
            System.out.println("Помилка масштабування: " + e.getMessage());
        }
    }
}