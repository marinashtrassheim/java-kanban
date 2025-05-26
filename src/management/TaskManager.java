package management;

import exceptions.TaskOverlapException;
import task.Epic;
import task.Status;
import task.SubTask;
import task.Task;

import java.time.Duration;
import java.util.*;

public interface TaskManager {
    void createTask(Task task) throws TaskOverlapException;

    void createSubTask(SubTask subTask) throws TaskOverlapException;

    void createEpic(Epic epic) throws TaskOverlapException;

    HashMap<Integer, Task> getAllTasks();

    HashMap<Integer, Epic> getAllEpics();

    HashMap<Integer, SubTask> getAllSubTasks();

    Task getTask(int id);

    SubTask getSubTask(int id);

    Epic getEpic(int id);

    void deleteTask(int id);

    void deleteSubTask(int id);

    void deleteAll();

    void deleteEpic(int id);

    void updateTask(Task taskToUpdate,
                    String newName,          // null = не обновлять
                    String newDescription,   // null = не обновлять
                    Status newStatus,        // null = не обновлять
                    String newStartTime, // null = не обновлять
                    Long newDuration) throws TaskOverlapException;

    void updateSubTask(SubTask subTaskToUpdate,
                       String newName,          // null = не обновлять
                       String newDescription,   // null = не обновлять
                       Status newStatus,        // null = не обновлять
                       String newStartTime, // null = не обновлять
                       Long newDuration,
                       Epic newEpic) throws TaskOverlapException;

    void updateEpic(Epic currentEpic, Epic updatedEpic);

    /**
     * Метод для обновления статуса эпика на основе статусов его подзадач.
     * Логика обновления:
     * 1. Если список подзадач пуст или все подзадачи имеют статус NEW, то статус эпика — NEW.
     * 2. Если все подзадачи имеют статус DONE, то статус эпика — DONE.
     * 3. В остальных случаях статус эпика — IN_PROGRESS.
     */
    default void updateEpicStatus(Epic epic) {
        TreeSet<SubTask> prioritizedSubTasks = epic.getPrioritizedSubTasks();
        if (prioritizedSubTasks == null || prioritizedSubTasks.isEmpty()) {
            epic.setStatus(Status.NEW); // Если подзадач нет, статус NEW
            return;
        }

        boolean allDone = true; // Предполагаем, что все подзадачи завершены
        boolean allNew = true;  // Предполагаем, что все подзадачи новые
        // Проверяем статусы всех подзадач
        for (SubTask subTask : prioritizedSubTasks) {
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

    public default void updateEpicTimes(Epic epic) {
        TreeSet<SubTask> prioritizedTasks = epic.getPrioritizedSubTasks();
        if (prioritizedTasks == null || prioritizedTasks.isEmpty()) {
            epic.setEpicStartTime(null);
            epic.setEpicEndTime(null);
            epic.setEpicDuration(null);
            return;
        }

        SubTask first = prioritizedTasks.first();
        epic.setEpicStartTime(first.getStartTime());

        SubTask last = prioritizedTasks.last();
        epic.setEpicEndTime(last.getEndTime());

        Duration duration = prioritizedTasks.stream()
                .map(SubTask::getDuration)
                .filter(Objects::nonNull)
                .reduce(Duration.ZERO, Duration::plus);
        epic.setEpicDuration(duration);
    }

    void addSubtaskToEpic(Epic epic, SubTask subTask);

    List<Task> getHistory();
}
