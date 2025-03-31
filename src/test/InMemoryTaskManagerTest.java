package test;

import static org.junit.jupiter.api.Assertions.*;

import management.InMemoryTaskManager;
import management.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.*;
import java.util.HashMap;

class InMemoryTaskManagerTest {
    private TaskManager taskManager;
    private Task task;
    private Epic epic;
    private SubTask subTask;
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, SubTask> subTasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();

    @BeforeEach
    void setUp() {
        taskManager = new InMemoryTaskManager();

        // Создаем тестовые данные
        task = new Task("Task", "Description");
        epic = new Epic("Epic", "Description");
        subTask = new SubTask("SubTask", "Description", epic);

        // Добавляем в менеджер
        taskManager.createTask(task);
        taskManager.createEpic(epic);
        taskManager.createSubTask(subTask);
    }
    /* Тесты для create-методов */
    @Test
    void createTask_shouldAddTaskToTasks() {
        assertFalse(taskManager.getAllTasks().isEmpty(), "Список задач не должен быть пустым после создания");
    }
    @Test
    void createEpic_shouldAddEpicToEpics() {
        assertFalse(taskManager.getAllEpics().isEmpty(), "Список задач не должен быть пустым после создания");
    }
    @Test
    void createSubTask_shouldAddSubTaskToSubTasks() {
        assertFalse(taskManager.getAllSubTasks().isEmpty(), "Список задач не должен быть пустым после создания");
    }
    /* Тесты для get-методов */
    @Test
    void getTask_shouldReturnCorrectTask() {
        Task testTask = taskManager.getTask(task.getId());
        assertEquals(task, testTask, "Должна вернуться тестовая задача");
    }
    @Test
    void getEpic_shouldReturnCorrectEpic() {
        Epic testEpic = taskManager.getEpic(epic.getId());
        assertEquals(epic, testEpic, "Должна вернуться тестовая задача");
    }
    @Test
    void getSubTask_shouldReturnCorrectSubTask() {
        SubTask testSubtask = taskManager.getSubTask(subTask.getId());
        assertEquals(subTask, testSubtask, "Должна вернуться тестовая задача");
    }
    /* Тесты на неизменность задачи при добавлении ее в менеджер */
    @Test
    void taskFields_shouldRemainUnchangedAfterAddingToManager() {
        // Создаем задачу и запоминаем её состояние
        Task originalTask = new Task("Original name", "Original description");
        originalTask.setStatus(Status.IN_PROGRESS);
        String originalName = originalTask.getName();
        String originalDescription = originalTask.getDescription();
        Status originalStatus = originalTask.getStatus();

        // Добавляем задачу в менеджер
        taskManager.createTask(originalTask);

        // Проверяем что все поля остались неизменными
        assertEquals(originalName, originalTask.getName(), "Название задачи не должно измениться");
        assertEquals(originalDescription, originalTask.getDescription(), "Описание задачи не должно измениться");
        assertEquals(originalStatus, originalTask.getStatus(), "Статус задачи не должен измениться");
    }
    @Test
    void epicFields_shouldRemainUnchangedAfterAddingToManager() {
        // Создаем задачу и запоминаем её состояние
        Epic originalEpic = new Epic("Original name", "Original description");
        String originalName = originalEpic.getName();
        String originalDescription = originalEpic.getDescription();
        Status originalStatus = originalEpic.getStatus();

        // Добавляем задачу в менеджер
        taskManager.createTask(originalEpic);

        // Проверяем что все поля остались неизменными
        assertEquals(originalName, originalEpic.getName(), "Название задачи не должно измениться");
        assertEquals(originalDescription, originalEpic.getDescription(), "Описание задачи не должно измениться");
        assertEquals(originalStatus, originalEpic.getStatus(), "Статус задачи не должен измениться");
    }
    @Test
    void subTaskFields_shouldRemainUnchangedAfterAddingToManager() {
        // Создаем задачу и запоминаем её состояние
        SubTask originalSubTask = new SubTask("Original name", "Original description", epic);
        String originalName = originalSubTask.getName();
        String originalDescription = originalSubTask.getDescription();
        Status originalStatus = originalSubTask.getStatus();
        Epic originalEpic = originalSubTask.getEpic();

        // Добавляем задачу в менеджер
        taskManager.createTask(originalEpic);

        // Проверяем что все поля остались неизменными
        assertEquals(originalName, originalSubTask.getName(), "Название задачи не должно измениться");
        assertEquals(originalDescription, originalSubTask.getDescription(), "Описание задачи не должно измениться");
        assertEquals(originalStatus, originalSubTask.getStatus(), "Статус задачи не должен измениться");
        assertEquals(originalEpic, originalEpic, "Эпик задачи не должен измениться");
    }

}