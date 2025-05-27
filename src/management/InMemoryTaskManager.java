package management;

import exceptions.TaskOverlapException;
import task.Epic;
import task.Status;
import task.SubTask;
import task.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private int id = 0; // Счетчик для генерации уникальных идентификаторов задач
    private final HashMap<Integer, Task> tasks = new HashMap<>();      // Обычные задачи
    private final HashMap<Integer, Epic> epics = new HashMap<>();     // Эпики
    private final HashMap<Integer, SubTask> subTasks = new HashMap<>(); // Подзадачи
    private final TreeSet<Task> prioritizedTasks = new TreeSet<>(
            Comparator.comparing(
                    Task::getStartTime,
                    Comparator.nullsLast(Comparator.naturalOrder()) // null идёт в конец
            ).thenComparing(Task::getId) // при одинаковом времени сортируем по ID
    );
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
    public void createTask(Task task) {  // Убрали throws TaskOverlapException
        if (isExecutionOverlapInprioritizedTasks(task)) {
            throw new TaskOverlapException();
        }
        int taskId = generateId();
        task.setId(taskId);
        tasks.put(taskId, task);
        prioritizedTasks.add(task);
    }

    /**
     * Добавляет подзадачу в коллекцию подзадач.
     *
     * @param subTask Подзадача для добавления
     */
    @Override
    public void createSubTask(SubTask subTask) {
        if (isExecutionOverlapInprioritizedTasks(subTask)) {
            throw new TaskOverlapException();
        }
        int subTaskId = generateId();
        subTask.setId(subTaskId);
        subTasks.put(subTaskId, subTask);
        prioritizedTasks.add(subTask);
        Epic epic = subTask.getEpic();
        epic.getPrioritizedSubTasks().add(subTask);
        epic.updateEpicStatus(epic);
        epic.updateEpicTimes(epic);
    }

    /**
     * Добавляет эпик в коллекцию эпиков.
     *
     * @param epic Эпик для добавления
     */
    @Override
    public void createEpic(Epic epic) {
        if (isExecutionOverlapInprioritizedTasks(epic)) {
            throw new TaskOverlapException();
        }
        int epicId = generateId();
        epic.setId(epicId);
        epics.put(epicId, epic);
        prioritizedTasks.add(epic);
    }

    public TreeSet<Task> getAllTasksTypes() {
        return prioritizedTasks;
    }

    /**
     * Возвращает строковое представление всех задач.
     *
     * @return Строка, содержащая информацию о всех задачах
     */
    @Override
    public HashMap<Integer, Task> getAllTasks() {
        return tasks;
    }

    /**
     * Возвращает строковое представление всех подзадач.
     *
     * @return Строка, содержащая информацию о всех эпиках
     */
    @Override
    public HashMap<Integer, SubTask> getAllSubTasks() {
        return subTasks;
    }

    /**
     * Возвращает строковое представление всех эпиков.
     *
     * @return Строка, содержащая информацию о всех эпиках
     */
    @Override
    public HashMap<Integer, Epic> getAllEpics() {
        return epics;
    }

    /**
     * Возвращает задачу по её идентификатору.
     *
     * @param id Идентификатор задачи
     */
    @Override
    public Task getTask(int id) {
        return tasks.get(id);
    }

    /**
     * Возвращает подзадачу по её идентификатору.
     *
     * @param id Идентификатор задачи
     */
    @Override
    public SubTask getSubTask(int id) {
        return subTasks.get(id);
    }

    /**
     * Возвращает эпик по её идентификатору.
     *
     * @param id Идентификатор эпика
     */
    @Override
    public Epic getEpic(int id) {
        return epics.get(id);
    }

    /**
     * Удаляет задачу по её идентификатору.
     *
     * @param id Идентификатор задачи для удаления
     */
    @Override
    public void deleteTask(int id) {
        Task task = tasks.get(id);
        tasks.remove(id);
        prioritizedTasks.remove(task);
    }

    /**
     * Удаляет подзадачу по её идентификатору.
     * Если подзадача связана с эпиком, она также удаляется из списка подзадач эпика.
     *
     * @param id Идентификатор подзадачи для удаления
     */
    @Override
    public void deleteSubTask(int id) {
        SubTask subTask = subTasks.get(id);
        Epic epic = subTask.getEpic();
        epic.deletePrioritizedSubTasks(subTask);
        subTasks.remove(id);
        prioritizedTasks.remove(subTask);
    }

    /**
     * Удаляет все задачи, подзадачи и эпики.
     */
    @Override
    public void deleteAll() {
        for (Epic epic : epics.values()) {
            epic.getPrioritizedSubTasks().clear();
        }
        tasks.clear();
        subTasks.clear();
        epics.clear();
        prioritizedTasks.clear();
    }

    /**
     * Удаляет эпик по его идентификатору.
     * Если эпик содержит подзадачи, они также удаляются.
     *
     * @param id Идентификатор эпика для удаления
     */
    @Override
    public void deleteEpic(int id) {
        Epic epic = epics.get(id);
        if (epic == null) return;
        // Удаляем все подзадачи этого эпика
        for (SubTask subTask : new ArrayList<>(epic.getPrioritizedSubTasks())) {
            subTasks.remove(subTask.getId());
            prioritizedTasks.remove(subTask);
        }
        epic.getPrioritizedSubTasks().clear();
        epics.remove(id);
        prioritizedTasks.remove(epic);
    }

    @Override
    public void updateTask(Task taskToUpdate,
                           String newName,          // null = не обновлять
                           String newDescription,   // null = не обновлять
                           Status newStatus,        // null = не обновлять
                           String newStartTime, // null = не обновлять
                           Long newDuration) {
        // Проверка согласованности временных параметров
        if ((newStartTime == null && newDuration != null) ||
                (newStartTime != null && newDuration == null)) {
            throw new IllegalArgumentException("Нельзя обновлять только startTime или только Duration. " +
                    "Укажите оба параметра или оба null");
        }
        // Проверка пересечений (если время задано)
        if (newStartTime != null) {
            Task tempTask = taskToUpdate.createTempTask(taskToUpdate, newStartTime, newDuration);
            if (isExecutionOverlapInprioritizedTasks(tempTask)) {
                throw new TaskOverlapException();
            }
        }
        Optional.ofNullable(newName).ifPresent(taskToUpdate::setName);
        Optional.ofNullable(newDescription).ifPresent(taskToUpdate::setDescription);
        Optional.ofNullable(newStatus).ifPresent(taskToUpdate::setStatus);
        Optional.ofNullable(newStartTime)
                .map(timeStr -> LocalDateTime.parse(timeStr, DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")))
                .ifPresent(taskToUpdate::setStartTime);
        Optional.ofNullable(newDuration)
                .map(Duration::ofMinutes)
                .ifPresent(taskToUpdate::setDuration);
    }

    @Override
    public void updateSubTask(SubTask subTaskToUpdate,
                              String newName,          // null = не обновлять
                              String newDescription,   // null = не обновлять
                              Status newStatus,        // null = не обновлять
                              String newStartTime, // null = не обновлять
                              Long newDuration,
                              Epic newEpic) {
        if ((newStartTime == null && newDuration != null) ||
                (newStartTime != null && newDuration == null)) {
            throw new IllegalArgumentException("Нельзя обновлять только startTime или только Duration. " +
                    "Укажите оба параметра или оба null");
        }
        if (newStartTime != null) {
            SubTask tempSubTask = subTaskToUpdate.createTempSubTask(subTaskToUpdate, newStartTime, newDuration);
            if (isExecutionOverlapInprioritizedTasks(tempSubTask)) {
                throw new TaskOverlapException();
            }
        }
        // Обновление базовых полей
        Optional.ofNullable(newName).ifPresent(subTaskToUpdate::setName);
        Optional.ofNullable(newDescription).ifPresent(subTaskToUpdate::setDescription);
        Optional.ofNullable(newStatus).ifPresent(subTaskToUpdate::setStatus);
        Optional.ofNullable(newStartTime)
                .map(timeStr -> LocalDateTime.parse(timeStr, DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")))
                .ifPresent(subTaskToUpdate::setStartTime);
        Optional.ofNullable(newDuration)
                .map(Duration::ofMinutes)
                .ifPresent(subTaskToUpdate::setDuration);

        // Обработка эпика с Optional
        Optional.ofNullable(newEpic).ifPresentOrElse(
                // Если newEpic не null (эпик изменился)
                updatedEpic -> {
                    Epic currentEpic = subTaskToUpdate.getEpic();
                    currentEpic.getPrioritizedSubTasks().remove(subTaskToUpdate);
                    prioritizedTasks.remove(subTaskToUpdate);
                    currentEpic.updateEpicStatus(currentEpic);
                    currentEpic.updateEpicStatus(currentEpic);

                    subTaskToUpdate.setEpic(updatedEpic);
                    updatedEpic.getPrioritizedSubTasks().add(subTaskToUpdate);
                    prioritizedTasks.add(subTaskToUpdate);
                    updatedEpic.updateEpicStatus(updatedEpic);
                    updatedEpic.updateEpicTimes(updatedEpic);
                },
                // Если newEpic null (эпик не изменился)
                () -> {
                    Epic currentEpic = subTaskToUpdate.getEpic();
                    currentEpic.getPrioritizedSubTasks().remove(subTaskToUpdate);
                    currentEpic.getPrioritizedSubTasks().add(subTaskToUpdate);
                    currentEpic.updateEpicTimes(currentEpic);
                    currentEpic.updateEpicStatus(currentEpic);
                }
        );
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
        epic.getPrioritizedSubTasks().add(subTask);
        subTask.setEpic(epic);
        epic.updateEpicStatus(epic);
        epic.updateEpicTimes(epic);
    }

    @Override
    public String toString() {
        return "Задачи= " + tasks + '\n' +
                "Подзадачи= " + subTasks + '\n' +
                "Эпики= " + epics;
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    private boolean isExecutionOverlapInprioritizedTasks(Task taskToCheck) {
        boolean result = false;
        LocalDateTime startTimeTaskToCheck = (taskToCheck instanceof Epic) ? ((Epic) taskToCheck).getEpicStartTime() : taskToCheck.getStartTime();
        LocalDateTime endTimeTaskToCheck = (taskToCheck instanceof Epic) ? ((Epic) taskToCheck).getEpicEndTime() : taskToCheck.getEndTime();
        if (startTimeTaskToCheck == null || endTimeTaskToCheck == null) {
            return result;
        }

        // Собираем ID задач, которые нужно исключить из проверки
        Set<Integer> excludedIds = new HashSet<>();
        excludedIds.add(taskToCheck.getId()); // всегда исключаем саму задачу

        if (taskToCheck instanceof Epic) {
            // Для эпика исключаем все его подзадачи
            ((Epic) taskToCheck).getPrioritizedSubTasks()
                    .forEach(subTask -> excludedIds.add(subTask.getId()));
        } else if (taskToCheck instanceof SubTask) {
            // Для подзадачи исключаем ее эпик
            Epic epic = ((SubTask) taskToCheck).getEpic();
            if (epic != null) {
                excludedIds.add(epic.getId());
            }
        }
        return prioritizedTasks.stream()
                .filter(currentTask -> !excludedIds.contains(currentTask.getId()))
                .anyMatch(currentTask -> {
                    LocalDateTime currentTaskStartTime = (currentTask instanceof Epic) ? ((Epic) currentTask).getEpicStartTime() : currentTask.getStartTime();
                    LocalDateTime currentTaskEndTime = (currentTask instanceof Epic) ? ((Epic) currentTask).getEpicEndTime() : currentTask.getEndTime();
                    return currentTaskStartTime != null && currentTaskEndTime != null
                            && startTimeTaskToCheck.isBefore(currentTaskEndTime)
                            && currentTaskStartTime.isBefore(endTimeTaskToCheck);
                });
    }
}
