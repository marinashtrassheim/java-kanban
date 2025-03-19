package Task;
import Management.TaskManager;
import java.util.Objects;
/**
 * Класс Task представляет базовую задачу, которая может быть выполнена.
 * Задача содержит идентификатор, название, описание и статус выполнения.
 */
public class Task {
    protected String name;        // Название задачи
    protected String description; // Описание задачи
    public int id;                // Уникальный идентификатор задачи
    public Status status;         // Текущий статус задачи (NEW, IN_PROGRESS, DONE)
    /**
     * Конструктор для создания задачи.
     *
     * @param id          Уникальный идентификатор задачи
     * @param name        Название задачи
     * @param description Описание задачи
     * @param status      Статус задачи (NEW, IN_PROGRESS, DONE)
     */
    public Task(int id, String name, String description, Status status) {
        this.id = id;
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
     * Переопределение метода equals для сравнения задач по идентификатору.
     *
     * @param o Объект для сравнения
     * @return true, если задачи имеют одинаковый идентификатор, иначе false
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
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
                "Название='" + name + '\'' +
                ", Описание='" + description + '\'' +
                ", ID=" + id +
                ", Статус=" + status +
                '}';
    }
}
