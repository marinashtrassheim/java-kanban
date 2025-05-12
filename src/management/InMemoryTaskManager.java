package management;

import task.Epic;
import task.SubTask;
import task.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    private int id = 0; // Счетчик для генерации уникальных идентификаторов задач
    private final HashMap<Integer, Task> allTasks = new HashMap<>();
    private final HistoryManager historyManager = Managers.getDefaultHistory();

    /**
     * Генерирует уникальный идентификатор для задачи.
     *
     * @return Уникальный идентификатор (int)
     */
    private int generateId() {
        id++;
        return id;
    }

    /**
     * Добавляет задачу в коллекцию задач.
     *
     * @param task Задача для добавления
     */
    @Override
    public void createTask(Task task) {
        int taskId = generateId();
        task.setId(taskId); // Устанавливаем ID задачи
        allTasks.put(taskId, task);
    }

    /**
     * Добавляет подзадачу в коллекцию подзадач.
     *
     * @param subTask Подзадача для добавления
     */
    @Override
    public void createSubTask(SubTask subTask) {
        int subTaskId = generateId();
        subTask.setId(subTaskId);
        allTasks.put(subTaskId, subTask);
        Epic epic = subTask.getEpic();
        if (epic != null) {
            addSubtaskToEpic(epic, subTask);
        }
    }

    /**
     * Добавляет эпик в коллекцию эпиков.
     *
     * @param epic Эпик для добавления
     */
    @Override
    public void createEpic(Epic epic) {
        int epicId = generateId();
        epic.setId(epicId);
        allTasks.put(epicId, epic);
    }

    public HashMap<Integer, Task> getAllTasksTypes() {
        return allTasks;
    }

    /**
     * Возвращает строковое представление всех задач.
     *
     * @return Строка, содержащая информацию о всех задачах
     */
    @Override
    public HashMap<Integer, Task> getAllTasks() {
        HashMap<Integer, Task> tasks = new HashMap<>();
        for (Task task : allTasks.values()) {
            if (!(task instanceof SubTask) && !(task instanceof Epic)) {
                tasks.put(task.getId(), task);
            }
        }
        return tasks;
    }

    /**
     * Возвращает строковое представление всех эпиков.
     *
     * @return Строка, содержащая информацию о всех эпиках
     */
    @Override
    public HashMap<Integer, Epic> getAllEpics() {
        HashMap<Integer, Epic> epics = new HashMap<>();
        for (Task task : allTasks.values()) {
            if (task instanceof Epic) {
                epics.put(task.getId(), (Epic) task);
            }
        }
        return epics;
    }

    /**
     * Возвращает строковое представление всех подзадач.
     *
     * @return Строка, содержащая информацию о всех подзадачах
     */
    @Override
    public HashMap<Integer, SubTask> getAllSubTasks() {
        HashMap<Integer, SubTask> subTasks = new HashMap<>();
        for (Task task : allTasks.values()) {
            if (task instanceof SubTask) {
                subTasks.put(task.getId(), (SubTask) task);
            }
        }
        return subTasks;
    }

    /**
     * Возвращает список подзадач, связанных с указанным эпиком.
     *
     * @param epicId Идентификатор эпика
     * @return Список подзадач, принадлежащих эпику, или пустой список, если эпик не найден
     */
    @Override
    public ArrayList<SubTask> getSubTasksByEpicId(int epicId) {
        Task task = allTasks.get(epicId); // Получаем Epic по ID
        if (task instanceof Epic epic) {
            return epic.getSubtasks();
        } else {
            return new ArrayList<>(); // Если Epic не найден, возвращаем пустой список
        }
    }

    /**
     * Возвращает задачу по её идентификатору.
     *
     * @param id Идентификатор задачи
     */
    @Override
    public Task getTask(int id) {
        Task task = allTasks.get(id);
        if (!(task instanceof SubTask) && !(task instanceof Epic)) {
            historyManager.addToHistory(task);
        }
        return task;
    }

    /**
     * Возвращает эпик по её идентификатору.
     *
     * @param id Идентификатор эпика
     */
    @Override
    public Epic getEpic(int id) {
        Task task = allTasks.get(id);
        if (task == null) {
            throw new IllegalArgumentException("Задача с ID " + id + " не найдена.");
        }
        if (!(task instanceof Epic epic)) {
            throw new IllegalArgumentException("Задача с ID " + id + " не является эпиком. Переданный тип: " + task.getClass().getSimpleName());
        }
        historyManager.addToHistory(epic);
        return epic;
    }

    /**
     * Возвращает подзадачу по её идентификатору.
     *
     * @param id Идентификатор подзадачи
     */
    @Override
    public SubTask getSubTask(int id) {
        Task task = allTasks.get(id);
        if (task == null) {
            throw new IllegalArgumentException("Задача с ID " + id + " не найдена.");
        }
        if (!(task instanceof SubTask subTask)) {
            throw new IllegalArgumentException("Задача с ID " + id + " не является эпиком. Переданный тип: " + task.getClass().getSimpleName());
        }
        historyManager.addToHistory(subTask);
        return subTask;
    }

    /**
     * Удаляет задачу по её идентификатору.
     *
     * @param id Идентификатор задачи для удаления
     */
    @Override
    public void deleteTask(int id) {
        Task task = allTasks.get(id);
        if (!(task instanceof SubTask) && !(task instanceof Epic)) {
            allTasks.remove(id);
        }
    }

    /**
     * Удаляет подзадачу по её идентификатору.
     * Если подзадача связана с эпиком, она также удаляется из списка подзадач эпика.
     *
     * @param id Идентификатор подзадачи для удаления
     */
    @Override
    public void deleteSubTask(int id) {
        Task task = allTasks.get(id);
        if (task instanceof SubTask subTask) {
            // Находим Epic, к которому принадлежит SubTask
            Epic parentEpic = subTask.getEpic();
            if (parentEpic != null) {
                // Удаляем SubTask из списка подзадач Epic
                parentEpic.getSubtasks().remove(subTask);
            }
            // Удаляем SubTask из коллекции subTasks
            allTasks.remove(id);
        }
    }

    /**
     * Удаляет все задачи, подзадачи и эпики.
     */
    @Override
    public void deleteAll() {
        allTasks.clear();
    }

    /**
     * Удаляет эпик по его идентификатору.
     * Если эпик содержит подзадачи, они также удаляются.
     *
     * @param id Идентификатор эпика для удаления
     */
    @Override
    public void deleteEpic(int id) {
        Task task = allTasks.get(id);
        //Epic epicToRemove = epics.get(id);
        if (task instanceof Epic) {
            ArrayList<SubTask> subTasksByEpicToRemove = getSubTasksByEpicId(id);
            if (!subTasksByEpicToRemove.isEmpty()) {
                for (SubTask subTask : subTasksByEpicToRemove) {
                    allTasks.remove(subTask.getId());
                }
            }
            allTasks.remove(id);
        }
    }

    /**
     * Обновляет задачу.
     *
     * @param currentTask Задача на обновление
     * @param updatedTask Задача на которую обновляем
     */
    @Override
    public void updateTask(Task currentTask, Task updatedTask) {
        currentTask.setName(updatedTask.getName());
        currentTask.setDescription(updatedTask.getDescription());
        currentTask.setStatus(updatedTask.getStatus());
    }

    /**
     * Обновляет подзадачу.
     * Если подзадача связана с эпиком, обновляем статус эпика.
     *
     * @param currentSubTask Подзадача на обновление
     * @param updatedSubTask Подзадача на которую меняем
     */
    @Override
    public void updateSubTask(SubTask currentSubTask, SubTask updatedSubTask) {
        currentSubTask.setName(updatedSubTask.getName());
        currentSubTask.setDescription(updatedSubTask.getDescription());
        currentSubTask.setStatus(updatedSubTask.getStatus());
        updateEpicStatus(currentSubTask.getEpic());
    }

    /**
     * Обновляет эпик.
     *
     * @param currentEpic Эпик на обновление
     * @param updatedEpic Новый эпик
     */
    @Override
    public void updateEpic(Epic currentEpic, Epic updatedEpic) {
        currentEpic.setName(updatedEpic.getName());
        currentEpic.setDescription(updatedEpic.getDescription());
    }

    /**
     * Метод для добавления подзадачи к эпику.
     * После добавления подзадачи статус эпика обновляется.
     *
     * @param subTask Подзадача, которую нужно добавить к эпику
     */
    @Override
    public void addSubtaskToEpic(Epic epic, SubTask subTask) {
        epic.setSubtasks(subTask);
        updateEpicStatus(epic); // Обновляем статус эпика
    }

    @Override
    public String toString() {
        return "Менеджер задач {" +
                "Задачи=" + allTasks +
                '}';
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }
}
