package task;

/**
 * Класс SubTask представляет собой подзадачу, которая может быть связана с эпиком (Epic).
 * Подзадача наследует все поля и методы класса Task и дополнительно содержит ссылку на эпик.
 */
public class SubTask extends Task {
    // Ссылка на эпик, к которому относится эта подзадача
    private Epic epic;

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

    /**
     * Конструктор для создания подзадачи с привязкой к эпику.
     * @param name        Название подзадачи
     * @param description Описание подзадачи
     * @param status      Статус подзадачи
     */
    public SubTask(String name, String description, Status status) {
        super(name, description); // Вызов конструктора родительского класса Task
        this.status = status;
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
        if (epic == null || epic.getId() == this.getId()) {  // Сравниваем только id
            return;
        }
        this.epic = epic;
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
