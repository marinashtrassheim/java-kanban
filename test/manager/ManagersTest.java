package manager;

import static org.junit.jupiter.api.Assertions.*;

import management.*;
import org.junit.jupiter.api.Test;

class ManagersTest {
    @Test
    void getDefault_shouldReturnInitializedTaskManagerInstance() {
        // Проверяем, что getDefault() возвращает не-null объект
        TaskManager taskManager = Managers.getDefault();
        assertNotNull(taskManager, "Менеджер задач не должен быть null");

        // Проверяем, что возвращается именно InMemoryTaskManager (на текущий момент)
        assertEquals(InMemoryTaskManager.class, taskManager.getClass(),
                "Должен возвращаться InMemoryTaskManager");
    }

    @Test
    void getDefaultHistory_shouldReturnInitializedHistoryManagerInstance() {
        // Проверяем, что getDefaultHistory() возвращает не-null объект
        HistoryManager historyManager = Managers.getDefaultHistory();
        assertNotNull(historyManager, "Менеджер истории не должен быть null");

        // Проверяем, что возвращается именно InMemoryHistoryManager
        assertEquals(InMemoryHistoryManager.class, historyManager.getClass(),
                "Должен возвращаться InMemoryHistoryManager");
    }
}