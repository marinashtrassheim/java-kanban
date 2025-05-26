package manager;

import exceptions.TaskOverlapException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Status;
import task.SubTask;
import task.Task;
import management.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public abstract class TaskManagerTest<T extends TaskManager> {
    protected T taskManager;

    protected abstract T createTaskManager();

    @BeforeEach
    public void setUp() {
        taskManager = createTaskManager(); // Сначала инициализируем менеджер

        // Затем создаем тестовые данные (если они нужны для всех тестов)
        Task task = new Task("Task", "Description", "16.03.2025 14:30", 120L);
        Epic epic = new Epic("Epic", "Description");
        SubTask subTask = new SubTask("SubTask", "Description", "15.03.2025 14:30", 120L, epic);

        try {
            taskManager.createTask(task);
            taskManager.createEpic(epic);
            taskManager.createSubTask(subTask);
        } catch (TaskOverlapException ignored) {}
    }
    @Test
    public void testCreateAndGetTask() {
        Task task = new Task("Test Task", "Test Description",
                "01.01.2023 10:00", 60L, Status.NEW);
        try {
            taskManager.createTask(task);
        } catch (exceptions.TaskOverlapException ignored) {}


        Task savedTask = taskManager.getTask(task.getId());

        assertNotNull(savedTask, "Задача не найдена");
        assertEquals(task, savedTask, "Задачи не совпадают");
    }

    @Test
    public void testCreateAndGetEpic() {
        Epic epic = new Epic("Test Epic", "Test Description");
        try {
        taskManager.createEpic(epic);
    } catch (exceptions.TaskOverlapException ignored) {}


        Epic savedEpic = taskManager.getEpic(epic.getId());

        assertNotNull(savedEpic, "Эпик не найден");
        assertEquals(epic, savedEpic, "Эпики не совпадают");
    }

    @Test
    public void testCreateAndGetSubTask() {
        Epic epic = new Epic("Test Epic", "Test Description");
        try {
            taskManager.createEpic(epic);
        } catch (exceptions.TaskOverlapException ignored) {}

        SubTask subTask = new SubTask("Test SubTask", "Test Description",
                "01.01.2023 10:00", 60L, epic);
        try {
            taskManager.createSubTask(subTask);
        } catch (exceptions.TaskOverlapException ignored) {}

        SubTask savedSubTask = taskManager.getSubTask(subTask.getId());

        assertNotNull(savedSubTask, "Подзадача не найдена");
        assertEquals(subTask, savedSubTask, "Подзадачи не совпадают");
        assertEquals(epic.getId(), savedSubTask.getEpic().getId(), "Эпик подзадачи не совпадает");
    }
    @Test
    public void testDeleteTask() {
        Task task = new Task("Test Task", "Test Description",
                "01.01.2023 10:00", 60L, Status.NEW);
        try {
            taskManager.createTask(task);
        } catch (exceptions.TaskOverlapException ignored) {}


        taskManager.deleteTask(task.getId());

        assertNull(taskManager.getTask(task.getId()), "Задача не удалена");
    }

    @Test
    public void testDeleteEpicWithSubTasks() {
        Epic epic = new Epic("Test Epic", "Test Description");
        try {
            taskManager.createEpic(epic);
        } catch (exceptions.TaskOverlapException ignored) {}

        SubTask subTask = new SubTask("Test SubTask", "Test Description",
                "01.01.2023 10:00", 60L, epic);
        try {
            taskManager.createSubTask(subTask);
        } catch (exceptions.TaskOverlapException ignored) {}

        taskManager.deleteEpic(epic.getId());

        assertNull(taskManager.getEpic(epic.getId()), "Эпик не удален");
        assertNull(taskManager.getSubTask(subTask.getId()), "Подзадача не удалена");
    }
    @Test
    public void testDeleteAll() {
        Task task = new Task("Test Task", "Test Description",
                "01.01.2023 10:00", 60L);
        try {
            taskManager.createTask(task);
        } catch (exceptions.TaskOverlapException ignored) {}

        Epic epic = new Epic("Test Epic", "Test Description");
        try {
            taskManager.createEpic(epic);
        } catch (exceptions.TaskOverlapException ignored) {}

        SubTask subTask = new SubTask("Test SubTask", "Test Description",
                "01.01.2023 10:00", 60L, epic);
        try {
            taskManager.createSubTask(subTask);
        } catch (exceptions.TaskOverlapException ignored) {}

        taskManager.deleteAll();

        assertTrue(taskManager.getAllTasks().isEmpty(), "Задачи не удалены");
        assertTrue(taskManager.getAllEpics().isEmpty(), "Эпики не удалены");
        assertTrue(taskManager.getAllSubTasks().isEmpty(), "Подзадачи не удалены");
    }
    @Test
    public void testUpdateTask() {
        Task task = new Task("Test Task", "Test Description",
                "01.01.2023 10:00", 60L, Status.NEW);
        try {
            taskManager.createTask(task);
        } catch (exceptions.TaskOverlapException ignored) {}

        try {
            taskManager.updateTask(task, "Updated Task", "Updated Description", Status.IN_PROGRESS,
                    "02.01.2023 10:00", 120L);
        } catch (exceptions.TaskOverlapException ignored) {}

        Task updatedTask = taskManager.getTask(task.getId());
        assertEquals("Updated Task", updatedTask.getName(), "Название задачи не обновлено");
        assertEquals("Updated Description", updatedTask.getDescription(), "Описание задачи не обновлено");
        assertEquals(Status.IN_PROGRESS, updatedTask.getStatus(), "Статус задачи не обновлен");
        assertEquals(LocalDateTime.of(2023, 1, 2, 10, 0), updatedTask.getStartTime(),
                "Время начала задачи не обновлено");
        assertEquals(Duration.ofMinutes(120), updatedTask.getDuration(), "Длительность задачи не обновлена");
    }

    @Test
    public void testUpdateSubTask() {
        Epic epic = new Epic("Test Epic", "Test Description");
        try {
            taskManager.createEpic(epic);
        } catch (exceptions.TaskOverlapException ignored) {}

        SubTask subTask = new SubTask("Test SubTask", "Test Description",
                "01.01.2023 10:00", 60L, epic);
        try {
            taskManager.createSubTask(subTask);
        } catch (exceptions.TaskOverlapException ignored) {}

        Epic newEpic = new Epic("New Epic", "New Description");
        try {
            taskManager.createEpic(newEpic);
        } catch (exceptions.TaskOverlapException ignored) {}

        try {
            taskManager.updateSubTask(subTask, "Updated SubTask", "Updated Description", Status.DONE,
                    "02.01.2023 10:00", 120L, newEpic);
        } catch (exceptions.TaskOverlapException ignored) {}

        SubTask updatedSubTask = taskManager.getSubTask(subTask.getId());
        assertEquals("Updated SubTask", updatedSubTask.getName(), "Название подзадачи не обновлено");
        assertEquals("Updated Description", updatedSubTask.getDescription(), "Описание подзадачи не обновлено");
        assertEquals(Status.DONE, updatedSubTask.getStatus(), "Статус подзадачи не обновлён");
        assertEquals(LocalDateTime.of(2023, 1, 2, 10, 0), updatedSubTask.getStartTime(),
                "Время начала подзадачи не обновлено");
        assertEquals(Duration.ofMinutes(120), updatedSubTask.getDuration(), "Длительность подзадачи не обновлена");
        assertEquals(newEpic.getId(), updatedSubTask.getEpic().getId(), "Эпик подзадачи не обновлён");
    }

    @Test
    public void testUpdateEpic() {
        Epic epic = new Epic("Test Epic", "Test Description");
        try {
        taskManager.createEpic(epic);
    } catch (exceptions.TaskOverlapException ignored) {}


        Epic updatedEpic = new Epic("Updated Epic", "Updated Description");
        taskManager.updateEpic(epic, updatedEpic);

        Epic savedEpic = taskManager.getEpic(epic.getId());
        assertEquals("Updated Epic", savedEpic.getName(), "Название эпика не обновлено");
        assertEquals("Updated Description", savedEpic.getDescription(), "Описание эпика не обновлено");
    }

    @Test
    public void testAddSubtaskToEpic() {
        Epic epic = new Epic("Test Epic", "Test Description");
        try {
        taskManager.createEpic(epic);
    } catch (exceptions.TaskOverlapException ignored) {}


        SubTask subTask = new SubTask("Test SubTask", "Test Description",
                "01.01.2023 10:00", 60L, epic);
        try {
        taskManager.createSubTask(subTask);
    } catch (exceptions.TaskOverlapException ignored) {}



        assertTrue(epic.getPrioritizedSubTasks().contains(subTask), "Подзадача не добавлена в эпик");
        assertEquals(epic.getId(), subTask.getEpic().getId(), "Эпик не установлен для подзадачи");
    }

    @Test
    public void testTaskOverlap() {
        Task task1 = new Task("Task 1", "Description",
                "01.01.2023 10:00", 60L, Status.NEW);
        try {
            taskManager.createTask(task1);
        } catch (exceptions.TaskOverlapException ignored) {}

        Task task2 = new Task("Task 2", "Description",
                "01.01.2023 10:30", 60L, Status.NEW);

        assertThrows(TaskOverlapException.class, () -> taskManager.createTask(task2),
                "Ожидалось исключение при пересечении задач");
    }

    @Test
    public void testEpicStatusWithAllNewSubtasks() {
        Epic epic = new Epic("Test Epic", "Test Description");
        try {
        taskManager.createEpic(epic);
    } catch (exceptions.TaskOverlapException ignored) {}


        SubTask subTask1 = new SubTask("SubTask 1", "Description",
                "01.01.2023 10:00", 60L, epic);
        SubTask subTask2 = new SubTask("SubTask 2", "Description",
                "01.01.2023 11:00", 60L, epic);
        try {
            taskManager.createSubTask(subTask1);
        } catch (exceptions.TaskOverlapException ignored) {}
        try {
            taskManager.createSubTask(subTask1);
        } catch (exceptions.TaskOverlapException ignored) {}


        assertEquals(Status.NEW, epic.getStatus(), "Статус эпика должен быть NEW");
    }

    @Test
    public void testEpicStatusWithAllDoneSubtasks() {
        Epic epic = new Epic("Test Epic", "Test Description");
        try {
            taskManager.createEpic(epic);
        } catch (exceptions.TaskOverlapException ignored) {}

        SubTask subTask1 = new SubTask("SubTask 1", "Description", "01.01.2023 10:00", 60L, epic);
        SubTask subTask2 = new SubTask("SubTask 2", "Description", "01.01.2023 11:00", 60L, epic);

        // Создаем подзадачи и устанавливаем их статус как DONE
        try {
            taskManager.createSubTask(subTask1);
            taskManager.updateSubTask(subTask1, null, null, Status.DONE, null, null, null);
        } catch (exceptions.TaskOverlapException | IllegalArgumentException ex) {
            fail("Исключение не ожидалось при обновлении первой подзадачи.");
        }

        try {
            taskManager.createSubTask(subTask2);
            taskManager.updateSubTask(subTask2, null, null, Status.DONE, null, null, null);
        } catch (exceptions.TaskOverlapException | IllegalArgumentException ex) {
            fail("Исключение не ожидалось при обновлении второй подзадачи.");
        }

        // Проверяем, что статус эпика стал DONE
        Epic retrievedEpic = taskManager.getEpic(epic.getId());
        assertEquals(Status.DONE, retrievedEpic.getStatus(), "Статус эпика должен быть DONE");
    }

    @Test
    public void testEpicStatusWithNewAndDoneSubtasks() {
        Epic epic = new Epic("Test Epic", "Test Description");
        try {
            taskManager.createEpic(epic);
        } catch (exceptions.TaskOverlapException ignored) {}

        SubTask subTask1 = new SubTask("SubTask 1", "Description", "01.01.2023 10:00", 60L, epic);
        SubTask subTask2 = new SubTask("SubTask 2", "Description", "01.01.2023 11:00", 60L, epic);

        // Создаем первую подзадачу (оставляем NEW)
        try {
            taskManager.createSubTask(subTask1);
        } catch (exceptions.TaskOverlapException ignored) {}

        // Создаем вторую подзадачу и ставим ей статус DONE
        try {
            taskManager.createSubTask(subTask2);
            taskManager.updateSubTask(subTask2, null, null, Status.DONE, null, null, null);
        } catch (exceptions.TaskOverlapException | IllegalArgumentException ex) {
            fail("Исключение не ожидалось при обновлении второй подзадачи.");
        }

        // Проверяем, что статус эпика изменился на IN_PROGRESS
        Epic retrievedEpic = taskManager.getEpic(epic.getId());
        assertEquals(Status.IN_PROGRESS, retrievedEpic.getStatus(), "Статус эпика должен быть IN_PROGRESS");
    }

    @Test
    public void testEpicStatusWithNoSubtasks() {
        Epic epic = new Epic("Test Epic", "Test Description");
        try {
        taskManager.createEpic(epic);
    } catch (exceptions.TaskOverlapException ignored) {}


        assertEquals(Status.NEW, epic.getStatus(), "Статус эпика без подзадач должен быть NEW");
    }

    @Test
    public void testEpicTimeCalculation() {
        Epic epic = new Epic("Test Epic", "Test Description");
        try {
        taskManager.createEpic(epic);
    } catch (exceptions.TaskOverlapException ignored) {}


        SubTask subTask1 = new SubTask("SubTask 1", "Description",
                "01.01.2023 10:00", 60L, epic);
        SubTask subTask2 = new SubTask("SubTask 2", "Description",
                "01.01.2023 12:00", 30L, epic);

        try {
            taskManager.createSubTask(subTask1);
        } catch (exceptions.TaskOverlapException ignored) {}
        try {
            taskManager.createSubTask(subTask2);
        } catch (exceptions.TaskOverlapException ignored) {}

        assertEquals(subTask1.getStartTime(), epic.getEpicStartTime(), "Неверное время начала эпика");
        assertEquals(subTask2.getEndTime(), epic.getEpicEndTime(), "Неверное время окончания эпика");
        assertEquals(Duration.ofMinutes(90), epic.getEpicDuration(), "Неверная длительность эпика");
    }

}