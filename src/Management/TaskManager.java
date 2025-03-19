package Management;
import Task.Task;
import Task.SubTask;
import Task.Epic;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Класс TaskManager отвечает за управление задачами, подзадачами и эпиками.
 * Он предоставляет методы для создания, обновления, удаления и получения задач,
 * а также для управления связями между подзадачами и эпиками.
 */
public class TaskManager {
    private int id = 0; // Счетчик для генерации уникальных идентификаторов задач
    private HashMap<Integer, Task> tasks = new HashMap<>(); // Коллекция для хранения задач
    private HashMap<Integer, SubTask> subTasks = new HashMap<>(); // Коллекция для хранения подзадач
    private HashMap<Integer, Epic> epics = new HashMap<>(); // Коллекция для хранения эпиков
    /**
     * Генерирует уникальный идентификатор для задачи.
     *
     * @return Уникальный идентификатор (int)
     */
    public int generateId() {
        id++;
        return id;
    }

    /**
     * Добавляет задачу в коллекцию задач.
     *
     * @param task Задача для добавления
     */
    public void createTask(Task task) {
        tasks.put(task.getId(), task); // Добавляем задачу в коллекцию
    }

    /**
     * Добавляет подзадачу в коллекцию подзадач.
     *
     * @param subTask Подзадача для добавления
     */
    public void createSubTask(SubTask subTask) {
        subTasks.put(subTask.getId(), subTask);
    }

    /**
     * Добавляет эпик в коллекцию эпиков.
     *
     * @param epic Эпик для добавления
     */
    public void createEpic(Epic epic) {
        epics.put(epic.getId(), epic);
    }
    /**
     * Возвращает строковое представление всех задач.
     *
     * @return Строка, содержащая информацию о всех задачах
     */
    public String getAllTasks() {
        String result = "";
        for (Task task : tasks.values()) {
            result += task.toString() + "\n"; // Конкатенация строк
        }
        return result;
    }
    /**
     * Возвращает строковое представление всех эпиков.
     *
     * @return Строка, содержащая информацию о всех эпиках
     */
    public String getAllEpics() {
        String result = "";
        for (Epic epic : epics.values()) {
            result += epic.toString() + "\n";
        }
        return result;
    }
    /**
     * Возвращает строковое представление всех подзадач.
     *
     * @return Строка, содержащая информацию о всех подзадачах
     */
    public String getAllSubTasks() {
        String result = "";
        for (SubTask subTask : subTasks.values()) {
            result += subTask.toString() + "\n";
        }
        return result;
    }
    /**
     * Возвращает список подзадач, связанных с указанным эпиком.
     *
     * @param epicId Идентификатор эпика
     * @return Список подзадач, принадлежащих эпику, или пустой список, если эпик не найден
     */
    public ArrayList<SubTask> getSubTasksByEpicId(int epicId) {
        Epic epic = epics.get(epicId); // Получаем Epic по ID
        if (epic != null) {
            return epic.getSubtasks(); // Возвращаем список подзадач Epic
        } else {
            return new ArrayList<>(); // Если Epic не найден, возвращаем пустой список
        }
    }

    /**
     * Возвращает задачу по её идентификатору.
     *
     * @param id Идентификатор задачи
     * @return Задача или null, если задача не найдена
     */
    public Task getTask(int id) {
        return tasks.get(id);
    }

    /**
     * Удаляет задачу по её идентификатору.
     *
     * @param id Идентификатор задачи для удаления
     */
    public void deleteTask(int id) {
        tasks.remove(id);
    }

    /**
     * Удаляет подзадачу по её идентификатору.
     * Если подзадача связана с эпиком, она также удаляется из списка подзадач эпика.
     *
     * @param id Идентификатор подзадачи для удаления
     */
    public void deleteSubTask(int id) {
        SubTask subTaskToRemove = subTasks.get(id); // Получаем SubTask по ID
        if (subTaskToRemove != null) {
            // Находим Epic, к которому принадлежит SubTask
            Epic parentEpic = subTaskToRemove.getEpic();
            if (parentEpic != null) {
                // Удаляем SubTask из списка подзадач Epic
                parentEpic.getSubtasks().remove(subTaskToRemove);
            }
            // Удаляем SubTask из коллекции subTasks
            subTasks.remove(id);
        }
    }

    /**
     * Удаляет все задачи, подзадачи и эпики.
     */
    public void deleteAll() {
        tasks.clear();
        subTasks.clear();
        epics.clear();
    }

    /**
     * Удаляет эпик по его идентификатору.
     * Если эпик содержит подзадачи, они также удаляются.
     *
     * @param id Идентификатор эпика для удаления
     */
    public void deleteEpic(int id) {
        Epic epicToRemove = epics.get(id);
        if (epicToRemove != null) {
            ArrayList<SubTask> subTasksByEpicToRemove = getSubTasksByEpicId(id);
            if (!subTasksByEpicToRemove.isEmpty()) {
                for (SubTask subTask : subTasksByEpicToRemove) {
                    subTasks.remove(subTask.getId());
                }
                epics.remove(id);
            } else {
                epics.remove(id);
            }
        }
        else {
            return;
        }
    }
    /**
     * Обновляет задачу по её идентификатору.
     *
     * @param id   Идентификатор задачи для обновления
     * @param task Новая версия задачи
     */
    public void updateTask(int id, Task task) {
        if (tasks.containsKey(id)) {
            tasks.put(id, task);
        } else {
            return;
        }
    }
    /**
     * Обновляет подзадачу по её идентификатору.
     * Если подзадача связана с эпиком, она также обновляется в списке подзадач эпика.
     *
     * @param id      Идентификатор подзадачи для обновления
     * @param subTask Новая версия подзадачи
     */
    public void updateSubTask(int id, SubTask subTask) {
        SubTask subTaskToUpdate = subTasks.get(id); // Получаем SubTask по ID
        if (subTaskToUpdate != null) {
            subTasks.put(id, subTask);
            // Находим Epic, к которому принадлежит SubTask
            Epic parentEpic = subTaskToUpdate.getEpic();
            if (parentEpic != null) {
                ArrayList<SubTask> subtasksInEpic = parentEpic.getSubtasks();
                // Ищем подзадачу в списке подзадач Epic и обновляем её
                for (int i = 0; i < subtasksInEpic.size(); i++) {
                    if (subtasksInEpic.get(i).getId() == id) {
                        subtasksInEpic.set(i, subTask);
                    }
                }
            }
        }
    }
    /**
     * Обновляет эпик по его идентификатору.
     *
     * @param id   Идентификатор эпика для обновления
     * @param epic Новая версия эпика
     */
    public void updateEpic(int id, Epic epic) {
        if (epics.containsKey(id)) {
            epics.put(id, epic);
        } else {
            return;
        }
    }
    /**
     * Возвращает строковое представление менеджера задач.
     *
     * @return Строка, содержащая информацию о всех задачах
     */
    @Override
    public String toString() {
        return "Менеджер задач {" +
                "Задачи=" + tasks +
                '}';
    }

}
