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

    @Test
    void shouldNotContainDuplicates() {
        historyManager.addToHistory(task);
        historyManager.addToHistory(task); // Дублирующее добавление

        List<Task> history = historyManager.getHistory();
        assertEquals(1, history.size(), "История не должна содержать дубликатов");
    }

    @Test
    void shouldRemoveTaskFromHistory() {
        historyManager.addToHistory(task);
        ((InMemoryHistoryManager) historyManager).remove(task.getId());

        assertTrue(historyManager.getHistory().isEmpty(), "История должна быть пустой после удаления");
    }


}