package task;
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
     * @param name        Название задачи
     * @param description Описание задачи
     * @param status Статус задачи
     */
    public Task(String name, String description, Status status) {
        this.name = name;
        this.description = description;
        this.status = status;
    }
    /**
     * Возвращает идентификатор задачи.
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
        return "Задача {" +
                "ID=" + id  +
                ", Название='" + name + '\'' +
                ", Описание='" + description + '\'' +
                ", Статус=" + status +
                '}';
    }
}
