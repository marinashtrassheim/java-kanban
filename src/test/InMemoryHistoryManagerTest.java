package test;

import static org.junit.jupiter.api.Assertions.*;
import management.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.*;

import java.util.List;

class InMemoryHistoryManagerTest {
    private HistoryManager historyManager;
    private TaskManager taskManager;
    private Task task;

    @BeforeEach
    void setUp() {
        historyManager = new InMemoryHistoryManager();
        taskManager = Managers.getDefault();
        task = new Task("Test Task", "Test Description");
        taskManager.createTask(task);
    }
    /* Базовый тест на добавление */
    @Test
    void add_shouldAddTaskToHistory() {
        historyManager.addToHistory(task);
        final List<Task> history = historyManager.getHistory();

        assertFalse(historyManager.getHistory().isEmpty(), "Список не должен быть пустым");
        assertEquals(1, history.size(), "История должна содержать 1 задачу");
    }
    /* Проверка сохранения предыдущей версии задачи */
    @Test
    void shouldKeepPreviousTaskVersionInHistory() {
        // Первое добавление задачи в историю
        historyManager.addToHistory(task);

        // Изменяем задачу
        task.setName("Modified Name");

        // Второе добавление (теперь с измененными данными)
        historyManager.addToHistory(task);

        List<Task> history = historyManager.getHistory();

        assertEquals("Test Task", history.get(0).getName(), "Первая версия должна сохранить оригинальное имя");
        assertEquals("Modified Name", history.get(1).getName(), "Вторая версия должна содержать измененное имя");
        // Проверяем что в истории две разные версии задачи

    }

}