package management;

import task.Epic;
import task.Status;
import task.SubTask;
import task.Task;

import java.util.*;

public interface TaskManager {
    void createTask(Task task);

    void createSubTask(SubTask subTask);

    void createEpic(Epic epic);

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
                    Long newDuration);

    void updateSubTask(SubTask subTaskToUpdate,
                       String newName,          // null = не обновлять
                       String newDescription,   // null = не обновлять
                       Status newStatus,        // null = не обновлять
                       String newStartTime, // null = не обновлять
                       Long newDuration,
                       Epic newEpic);

    void updateEpic(Epic currentEpic, Epic updatedEpic);

    void addSubtaskToEpic(Epic epic, SubTask subTask);

    List<Task> getHistory();
}
