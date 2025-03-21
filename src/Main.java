import management.TaskManager;
import task.Status;
import task.Task;
import task.Epic;
import task.SubTask;

public class Main {
    public static void main(String[] args) {

        TaskManager taskManager = new TaskManager();
        Task task1 = new Task("Task1", "description");
        taskManager.createTask(task1);

        Task task2 = new Task("Task2", "description");
        taskManager.createTask(task2);

        Epic epic1 = new Epic("Epic1", "descr");
        taskManager.createEpic(epic1);

        Epic epic2 = new Epic("Epic2", "descr");
        taskManager.createEpic(epic2);

        SubTask subTask1 = new SubTask("SubTask1", "descr", epic1);
        taskManager.createSubTask(subTask1);
        SubTask subTask2 = new SubTask("SubTask2", "descr", epic1);
        taskManager.createSubTask(subTask2);
        SubTask subTask3 = new SubTask("SubTask3", "descr", epic2);
        taskManager.createSubTask(subTask3);

        taskManager.updateTask(task1, "Updated Task 1", "desc", Status.DONE);
        taskManager.updateSubTask(subTask1, "Updated SubTask1", "desc", Status.IN_PROGRESS);

        System.out.println(taskManager.getAllTasks());
        System.out.println(taskManager.getAllSubTasks());
        System.out.println(taskManager.getAllEpics());



    }
}
