import task.*;
import management.*;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        TaskManager taskManager = Managers.getDefault();

        Task task1 = new Task("Task1", "description");
        taskManager.createTask(task1); //id = 1
        Task task2 = new Task("Task2", "description");
        taskManager.createTask(task2); //id = 2
        Task updatedTask = new Task("updatedTask", "description", Status.IN_PROGRESS);

        Epic epic1 = new Epic("Epic1", "descr");
        taskManager.createEpic(epic1); //id = 3
        Epic epic2 = new Epic("Epic2", "descr");
        taskManager.createEpic(epic2); // id =4
        Epic updatedEpic = new Epic("UpdatedEpic", "Descrip");

        SubTask subTask1 = new SubTask("SubTask1", "descr", epic1);
        taskManager.createSubTask(subTask1); //id =5
        SubTask subTask2 = new SubTask("SubTask2", "descr", epic1);
        taskManager.createSubTask(subTask2); //id =6
        SubTask subTask3 = new SubTask("SubTask3", "descr", epic2);
        taskManager.createSubTask(subTask3); //id =7
        SubTask updatedSubTask = new SubTask("UpdatedSubTask", "descr", Status.DONE);
        SubTask subTask4 = new SubTask("SubTask3", "descr", Status.NEW);
        taskManager.createSubTask(subTask4);

        taskManager.updateTask(task1, updatedTask);
        taskManager.updateSubTask(subTask1, updatedSubTask);
        taskManager.updateEpic(epic1, updatedEpic);

        // Выводим все задачи
        System.out.println("=== Все задачи ===");
        System.out.println(taskManager.getAllTasks());
        System.out.println("\n=== Все подзадачи ===");
        System.out.println(taskManager.getAllSubTasks());
        System.out.println("\n=== Все эпики ===");
        System.out.println(taskManager.getAllEpics());

        // Просматриваем задачи для истории
        System.out.println("\n=== Просмотр задач ===");
        System.out.println(taskManager.getTask(1)); // Task
        System.out.println(taskManager.getEpic(3)); // Epic
        System.out.println(taskManager.getSubTask(5)); // SubTask

        // Многократно просматриваем одну задачу (должна остаться только одна запись в истории)
        for (int i = 0; i < 7; i++) {
            taskManager.getTask(1);
        }

        System.out.println(taskManager.getSubTask(7)); // Добавим еще одну задачу в историю, чтобы проверить лимит 10 задач

        System.out.println("История просмотра");
        List<Task> history = taskManager.getHistory();
        for (Task task : history) {
            System.out.println(task);
        }
    }
}

