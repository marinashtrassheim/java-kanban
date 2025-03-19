package Task;
import Task.Epic;
/**
 * Класс SubTask представляет собой подзадачу, которая может быть связана с эпиком (Epic).
 * Подзадача наследует все поля и методы класса Task и дополнительно содержит ссылку на эпик.
 */
public class SubTask extends Task {
    // Ссылка на эпик, к которому относится эта подзадача
    protected Epic epic;

    /**
     * Конструктор для создания подзадачи без привязки к эпику.
     *
     * @param id          Уникальный идентификатор подзадачи
     * @param name        Название подзадачи
     * @param description Описание подзадачи
     * @param status      Статус подзадачи (NEW, IN_PROGRESS, DONE)
     */
    public SubTask(int id, String name, String description, Status status) {
        super(id, name, description, status); // Вызов конструктора родительского класса Task
    }

    /**
     * Конструктор для создания подзадачи с привязкой к эпику.
     *
     * @param id          Уникальный идентификатор подзадачи
     * @param name        Название подзадачи
     * @param description Описание подзадачи
     * @param status      Статус подзадачи (NEW, IN_PROGRESS, DONE)
     * @param epic        Эпик, к которому относится подзадача
     */
    public SubTask(int id, String name, String description, Status status, Epic epic) {
        super(id, name, description, status); // Вызов конструктора родительского класса Task
        this.epic = epic; // Привязка подзадачи к эпику
    }
    /**
     * Метод для привязки подзадачи к эпику.
     *
     * @param epic Эпик, к которому нужно привязать подзадачу
     */
    public void addToEpic(Epic epic) {
        this.epic = epic; //Устанавливаем ссылку на эпик
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
     * Метод для установки эпика, к которому относится подзадача.
     *
     * @param epic Эпик, к которому нужно привязать подзадачу
     */
    public void setEpic(Epic epic) {
        this.epic = epic; //Устанавливаем ссылку на эпик
    }
    /**
     * Переопределение метода toString для удобного вывода информации о подзадаче.
     *
     * @return Строковое представление подзадачи в формате:
     * "Подзадача {ID=..., Название=..., Описание=..., Статус=..., ID эпика=...}"
     */
    @Override
    public String toString() {
        return "Подзадача {" +
                "ID=" + id +
                ", Название='" + name + '\'' +
                ", Описание='" + description + '\'' +
                ", Статус=" + status +
                ", ID эпика=" + (epic != null ? epic.getId() : "не назначен") +
                '}';
    }
}
