package task;

import java.util.ArrayList;

/**
 * Класс Epic представляет собой задачу типа "Эпик", которая может содержать подзадачи (SubTask).
 * Эпик имеет статус, который зависит от статусов его подзадач.
 */
public class Epic extends Task {
    // Список подзадач, относящихся к этому эпику
    private ArrayList<SubTask> subtasks = new ArrayList<SubTask>();

    /**
     * Конструктор для создания эпика, когда список подзадач неизвестен.
     *
     * @param name        Название эпика
     * @param description Описание эпика
     */
    public Epic(String name, String description) {
        super(name, description);
        this.status = Status.NEW; // По умолчанию статус NEW
    }

    /**
     * Метод для получения списка подзадач, относящихся к этому эпику.
     *
     * @return Список подзадач
     */
    public ArrayList<SubTask> getSubtasks() {
        return subtasks;
    }

    /**
     * Метод для установки списка подзадач, относящихся к этому эпику.
     */
    public void setSubtasks(SubTask subtask) {
        if (subtask == null || subtask.getId() == this.getId()) {
            return;
        }
        subtasks.add(subtask);
    }

    /**
     * Переопределение метода toString для удобного вывода информации об эпике.
     *
     * @return Строковое представление эпика в формате:
     * "Эпик {ID=..., Название=..., Описание=..., Статус=..., Подзадачи=...}"
     */
    @Override
    public String toString() {
        return "Эпик {" +
                "ID=" + id +
                ", Название='" + name + '\'' +
                ", Описание='" + description + '\'' +
                ", Статус=" + status +
                ", Подзадачи=" + subtasks +
                '}';
    }
}
