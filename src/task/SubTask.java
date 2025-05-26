package task;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Класс SubTask представляет собой подзадачу, которая может быть связана с эпиком (Epic).
 * Подзадача наследует все поля и методы класса Task и дополнительно содержит ссылку на эпик.
 */
public class SubTask extends Task {
    // Ссылка на эпик, к которому относится эта подзадача
    private Epic epic;

    /**
     * Конструктор для создания подзадачи с привязкой к эпику, временем старта и длительностью.
     *
     * @param name        Название подзадачи
     * @param description Описание подзадачи
     * @param startTime Дата и время старта работы над задачей
     * @param duration Длительность работы над задачей в минутах
     * @param epic        Эпик, к которому относится подзадача
     */
    public SubTask(String name, String description, String startTime, long duration, Epic epic) {
        super(name, description, startTime, duration); // Вызов конструктора родительского класса Task
        this.epic = epic; // Привязка подзадачи к эпику
        this.status = Status.NEW;
    }

    /**
     * Конструктор для создания подзадачи с привязкой к эпику.
     *
     * @param name        Название подзадачи
     * @param description Описание подзадачи
     * @param epic        Эпик, к которому относится подзадача
     */
    public SubTask(String name, String description, Epic epic) {
        super(name, description); // Вызов конструктора родительского класса Task
        this.epic = epic; // Привязка подзадачи к эпику
        this.status = Status.NEW;
    }

    public void setEpic(Epic epic) {
        this.epic = epic;
    }

    /**
     * Метод для получения эпика, к которому относится подзадача.
     *
     * @return Эпик, к которому привязана подзадача, или null, если подзадача не привязана
     */
    public Epic getEpic() {
        return epic;
    }

    /**
     * Переопределение метода toString для удобного вывода информации о подзадаче.
     *
     * @return Строковое представление подзадачи в формате:
     * "Подзадача {ID=..., Название=..., Описание=..., Статус=..., ID эпика=...}"
     */
    public SubTask createTempSubTask(SubTask original, String startTime, long duration) {
        SubTask copy = new SubTask(original.getName(), original.getDescription(), original.getEpic());
        copy.setId(original.getId());
        copy.setStatus(original.getStatus());
        copy.setStartTime(LocalDateTime.parse(startTime, formatter));
        copy.setDuration(Duration.ofMinutes(duration));
        return copy;
    }

    @Override
    public String toString() {
        return String.format("""
            Подзадача {
                ID = %d
                Название = '%s'
                Описание = '%s'
                Статус = %s
                ID эпика = %s
                Время начала = %s
                Длительность = %s минут
            }""",
                id,
                name,
                description,
                status,
                epic != null ? epic.getId() : "не назначен",
                startTime != null ? startTime.format(formatter) : "не назначено",
                duration != null ? duration.toMinutes() : "не назначено");
    }
}
