package task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Класс Task представляет базовую задачу, которая может быть выполнена.
 * Задача содержит идентификатор, название, описание и статус выполнения.
 */
public class Task {
    protected String name;        // Название задачи
    protected String description; // Описание задачи
    protected Status status;         // Текущий статус задачи (NEW, IN_PROGRESS, DONE)
    protected int id;
    protected LocalDateTime startTime; // Время старта задачи (2023-11-15T14:30)
    protected Duration duration; // Длительность работы над задачей в минутах
    public static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    /**
     * Конструктор для создания задачи.
     *
     * @param name        Название задачи
     * @param description Описание задачи
     * @param startTime Дата и время старта работы над задачей
     * @param duration Длительность работы над задачей в минутах
     */
    public Task(String name, String description, String startTime, long duration) {
        this.name = name;
        this.description = description;
        this.startTime = LocalDateTime.parse(startTime, formatter);
        this.duration = Duration.ofMinutes(duration);
        this.status = Status.NEW;
    }

    /**
     * Конструктор для создания задачи.
     *
     * @param name        Название задачи
     * @param description Описание задачи
     * @param status      Статус задачи
     * @param startTime Дата и время старта работы над задачей
     * @param duration Длительность работы над задачей в минутах
     */
    public Task(String name, String description, String startTime, long duration, Status status) {
        this.name = name;
        this.description = description;
        this.startTime = LocalDateTime.parse(startTime, formatter);
        this.duration = Duration.ofMinutes(duration);
        this.status = status;
    }

    /**
     * Конструктор для создания задачи.
     *
     * @param name        Название задачи
     * @param description Описание задачи
     */
    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        this.status = Status.NEW;
    }

    /**
     * Конструктор для создания задачи.
     *
     * @param name        Название задачи
     * @param description Описание задачи
     * @param status      Статус задачи
     */
    public Task(String name, String description, Status status) {
        this.name = name;
        this.description = description;
        this.status = status;
    }

    /**
     * Возвращает идентификатор задачи.
     *
     * @return Уникальный идентификатор задачи
     */
    public int getId() {
        return id;
    }

    /**
     * Устанавливает идентификатор задачи.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Возвращает наименование задачи.
     *
     * @return наименование задачи
     */
    public String getName() {
        return name;
    }

    /**
     * Устанавливает наименование задачи.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Возвращает описание задачи.
     *
     * @return описание задачи
     */
    public String getDescription() {
        return description;
    }

    /**
     * Устанавливает описание задачи.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Возвращает статус задачи.
     *
     * @return статус задачи
     */
    public Status getStatus() {
        return status;
    }

    /**
     * Устанавливает статус задачи.
     */
    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public Duration getDuration() {
        if (duration == null) {
            throw new IllegalStateException("Параметр не задан для задачи");
        }
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public LocalDateTime getEndTime() {
        if (startTime == null || duration == null) {
            return null;
        }
        return startTime.plus(duration);
    }

    public DateTimeFormatter getFormatter() {
        return formatter;
    }

    public void setFormatter(DateTimeFormatter formatter) {
        this.formatter = formatter;
    }

    public Task createTempTask(Task original, String startTime, long duration) {
        Task copy = new Task(original.getName(), original.getDescription());
        copy.setId(original.getId());
        copy.setStatus(original.getStatus());
        copy.setStartTime(LocalDateTime.parse(startTime, formatter));
        copy.setDuration(Duration.ofMinutes(duration));
        return copy;
    }

    /**
     * Переопределение метода equals для сравнения задач по идентификатору.
     *
     * @param o Объект для сравнения
     * @return true, если задачи имеют одинаковый идентификатор, иначе false
     */

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (!(o instanceof Task task)) return false;

        return id == task.id;
    }

    /**
     * Переопределение метода hashCode для корректной работы с коллекциями.
     *
     * @return Хэш-код, основанный на идентификаторе задачи
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    /**
     * Переопределение метода toString для удобного вывода информации о задаче.
     *
     * @return Строковое представление задачи в формате:
     * "Задача {Название=..., Описание=..., ID=..., Статус=...}"
     */
    @Override
    public String toString() {
        return String.format("""
            Задача {
                ID = %d
                Название = '%s'
                Описание = '%s'
                Статус = %s
                Время начала = %s
                Длительность = %s минут
            }""",
                id,
                name,
                description,
                status,
                startTime != null ? startTime.format(formatter) : "не назначено",
                duration != null ? duration.toMinutes() : "не назначено");
    }
}
