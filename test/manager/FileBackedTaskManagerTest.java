package manager;

import management.InMemoryTaskManager;

public class FileBackedTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {
    protected InMemoryTaskManager taskManager;

    @Override
    protected InMemoryTaskManager createTaskManager() {
        return new InMemoryTaskManager();
    }
}