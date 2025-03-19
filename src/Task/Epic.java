package Task;
import java.util.ArrayList;
/**
 * Класс Epic представляет собой задачу типа "Эпик", которая может содержать подзадачи (SubTask).
 * Эпик имеет статус, который зависит от статусов его подзадач.
 */
public class Epic extends Task{
    // Список подзадач, относящихся к этому эпику
    ArrayList<SubTask> subtasks = new ArrayList<SubTask>();

    /**
     * Конструктор для создания эпика, когда список подзадач неизвестен.
     *
     * @param id          Уникальный идентификатор эпика
     * @param name        Название эпика
     * @param description Описание эпика
     */
    public Epic(int id, String name, String description) {
        super(id, name, description, Status.NEW); // По умолчанию статус NEW
    }

    /**
     * Конструктор для создания эпика с известным списком подзадач.
     *
     * @param id          Уникальный идентификатор эпика
     * @param name        Название эпика
     * @param description Описание эпика
     * @param subtasks    Список подзадач, относящихся к этому эпику
     */
    public Epic(int id, String name, String description, ArrayList<SubTask> subtasks) {
        super(id, name, description, Status.NEW); // По умолчанию статус NEW
        this.subtasks = subtasks;
        updateEpicStatus(); // Обновляем статус эпика на основе переданных подзадач
    }

    /**
     * Метод для обновления статуса эпика на основе статусов его подзадач.
     * Логика обновления:
     * 1. Если список подзадач пуст или все подзадачи имеют статус NEW, то статус эпика — NEW.
     * 2. Если все подзадачи имеют статус DONE, то статус эпика — DONE.
     * 3. В остальных случаях статус эпика — IN_PROGRESS.
     */
    public void updateEpicStatus() {
        if (subtasks == null || subtasks.isEmpty()) {
            this.status = Status.NEW; // Если подзадач нет, статус NEW
            return;
        }

        boolean allDone = true; // Предполагаем, что все подзадачи завершены
        boolean allNew = true;  // Предполагаем, что все подзадачи новые
        // Проверяем статусы всех подзадач
        for (SubTask subTask : subtasks) {
            if (subTask.status != Status.DONE) {
                allDone = false; // Если хотя бы одна подзадача не DONE, то Epic не DONE
            }
            if (subTask.status != Status.NEW) {
                allNew = false; // Если хотя бы одна подзадача не NEW, то Epic не NEW
            }
        }
        // Устанавливаем статус эпика на основе проверки
        if (allDone) {
            this.status = Status.DONE;
        } else if (allNew) {
            this.status = Status.NEW;
        } else {
            this.status = Status.IN_PROGRESS;
        }
    }
    /**
     * Метод для добавления подзадачи к эпику.
     * После добавления подзадачи статус эпика обновляется.
     *
     * @param subTask Подзадача, которую нужно добавить к эпику
     */
    public void addSubtasks(SubTask subTask) {
        subtasks.add(subTask); // Добавляем подзадачу в список
        subTask.addToEpic(this); // Связываем подзадачу с этим эпиком
        updateEpicStatus(); // Обновляем статус эпика
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
