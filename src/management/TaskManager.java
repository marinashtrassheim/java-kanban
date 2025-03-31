package management;

import task.Epic;
import task.Status;
import task.SubTask;
import task.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface TaskManager {
    void createTask(Task task);

    void createSubTask(SubTask subTask);

    void createEpic(Epic epic);

    HashMap<Integer, Task> getAllTasks();

    HashMap<Integer, Epic> getAllEpics();

    HashMap<Integer, SubTask> getAllSubTasks();

    ArrayList<SubTask> getSubTasksByEpicId(int epicId);

    Task getTask(int id);

    Epic getEpic(int id);

    SubTask getSubTask(int id);

    void deleteTask(int id);

    void deleteSubTask(int id);

    void deleteAll();

    void deleteEpic(int id);

    void updateTask(Task currentTask, Task updatedTask);

    void updateSubTask(SubTask currentSubTask, SubTask updatedSubTask);

    void updateEpic(Epic currentEpic, Epic updatedEpic);

    /**
     * Метод для обновления статуса эпика на основе статусов его подзадач.
     * Логика обновления:
     * 1. Если список подзадач пуст или все подзадачи имеют статус NEW, то статус эпика — NEW.
     * 2. Если все подзадачи имеют статус DONE, то статус эпика — DONE.
     * 3. В остальных случаях статус эпика — IN_PROGRESS.
     */
    default void updateEpicStatus(Epic epic) {
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

    void addSubtaskToEpic(Epic epic, SubTask subTask);

    List<Task> getHistory();
}
