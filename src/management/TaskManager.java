package management;
import task.Status;
import task.Task;
import task.SubTask;
import task.Epic;

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
     * @return Уникальный идентификатор (int)
     */
    private int generateId() {
        id++;
        return id;
    }
    /**
     * Добавляет задачу в коллекцию задач.
     * @param task Задача для добавления
     */
    public void createTask(Task task) {
        int taskId = generateId();
        task.setId(taskId); // Устанавливаем ID задачи
        tasks.put(taskId, task);
    }
    /**
     * Добавляет подзадачу в коллекцию подзадач.
     * @param subTask Подзадача для добавления
     */
    public void createSubTask(SubTask subTask) {
        int subTaskId = generateId();
        subTask.setId(subTaskId);
        subTasks.put(subTaskId, subTask);
        Epic epic = subTask.getEpic();
        if (epic != null) {
            addSubtaskToEpic(epic, subTask);
        }
    }
    /**
     * Добавляет эпик в коллекцию эпиков.
     * @param epic Эпик для добавления
     */
    public void createEpic(Epic epic) {
        int epicId = generateId();
        epic.setId(epicId);
        epics.put(epicId, epic);
    }
    /**
     * Возвращает строковое представление всех задач.
     * @return Строка, содержащая информацию о всех задачах
     */
    public HashMap<Integer, Task> getAllTasks() {
        return tasks;
    }
    /**
     * Возвращает строковое представление всех эпиков.
     *
     * @return Строка, содержащая информацию о всех эпиках
     */
    public HashMap<Integer, Epic> getAllEpics() {
        return epics;
    }
    /**
     * Возвращает строковое представление всех подзадач.
     *
     * @return Строка, содержащая информацию о всех подзадачах
     */
    public HashMap<Integer, SubTask> getAllSubTasks() {
        return subTasks;
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
     * @param id Идентификатор задачи
     * @return Задача или null, если задача не найдена
     */
    public Task getTask(int id) {
        return tasks.get(id);
    }
    /**
     * Удаляет задачу по её идентификатору.
     * @param id Идентификатор задачи для удаления
     */
    public void deleteTask(int id) {
        tasks.remove(id);
    }
    /**
     * Удаляет подзадачу по её идентификатору.
     * Если подзадача связана с эпиком, она также удаляется из списка подзадач эпика.
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
     * Обновляет задачу.
     * @param currentTask Задача на обновление
     * @param updatedTask Задача на которую обновляем
     */
    public void updateTask(Task currentTask, Task updatedTask) {
        currentTask.setName(updatedTask.getName());
        currentTask.setDescription(updatedTask.getDescription());
        currentTask.setStatus(updatedTask.getStatus());
    }
    /**
     * Обновляет подзадачу.
     * Если подзадача связана с эпиком, обновляем статус эпика.
     * @param currentSubTask Подзадача на обновление
     * @param updatedSubTask Подзадача на которую меняем
     */
    public void updateSubTask(SubTask currentSubTask, SubTask updatedSubTask) {
        currentSubTask.setName(updatedSubTask.getName());
        currentSubTask.setDescription(updatedSubTask.getDescription());
        currentSubTask.setStatus(updatedSubTask.getStatus());
        updateEpicStatus(currentSubTask.getEpic());
    }
    /**
     * Обновляет эпик.
     * @param currentEpic Эпик на обновление
     * @param updatedEpic Новый эпик
     */
    public void updateEpic(Epic currentEpic, Epic updatedEpic) {
        currentEpic.setName(updatedEpic.getName());
        currentEpic.setDescription(updatedEpic.getDescription());
    }
    /**
     * Метод для обновления статуса эпика на основе статусов его подзадач.
     * Логика обновления:
     * 1. Если список подзадач пуст или все подзадачи имеют статус NEW, то статус эпика — NEW.
     * 2. Если все подзадачи имеют статус DONE, то статус эпика — DONE.
     * 3. В остальных случаях статус эпика — IN_PROGRESS.
     */
    private void updateEpicStatus(Epic epic) {
        ArrayList<SubTask> subtasks = epic.getSubtasks();
        if (subtasks == null || subtasks.isEmpty()) {
            epic.setStatus(Status.NEW); // Если подзадач нет, статус NEW
            return;
        }

        boolean allDone = true; // Предполагаем, что все подзадачи завершены
        boolean allNew = true;  // Предполагаем, что все подзадачи новые
        // Проверяем статусы всех подзадач
        for (SubTask subTask : subtasks) {
            if (subTask.getStatus() != Status.DONE) {
                allDone = false; // Если хотя бы одна подзадача не DONE, то Epic не DONE
            }
            if (subTask.getStatus() != Status.NEW) {
                allNew = false; // Если хотя бы одна подзадача не NEW, то Epic не NEW
            }
        }
        // Устанавливаем статус эпика на основе проверки
        if (allDone) {
            epic.setStatus(Status.DONE);
        } else if (allNew) {
            epic.setStatus(Status.NEW);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }
    /**
     * Метод для добавления подзадачи к эпику.
     * После добавления подзадачи статус эпика обновляется.
     * @param subTask Подзадача, которую нужно добавить к эпику
     */
    public void addSubtaskToEpic(Epic epic, SubTask subTask) {
        epic.setSubtasks(subTask);
        updateEpicStatus(epic); // Обновляем статус эпика
    }
    @Override
    public String toString() {
        return "Менеджер задач {" +
                "Задачи=" + tasks +
                '}';
    }

}
