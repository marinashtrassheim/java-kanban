package manager;

import exceptions.TaskOverlapException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import task.Epic;
import task.Status;
import task.SubTask;
import task.Task;
import management.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


import static management.CSVFormat.taskFromString;
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
        taskManager.createTask(task);
        taskManager.createEpic(epic);
        taskManager.createSubTask(subTask);
    }

    @Test
    public void testCreateAndGetTask() {
        Task task = new Task("Test Task", "Test Description",
                "01.01.2023 10:00", 60L, Status.NEW);
        taskManager.createTask(task);
        Task savedTask = taskManager.getTask(task.getId());
        assertNotNull(savedTask, "Задача не найдена");
        assertEquals(task, savedTask, "Задачи не совпадают");
    }

    @Test
    public void testCreateAndGetEpic() {
        Epic epic = new Epic("Test Epic", "Test Description");
        taskManager.createEpic(epic);
        Epic savedEpic = taskManager.getEpic(epic.getId());
        assertNotNull(savedEpic, "Эпик не найден");
        assertEquals(epic, savedEpic, "Эпики не совпадают");
    }

    @Test
    public void testCreateAndGetSubTask() {
        Epic epic = new Epic("Test Epic", "Test Description");
        taskManager.createEpic(epic);
        SubTask subTask = new SubTask("Test SubTask", "Test Description",
                "01.01.2023 10:00", 60L, epic);
        taskManager.createSubTask(subTask);
        SubTask savedSubTask = taskManager.getSubTask(subTask.getId());
        assertNotNull(savedSubTask, "Подзадача не найдена");
        assertEquals(subTask, savedSubTask, "Подзадачи не совпадают");
        assertEquals(epic.getId(), savedSubTask.getEpic().getId(), "Эпик подзадачи не совпадает");
    }

    @Test
    public void testDeleteTask() {
        Task task = new Task("Test Task", "Test Description",
                "01.01.2023 10:00", 60L, Status.NEW);
        taskManager.createTask(task);
        taskManager.deleteTask(task.getId());
        assertNull(taskManager.getTask(task.getId()), "Задача не удалена");
    }

    @Test
    public void testDeleteEpicWithSubTasks() {
        Epic epic = new Epic("Test Epic", "Test Description");
        taskManager.createEpic(epic);
        SubTask subTask = new SubTask("Test SubTask", "Test Description",
                "01.01.2023 10:00", 60L, epic);
        taskManager.createSubTask(subTask);
        taskManager.deleteEpic(epic.getId());
        assertNull(taskManager.getEpic(epic.getId()), "Эпик не удален");
        assertNull(taskManager.getSubTask(subTask.getId()), "Подзадача не удалена");
    }

    @Test
    public void testDeleteAll() {
        Task task = new Task("Test Task", "Test Description",
                "01.01.2023 10:00", 60L);
        taskManager.createTask(task);
        Epic epic = new Epic("Test Epic", "Test Description");
        taskManager.createEpic(epic);
        SubTask subTask = new SubTask("Test SubTask", "Test Description",
                "01.01.2023 15:00", 60L, epic);
        taskManager.createSubTask(subTask);
        taskManager.deleteAll();
        assertTrue(taskManager.getAllTasks().isEmpty(), "Задачи не удалены");
        assertTrue(taskManager.getAllEpics().isEmpty(), "Эпики не удалены");
        assertTrue(taskManager.getAllSubTasks().isEmpty(), "Подзадачи не удалены");
    }

    @Test
    public void testUpdateTask() {
        Task task = new Task("Test Task", "Test Description",
                "01.01.2023 10:00", 60L, Status.NEW);
        taskManager.createTask(task);
        taskManager.updateTask(task, "Updated Task", "Updated Description", Status.IN_PROGRESS,
                "02.01.2023 10:00", 120L);
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
        taskManager.createEpic(epic);
        SubTask subTask = new SubTask("Test SubTask", "Test Description",
                "01.01.2023 10:00", 60L, epic);
        taskManager.createSubTask(subTask);
        Epic newEpic = new Epic("New Epic", "New Description");
        taskManager.createEpic(newEpic);
        taskManager.updateSubTask(subTask, "Updated SubTask", "Updated Description", Status.DONE,
                "02.01.2023 10:00", 120L, newEpic);
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
        taskManager.createEpic(epic);
        Epic updatedEpic = new Epic("Updated Epic", "Updated Description");
        taskManager.updateEpic(epic, updatedEpic);
        Epic savedEpic = taskManager.getEpic(epic.getId());
        assertEquals("Updated Epic", savedEpic.getName(), "Название эпика не обновлено");
        assertEquals("Updated Description", savedEpic.getDescription(), "Описание эпика не обновлено");
    }

    @Test
    public void testAddSubtaskToEpic() {
        Epic epic = new Epic("Test Epic", "Test Description");
        taskManager.createEpic(epic);
        SubTask subTask = new SubTask("Test SubTask", "Test Description",
                "01.01.2023 10:00", 60L, epic);
        taskManager.createSubTask(subTask);
        assertTrue(epic.getPrioritizedSubTasks().contains(subTask), "Подзадача не добавлена в эпик");
        assertEquals(epic.getId(), subTask.getEpic().getId(), "Эпик не установлен для подзадачи");
    }

    @Test
    public void testTaskOverlap() {
        Task task1 = new Task("Task 1", "Description",
                "01.01.2023 10:00", 60L, Status.NEW);
        taskManager.createTask(task1);
        Task task2 = new Task("Task 2", "Description",
                "01.01.2023 10:30", 60L, Status.NEW);
        assertThrows(TaskOverlapException.class, () -> taskManager.createTask(task2),
                "Ожидалось исключение при пересечении задач");
    }

    @Test
    public void testEpicStatusWithAllNewSubtasks() {
        Epic epic = new Epic("Test Epic", "Test Description");
        taskManager.createEpic(epic);
        SubTask subTask1 = new SubTask("SubTask 1", "Description",
                "01.01.2023 10:00", 60L, epic);
        SubTask subTask2 = new SubTask("SubTask 2", "Description",
                "01.01.2023 11:00", 60L, epic);
        taskManager.createSubTask(subTask1);
        taskManager.createSubTask(subTask1);
        assertEquals(Status.NEW, epic.getStatus(), "Статус эпика должен быть NEW");
    }

    @Test
    public void testEpicStatusWithAllDoneSubtasks() {
        Epic epic = new Epic("Test Epic", "Test Description");
        taskManager.createEpic(epic);
        SubTask subTask1 = new SubTask("SubTask 1", "Description", "01.01.2023 10:00", 60L, epic);
        SubTask subTask2 = new SubTask("SubTask 2", "Description", "01.01.2023 11:00", 60L, epic);

        // Создаем подзадачи и устанавливаем их статус как DONE
        taskManager.createSubTask(subTask1);
        taskManager.updateSubTask(subTask1, null, null, Status.DONE, null, null, null);
        taskManager.createSubTask(subTask2);
        taskManager.updateSubTask(subTask2, null, null, Status.DONE, null, null, null);
        // Проверяем, что статус эпика стал DONE
        Epic retrievedEpic = taskManager.getEpic(epic.getId());
        assertEquals(Status.DONE, retrievedEpic.getStatus(), "Статус эпика должен быть DONE");
    }

    @Test
    public void testEpicStatusWithNewAndDoneSubtasks() {
        Epic epic = new Epic("Test Epic", "Test Description");
        taskManager.createEpic(epic);
        SubTask subTask1 = new SubTask("SubTask 1", "Description", "01.01.2023 10:00", 60L, epic);
        SubTask subTask2 = new SubTask("SubTask 2", "Description", "01.01.2023 11:00", 60L, epic);

        // Создаем первую подзадачу (оставляем NEW)
        taskManager.createSubTask(subTask1);
        // Создаем вторую подзадачу и ставим ей статус DONE
        taskManager.createSubTask(subTask2);
        taskManager.updateSubTask(subTask2, null, null, Status.DONE, null, null, null);
        // Проверяем, что статус эпика изменился на IN_PROGRESS
        Epic retrievedEpic = taskManager.getEpic(epic.getId());
        assertEquals(Status.IN_PROGRESS, retrievedEpic.getStatus(), "Статус эпика должен быть IN_PROGRESS");
    }

    @Test
    public void testEpicStatusWithNoSubtasks() {
        Epic epic = new Epic("Test Epic", "Test Description");
        taskManager.createEpic(epic);
        assertEquals(Status.NEW, epic.getStatus(), "Статус эпика без подзадач должен быть NEW");
    }

    @Test
    public void testEpicTimeCalculation() {
        Epic epic = new Epic("Test Epic", "Test Description");
        taskManager.createEpic(epic);
        SubTask subTask1 = new SubTask("SubTask 1", "Description",
                "01.01.2023 10:00", 60L, epic);
        SubTask subTask2 = new SubTask("SubTask 2", "Description",
                "01.01.2023 12:00", 30L, epic);
        taskManager.createSubTask(subTask1);
        taskManager.createSubTask(subTask2);
        assertEquals(subTask1.getStartTime(), epic.getEpicStartTime(), "Неверное время начала эпика");
        assertEquals(subTask2.getEndTime(), epic.getEpicEndTime(), "Неверное время окончания эпика");
        assertEquals(Duration.ofMinutes(90), epic.getEpicDuration(), "Неверная длительность эпика");
    }

    @Test
    void testLoadFromFile_ShouldCorrectlyRestoreTasks(@TempDir Path tempDir) throws IOException {
        // 1. Подготовка тестового файла
        File testFile = tempDir.resolve("test_tasks.csv").toFile();
        String testData =
                "id,type,name,status,description,startTime,duration,epic\n" +
                        "1,TASK,Task11,NEW,description,15.11.2023 14:30,120,\n" +  // Задача
                        "2,EPIC,Epic11,NEW,desc,17.11.2023 14:30,60,\n" +       // Эпик
                        "3,SUBTASK,SubTask11,NEW,subDesc,17.11.2023 14:30,60,2";   // Подзадача

        Files.writeString(testFile.toPath(), testData);

        // 2. Загрузка данных из файла
        FileBackedTaskManager loadedManager = new FileBackedTaskManager(testFile.getPath());
        FileBackedTaskManager manager = loadedManager.loadFromFile(testFile);

        // 3. Проверки
        // Проверяем, что все задачи загрузились
        assertEquals(1, manager.getAllTasks().size(), "Должна быть ровно одна обычная задача");
        assertEquals(1, manager.getAllEpics().size(), "Должен быть ровно один эпик");
        assertEquals(1, manager.getAllSubTasks().size(), "Должна быть ровно одна подзадача");

        // Проверяем загруженную задачу
        Task loadedTask = manager.getTask(1);
        assertNotNull(loadedTask, "Обычная задача не должна быть null");
        assertEquals("Task11", loadedTask.getName(), "Название задачи неправильное");
        assertEquals(Status.NEW, loadedTask.getStatus(), "Статус задачи должен быть NEW");
        assertEquals(LocalDateTime.parse("15.11.2023 14:30", DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")),
                loadedTask.getStartTime(), "Начало задачи неправильное");
        assertEquals(Duration.ofMinutes(120), loadedTask.getDuration(), "Продолжительность задачи неверна");

        // Проверяем загруженный эпик
        Epic loadedEpic = manager.getEpic(2);
        assertNotNull(loadedEpic, "Эпик не должен быть null");
        assertEquals("Epic11", loadedEpic.getName(), "Название эпика неправильное");
        assertEquals(Status.NEW, loadedEpic.getStatus(), "Статус эпика должен быть NEW");
        // Проверяем загруженную подзадачу
        SubTask loadedSubTask = manager.getSubTask(3);
        assertNotNull(loadedSubTask, "Подзадача не должна быть null");
        assertEquals("SubTask11", loadedSubTask.getName(), "Название подзадачи неправильное");
        assertEquals(Status.NEW, loadedSubTask.getStatus(), "Статус подзадачи должен быть NEW");
        manager.addSubtaskToEpic(loadedEpic, loadedSubTask);
        assertEquals(loadedEpic, loadedSubTask.getEpic(), "Подзадача должна быть связана с правильным эпиком");
        assertEquals(LocalDateTime.parse("17.11.2023 14:30", DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")),
                loadedEpic.getStartTime(), "Начало эпика неправильное");
        assertEquals(Duration.ofMinutes(60), loadedEpic.getDuration(), "Продолжительность эпика неверна");

        assertEquals(LocalDateTime.parse("17.11.2023 14:30", DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")),
                loadedSubTask.getStartTime(), "Начало подзадачи неправильное");
        assertEquals(Duration.ofMinutes(60), loadedSubTask.getDuration(), "Продолжительность подзадачи неверна");
    }

    @Test
    void testCreateTaskAndSave() throws Exception {
        Path tempFilePath = Files.createTempFile("test_tasks.csv", ".csv");
        FileBackedTaskManager manager = new FileBackedTaskManager(tempFilePath.toString());
        // Создаем простую задачу
        Task task = new Task("Task11", "description",
                "15.11.2023 14:30", 120L, Status.NEW);
        // Добавляем задачу в менеджер
        manager.createTask(task);

        // Читаем содержимое файла
        List<String> savedContent = Files.readAllLines(tempFilePath);
        String taskToCheckInStr = "1,TASK,Task11,NEW,description,15.11.2023 14:30,120,";
        Task taskToCheck = taskFromString(manager, taskToCheckInStr);

        // Проверяем наличие задачи
        assertEquals(taskToCheck, task);
        assertTrue(savedContent.contains(CSVFormat.taskToCSVString(task)));

        // Дополнительно проверяем, что задача присутствует в менеджере
        assertEquals(1, manager.getAllTasks().size());
        Files.deleteIfExists(tempFilePath);
    }
}