
import exceptions.IllegalInputTimeSpanException;
import exceptions.NegativeTimeSpanException;
import exceptions.IllegalScaleFactorException;

// 1. Конструктори
public class TimeSpan {
    private int hours;
    private int minutes;

    // конструктор без аргументів (тобто 0 год 0 хв)
    public TimeSpan() {
        this(0, 0);
    }

    // конструктор з одним аргументом
    public TimeSpan(int minutes) {
        if (minutes < 0) {
            throw new IllegalInputTimeSpanException("Кількість хвилин не може бути від'ємною");
        }
        this.hours = minutes / 60;
        this.minutes = minutes % 60;
    }

    // конструктор з двома аргументами
    public TimeSpan(int hours, int minutes) {
        if (hours < 0) {
            throw new IllegalInputTimeSpanException("Години не можуть бути від'ємними");
        }
        if (minutes < 0 || minutes > 59) {
            throw new IllegalInputTimeSpanException("Хвилини повинні бути в діапазоні від 0 до 59");
        }
        this.hours = hours;
        this.minutes = minutes;
    }

    // конструктор копіювання
    public TimeSpan(TimeSpan span) {
        if (span == null) {
            throw new IllegalInputTimeSpanException("Об'єкт TimeSpan не може бути null");
        }
        this.hours = span.getHours();
        this.minutes = span.getMinutes();
    }

    // 2. Методи отримання значень
    // Гетери
    // повертає кіл. повних годин
    public int getHours() {
        return hours;
    }

    // повертає хв. частинну інтервалу (0-59)
    public int getMinutes() {
        return minutes;
    }

    // 3. Перевантажені методи add()
    // додавання годин та хвилин
    public void add(int hours, int minutes) {
        if (hours < 0) {
            throw new IllegalInputTimeSpanException("Години не можуть бути від'ємними");
        }
        if (minutes < 0 || minutes > 59) {
            throw new IllegalInputTimeSpanException("Хвилини повинні бути в діапазоні від 0 до 59");
        }

        this.hours += hours;
        this.minutes += minutes;

        // нормалізація хвилин
        if (this.minutes >= 60) {
            this.hours += this.minutes / 60;
            this.minutes = this.minutes % 60;
        }
    }

    // додавання хвилин
    public void add(int minutes) {
        if (minutes < 0) {
            throw new IllegalInputTimeSpanException("Кількість хвилин не може бути від'ємною");
        }
        add(minutes / 60, minutes % 60);
    }

    // додавання іншого TimeSpan
    public void add(TimeSpan span) {
        if (span == null) {
            throw new IllegalInputTimeSpanException("Об'єкт TimeSpan не може бути null");
        }
        add(span.getHours(), span.getMinutes());
    }

    // 4. Перевантажені методи subtract()
    // віднімання годин та хвилин
    public void subtract(int hours, int minutes) {
        if (hours < 0) {
            throw new IllegalInputTimeSpanException("Години не можуть бути від'ємними");
        }
        if (minutes < 0 || minutes > 59) {
            throw new IllegalInputTimeSpanException("Хвилини повинні бути в діапазоні від 0 до 59");
        }

        long currentTotalMinutes = (long) this.hours * 60 + this.minutes;
        long subTotalMinutes = (long) hours * 60 + minutes;

        // Перевірка на від'ємний результат
        if (subTotalMinutes > currentTotalMinutes) {
            throw new NegativeTimeSpanException(
                    "Результат віднімання не може бути від'ємним");
        }

        // Обчислення нового інтервалу
        long remainingMinutes = currentTotalMinutes - subTotalMinutes;

        this.hours = (int) (remainingMinutes / 60);
        this.minutes = (int) (remainingMinutes % 60);
    }

    // віднімання хвилин
    public void subtract(int minutes) {
        if (minutes < 0) {
            throw new IllegalInputTimeSpanException("Кількість хвилин не може бути від'ємною");
        }
        subtract(minutes / 60, minutes % 60);
    }

    // віднімання іншого TimeSpan
    public void subtract(TimeSpan span) {
        if (span == null) {
            throw new IllegalInputTimeSpanException("Об'єкт TimeSpan не може бути null");
        }
        subtract(span.getHours(), span.getMinutes());
    }

    // 5. Повна тривалість інтервалу
    // повертає тривалість інтервалу в годинах як дробове число
    public double getTotalHours() {
        return this.hours + (double) this.minutes / 60.0;
    }

    // повертає повну тривалість інтервалу у хвилинах
    public int getTotalMinutes() {
        return this.hours * 60 + this.minutes;
    }

    // 6. Масштабування інтервалу
    public void scale(int factor) {
        // Перевірка коректності множника
        if (factor <= 0) {
            throw new IllegalScaleFactorException(
                    "Множник повинен бути більшим за 0");
        }

        // Переведення інтервалу у хвилини
        long totalMinutes = (long) this.hours * 60 + this.minutes;

        // Перевірка можливого переповнення
        long maxMinutes = (long) Integer.MAX_VALUE * 60 + 59;

        if (totalMinutes > maxMinutes / factor) {
            throw new IllegalScaleFactorException("Результат масштабування занадто великий");
        }

        // Масштабування та нормалізація
        long scaledMinutes = totalMinutes * factor;

        this.hours = (int) (scaledMinutes / 60);
        this.minutes = (int) (scaledMinutes % 60);
    }
}
