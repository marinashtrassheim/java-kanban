package management;

import task.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager{

    private List<Task> history = new ArrayList<>();

    @Override
    public List<Task> getHistory() {
        return history;
    }

    @Override
    public void addToHistory(Task task) {
        if (history.size() >= 10) {
            history.remove(0);
        }
        // Создаем копию задачи для истории
        Task taskCopy = new Task(task.getName(), task.getDescription());
        taskCopy.setId(task.getId());
        taskCopy.setStatus(task.getStatus());
        history.add(taskCopy);
    }
}
