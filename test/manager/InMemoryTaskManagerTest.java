package manager;

import management.InMemoryTaskManager;
import org.junit.jupiter.api.Test;
import task.Status;
import task.Task;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {
    @Override
    protected InMemoryTaskManager createTaskManager() {
        return new InMemoryTaskManager(); // Реализация создания менеджера
    }

    @Test
    public void testIsExecutionOverlapInprioritizedTasks_NoOverlap() {

        Task task1 = new Task("Task 1", "Description",
                "15.03.2023 10:00", 60L, Status.NEW);
        try {
            taskManager.createTask(task1);
        } catch (exceptions.TaskOverlapException ignored) {}

        Task task2 = new Task("Task 1", "Description",
                "16.03.2023 10:00", 60L, Status.NEW);
        try {
            taskManager.createTask(task2);
        } catch (exceptions.TaskOverlapException ignored) {}

        assertFalse(taskManager.isExecutionOverlapInprioritizedTasks(task2));
    }
}