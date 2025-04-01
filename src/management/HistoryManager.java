package management;

import task.Task;
import java.util.List;

public interface HistoryManager {

    List<Task> getHistory();

    void addToHistory(Task task);
}
